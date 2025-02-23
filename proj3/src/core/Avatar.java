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
    private TETile[][] visionboard;
    private TERenderer ter;
    private int height;
    private int width;
    private String currTile;
    private String fakeTile;
    private int x;
    private int y;
    private int coresLeft;
    private List<int[]> cores;
    private long seedID;
    private Random seed;
    private boolean isGameOver;
    private int theme;
    private boolean sight;
    private final String SAVE_FILE = "save.txt";
    private final String THEME_SAVE = "theme.txt";
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

        seed = new Random(seedID);

        sight = true;
    }

    //themeifies the world
    private TETile[][] themeify() {
        TETile[][] tiles = new TETile[width][height];
        if (sight) {
            tiles = world;
        } else {
            tiles = vision();
        }
        if (theme == 1) {
            return themeifyHelper(tiles, Tileset.GRASS, Tileset.WATER, Tileset.FLOWER, Tileset.YELLOW_CELL);
        } else if (theme == 2) {
            return themeifyHelper(tiles, Tileset.SAND, Tileset.MOUNTAIN, Tileset.RED_CELL, Tileset.TREE);
        } else {
            return tiles;
        }
    }

    //helps themeify the world
    private TETile[][] themeifyHelper(TETile[][] tiles, TETile floors, TETile walls, TETile avatar, TETile cres) {
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
                    ret[i][j] = cres;
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
    public Avatar(TETile[][] world, TETile[][] base, int[] coords, long seedID, List<int[]> cores, int coresLeft,
                  boolean newsight, int newtheme) {
        this.world = world;
        this.base = base;
        height = this.world[0].length;
        width = this.world.length;
        this.x = coords[0];
        this.y = coords[1];
        this.cores = cores;
        this.coresLeft = coresLeft;
        this.seedID = seedID;
        seed = new Random(seedID);
        this.sight = newsight;
        theme = newtheme;
    }

    //allows for movement, saving, loading
    public void runGame() {
        ter = new TERenderer();
        ter.initialize(width, height + 3);
        while (!isGameOver) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 'w' || input == 'a' || input == 's' || input == 'd' || input == 'W' || input == 'A'
                        || input == 'S' || input == 'D') {
                    move(input);
                    ter.drawTiles(getWorld());
                }
                if (input == 'o' || input == 'O') {
                    sight = !sight;
                    ter.drawTiles(getWorld());
                }
                if (input == 'l' || input == 'L') {
                    loadFile(SAVE_FILE);
                }
                if (input == ':') {
                    saveFileCaller();
                }
                if (input == 't' || input == 'T') {
                    screen().themeScreen();
                }
            }
            //System.out.println(theme);
            renderBoard();
            double x9 = StdDraw.mouseX();
            double y9 = StdDraw.mouseY();
            mouseTrack(x9, y9);
        }
        renderBoard();
        screen().gameOverScreen();
        StdDraw.show();
    }
    public void mouseTrack(double x9, double y9) {
        int arrX = (int) x9;
        int arrY = (int) y9;
        if (arrX < 0 || arrX >= width || arrY < 0 || arrY >= height) {
            currTile = "nothing";
            fakeTile = "out of this world";
            return;
        }
        fakeTile = arrX + " " + arrY;
        currTile = getWorld()[arrX][arrY].description();
    }
    public void changeTheme(char input) {
        theme = Character.getNumericValue(input);
    }

    private TETile[][] vision() {
        int eight = world[0].length;
        int idth = world.length;
        TETile[][] ret = new TETile[idth][eight];
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (i - x <= 3 && j - y <= 3 && x - i <= 3 && y - j <= 3) {
                    ret[i][j] = world[i][j];
                } else {
                    ret[i][j] = Tileset.NOTHING;
                }
            }
        }
        return ret;
    }

    //calls save file
    public void saveFileCaller() {
        while (!isGameOver) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input != 'q' && input != 'Q') {
                    runGame();
                } else {
                    saveFile(SAVE_FILE);
                    System.exit(0);
                }
            }
            renderBoard();
        }
    }

    //renders the board(for main)
    private void renderBoard() {
        StdDraw.clear(StdDraw.BLACK);
        screen().hud();
        ter.drawTiles(getWorld());
        //renderAvatar();
        StdDraw.show();
    }

    public String getTheme() {
        if (theme == 1) {
            return "forest";
        }
        if (theme == 2) {
            return "desert";
        }
        return "default";
    }
    private void checkBoard(int x1, int y1) {
        for (int[] core: cores) {
            if (core[0] == x1 && core[1] == y1) {
                cores.remove(core);
                coresLeft--;
                return;
            }
        }
    }

    public int getCoresLeft() {
        return coresLeft;
    }

    public long getID() {
        return seedID;
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
    public Avatar saveFile(String file) {
        isGameOver = true;
        String ret = width + " " + height + " " + seedID + " " + sight + " " + theme; //saves dimensions
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

        FileUtils.writeFile(file, ret);
        return this;
    }

    //returns a list with elem 0 being the base world and elem 1 being the current state
    public Avatar loadFile(String filename) {
        List<TETile[][]> load = new ArrayList<>();
        int aX = 0;
        int aY = 0;
        int newidth = 0;
        int newheight = 0;
        long newID = 0;
        boolean newsight = false;
        int newtheme = 0;
        int[] coords;
        List<int[]> newcores = new ArrayList<>();
        int newCoresLeft = 0;
        In file = new In(filename);

        if (file.hasNextLine()) {
            String[] line1 = file.readLine().split(" ");
            newidth = Integer.parseInt(line1[0]);
            newheight = Integer.parseInt(line1[1]);
            newID = Long.parseLong(line1[2]);
            newsight = Boolean.parseBoolean(line1[3]);
            newtheme = Integer.parseInt(line1[4]);
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

        coords = new int[]{aX, aY};
        Avatar mayberet = new Avatar(load.get(0), load.get(1), coords, newID, newcores,
                newCoresLeft, newsight, newtheme);
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
    
    public StartScreen screen() {
        return new StartScreen(this, currTile, fakeTile, getTheme());
    }
}
