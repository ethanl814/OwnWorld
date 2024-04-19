package core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Avatar {
    private TETile[][] world;
    private TETile[][] base;
    private TERenderer ter;
    private int height;
    private int width;
    private int x;
    private int y;
    private int coresLeft;
    private List<int[]> cores;
    private long seedID;
    private boolean isGameOver;
    private static int theme;
    private static final String SAVE_FILE = "save.txt";
    private StartScreen screen;
    public Avatar(World world) {
        this.world = world.getWorld(); //builds the world
        this.base = buildBase(world.getWorld());
        height = this.world[0].length;
        width = this.world.length;
        isGameOver = false;
        theme = 0;

        cores = world.spawnCores(); //builds dungeon cores
        buildCores();

        int[] startCoords = world.spawn(); //spawns avatar
        x = startCoords[0];
        y = startCoords[1];
        this.world[x][y] = Tileset.AVATAR;
        if (cores.contains(startCoords)) {
            cores.remove(startCoords);
        }
        coresLeft = cores.size();

        seedID = world.getSeedID();
        screen = new StartScreen();
    }

    //themeifies the world
    private TETile[][] themeify() {
        if (theme == 1) {
            return themeifyHelper(world, Tileset.GRASS, Tileset.WATER, Tileset.CELL, Tileset.FLOWER);
        } else if (theme == 2) {
            return themeifyHelper(world, Tileset.SAND, Tileset.MOUNTAIN, Tileset.RED_CELL, Tileset.AVATAR);
        } else {
            return world;
        }
    }

    //helps themeify the world
    private TETile[][] themeifyHelper(TETile[][] tiles, TETile floors, TETile walls, TETile avatar, TETile cores) {
        int eight = tiles[0].length;
        int idth = tiles.length;
        TETile[][] ret = new TETile[idth][eight];
        World.fillWithNothing(ret);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == Tileset.WALL) {
                    ret[i][j] = walls;
                } else if (tiles[i][j] == Tileset.FLOOR) {
                    ret[i][j] = floors;
                } else if (tiles[i][j] == Tileset.CELL) {
                    ret[i][j] = cores;
                } else if (tiles[i][j] == Tileset.AVATAR) {
                    ret[i][j] = avatar;
                }
            }
        }
        return ret;
    }


    //places the cores on the world
    private void buildCores() {
        for (int[] core: cores) {
            int coreX = core[0];
            int coreY = core[1];
            world[coreX][coreY] = Tileset.CELL;
        }
    }

    //builds avatar when loading file
    public Avatar(TETile[][] world, TETile[][] base, int x, int y, long seedID, List<int[]> cores, int coresLeft) {
        this.world = world;
        this.base = base;
        height = this.world[0].length;
        width = this.world.length;
        this.x = x;
        this.y = y;
        this.cores = cores;
        this.coresLeft = coresLeft;
        screen = new StartScreen();
        this.seedID = seedID;
    }

    //allows for movement, saving, loading
    public void runGame() {
        ter = new TERenderer();
        ter.initialize(width, height + 2);
        while (!isGameOver) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 'w' || input == 'a' || input == 's' || input == 'd' || input == 'W' || input == 'A'
                        || input == 'S' || input == 'D') {
                    move(input);
                    ter.drawTiles(getWorld());
                }
                if (input == 'l' || input == 'L') {
                    loadFile(SAVE_FILE);
                }
                if (input == ':') {
                    saveFileCaller();
                }
                if (input == 't' || input == 'T') {
                    screen.themeScreen();
                }
            }
            renderBoard();
        }
    }
    public static void changeTheme(char input) {
        theme = Character.getNumericValue(input);
    }


    //calls save file
    public void saveFileCaller() {
        while (!isGameOver) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input != 'q' && input != 'Q') {
                    runGame();
                } else {
                    saveFile();
                    System.exit(0);
                }
            }
            renderBoard();
        }
    }

    //renders the board(for main)
    private void renderBoard() {
//        System.out.println(x + " " + y);
//        System.out.println(getWorld());
        StdDraw.clear(StdDraw.BLACK);
        hud();
        System.out.println(seedID);
        ter.drawTiles(getWorld());
        //renderAvatar();
        StdDraw.show();
    }
    public void hud() {
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        StdDraw.setPenColor(Color.white);
        StdDraw.text(10, 51, "press 't' to change theme");
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        StdDraw.text(20, 51, "cores left: " + cores.size());
        StdDraw.setPenColor(Color.green);
        StdDraw.text(31, 51, "current theme: " + getTheme());
        StdDraw.setPenColor(Color.orange);
        StdDraw.text(40, 51, "seed: " + seedID);
    }
    public String getTheme() {
        if (theme == 1) {
            return "forest";
        } if (theme == 2) {
            return "desert";
        }
        return "default";
    }
    private void checkBoard(int x, int y) {
        for (int[] core: cores) {
            if (core[0] == x && core[1] == y) {
                cores.remove(core);
                coresLeft--;
                return;
            }
        }
    }

    //returns the state of the world as an array
    public TETile[][] getWorld() {
        return themeify();
    }

    //moves character in a direction
    public void move(char command) {
        if (command == 'w' || command == 'W') { //up
            if (!(world[x][y + 1] == Tileset.WALL)) {
                world[x][y + 1] = Tileset.AVATAR;
                world[x][y] = base[x][y];
                y = y + 1;
                checkBoard(x, y);
            }
        } else if (command == 'a' || command == 'A') { //left
            if (!(world[x - 1][y] == Tileset.WALL)) {
                world[x - 1][y] = Tileset.AVATAR;
                world[x][y] = base[x][y];
                x = x - 1;
                checkBoard(x, y);
            }
        } else if (command == 's' || command == 'S') { //down
            if (!(world[x][y - 1] == Tileset.WALL)) {
                world[x][y - 1] = Tileset.AVATAR;
                world[x][y] = base[x][y];
                y = y - 1;
                checkBoard(x, y);
            }
        } else if (command == 'd' || command == 'D') { //right
            if (!(world[x + 1][y] == Tileset.WALL)) {
                world[x + 1][y] = Tileset.AVATAR;
                world[x][y] = base[x][y];
                x = x + 1;
                checkBoard(x, y);
            }
        }

        if (coresLeft == 0) {
            isGameOver = true;
        }
    }

    //saves the current state of the world
    public Avatar saveFile() {
        isGameOver = true;
        String ret = width + " " + height + " " + seedID; //saves dimensions
        for (int y1 = height - 1; y1 >= 0; y1--) { //saves curr state
            ret = ret + "\n";
            for (int x1 = 0; x1 < width; x1++) {
                if (world[x1][y1] == Tileset.WALL) {
                    ret = ret + "#";
                } else if (world[x1][y1] == Tileset.FLOOR) {
                    ret = ret + ".";
                } else if (world[x1][y1] == Tileset.AVATAR) {
                    ret = ret + "@";
                } else if (world[x1][y1] == Tileset.CELL) {
                    ret = ret + "c";
                } else {
                    ret = ret + " ";
                }
            }
        }
        ret = ret + "\n";
        ret = ret + "b";

        for (int y1 = height - 1; y1 >= 0; y1--) { //saves base
            ret = ret + "\n";
            for (int x1 = 0; x1 < width; x1++) {
                if (base[x1][y1] == Tileset.WALL) {
                    ret = ret + "#";
                } else if (base[x1][y1] == Tileset.FLOOR) {
                    ret = ret + ".";
                } else {
                    ret = ret + " ";
                }
            }
        }
        ret = ret + "\n";

        FileUtils.writeFile(SAVE_FILE, ret);
        return this;
    }

    //returns a list with elem 0 being the base world and elem 1 being the current state
    public static Avatar loadFile(String filename) {
        List<TETile[][]> load = new ArrayList<>();
        int aX = 0;
        int aY = 0;
        int newidth = 0;
        int newheight = 0;
        long newID = 0;
        List<int[]> newcores = new ArrayList<>();
        int newCoresLeft = 0;
        In file = new In(filename);

        if (file.hasNextLine()) {
            String[] line1 = file.readLine().split(" ");
            newidth = Integer.parseInt(line1[0]);
            newheight = Integer.parseInt(line1[1]);
            newID = Long.parseLong(line1[2]);
        }
        TETile[][] ret = new TETile[newidth][newheight];
        World.fillWithNothing(ret);
        int y1 = newheight - 1;
        while (file.hasNextLine()) {
            String[] line = file.readLine().split("");
            if (line[0].equals("b")) {
                break;
            }
            for (int x1 = 0; x1 < newidth; x1++) {
                if (line[x1].equals("#")) {
                    ret[x1][y1] = Tileset.WALL;
                } else if (line[x1].equals(".")) {
                    ret[x1][y1] = Tileset.FLOOR;
                } else if (line[x1].equals("c")) {
                    ret[x1][y1] = Tileset.CELL;
                    int[] core = new int[]{x1, y1};
                    newcores.add(core);
                    newCoresLeft++;
                } else if (line[x1].equals("@")) {
                    ret[x1][y1] = Tileset.AVATAR;
                    aX = x1;
                    aY = y1;
                }
            }
            y1--;
        }
        load.add(ret);




        TETile[][] newbase = new TETile[newidth][newheight];
        World.fillWithNothing(newbase);
        y1 = newheight - 1;
        while (file.hasNextLine()) {
            String[] line = file.readLine().split("");
            for (int x1 = 0; x1 < newidth; x1++) {
                if (line[x1].equals("#")) {
                    newbase[x1][y1] = Tileset.WALL;
                } else if (line[x1].equals(".")) {
                    newbase[x1][y1] = Tileset.FLOOR;
                } else if (line[x1].equals("@")) {
                    newbase[x1][y1] = Tileset.AVATAR;
                }
            }
            y1--;
        }
        load.add(newbase);


        Avatar mayberet = new Avatar(load.get(0), load.get(1), aX, aY, newID, newcores, newCoresLeft);
        return mayberet;
    }

    //copies the TETile array
    private TETile[][] buildBase(TETile[][] tiles) {
        int eight = tiles[0].length;
        int idth = tiles.length;
        TETile[][] ret = new TETile[idth][eight];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                ret[i][j] = tiles[i][j];
            }
        }
        return ret;
    }
}
