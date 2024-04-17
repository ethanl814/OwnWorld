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
    private Random seed;
    private long seedID;
    private static final String SAVE_FILE = "src/save.txt";
    public Avatar(World world) {
        this.world = world.getWorld();
        this.base = buildBase(world.getWorld());
        height = this.world[0].length;
        width = this.world.length;
        int[] startCoords = world.spawn();
        x = startCoords[0];
        y = startCoords[1];
        this.world[x][y] = Tileset.AVATAR;
        seed = new Random(seedID);
    }

    public Avatar(TETile[][] world, TETile[][] base, int x, int y, long seedID) {
        this.world = world;
        this.base = base;
        height = this.world[0].length;
        width = this.world.length;
        this.x = x;
        this.y = y;
        this.seedID = seedID;
        seed = new Random(this.seedID);
    }
    public void runGame() {
        ter = new TERenderer();
        ter.initialize(width, height);
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 'w' || input == 'a' || input == 's' || input == 'd') {
                    move(input);
                    ter.drawTiles(world);
                }
                if (input == 'l') {
                    loadFile(SAVE_FILE);
                }
                if (input == ':') {
                    saveFileCaller();
                }
            }
            renderBoard();
        }
    }
    public void saveFileCaller() {
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input != 'q') {
                    runGame();
                } else {
                    saveFile();
                }
            }
            renderBoard();
        }
    }

    //renders the board(for main)
    private void renderBoard() {
        System.out.println(x + " " + y);
        System.out.println(world);
        ter.drawTiles(world);
//        renderAvatar();
        StdDraw.show();
    }

    //returns the state of the world as a array
    public TETile[][] groWorld() {
        return world;
    }

    //moves character in a direction
    public void move(char command) {
        if (command == 'w') { //up
            if (!(world[x][y + 1] == Tileset.WALL)) {
                world[x][y + 1] = Tileset.AVATAR;
                world[x][y] = base[x][y];
                y = y + 1;
            }
        } else if (command == 'a') { //left
            if (!(world[x - 1][y] == Tileset.WALL)) {
                world[x - 1][y] = Tileset.AVATAR;
                world[x][y] = base[x][y];
                x = x - 1;
            }
        } else if (command == 's') { //down
            if (!(world[x][y - 1] == Tileset.WALL)) {
                world[x][y - 1] = Tileset.AVATAR;
                world[x][y] = base[x][y];
                y = y - 1;
            }
        } else if (command == 'd') { //right
            if (!(world[x + 1][y] == Tileset.WALL)) {
                world[x + 1][y] = Tileset.AVATAR;
                world[x][y] = base[x][y];
                x = x + 1;
            }
        }
    }

    //saves the current state of the world
    public void saveFile() {
        String ret = width + " " + height + " " + seedID; //saves dimensions
        for (int y = height - 1; y >= 0; y--) { //saves curr state
            ret = ret + "\n";
            for (int x = 0; x < width; x++) {
                if (world[x][y] == Tileset.WALL) {
                    ret = ret + "#";
                } else if (world[x][y] == Tileset.FLOOR) {
                    ret = ret + ".";
                } else if (world[x][y] == Tileset.AVATAR) {
                    ret = ret + "@";
                } else {
                    ret = ret + " ";
                }
            }
        }
        ret = ret + "\n";
        ret = ret + "b";

        for (int y = height - 1; y >= 0; y--) { //saves base
            ret = ret + "\n";
            for (int x = 0; x < width; x++) {
                if (base[x][y] == Tileset.WALL) {
                    ret = ret + "#";
                } else if (base[x][y] == Tileset.FLOOR) {
                    ret = ret + ".";
                } else if (base[x][y] == Tileset.AVATAR) {
                    ret = ret + "@";
                } else {
                    ret = ret + " ";
                }
            }
        }
        ret = ret + "\n";

        FileUtils.writeFile(SAVE_FILE, ret);
    }

    //returns a list with elem 0 being the base world and elem 1 being the current state
    public Avatar loadFile(String filename) {
        List<TETile[][]> load = new ArrayList<>();
        int aX = 0;
        int aY = 0;
        int newidth = 0;
        int newheight = 0;
        long newID = 0;

        In file = new In(filename);
        if (file.hasNextLine()) {
            String[] line1 = file.readLine().split(" ");
            if (line1.length == 0) {
                return this;
            }
            newidth = Integer.parseInt(line1[0]);
            newheight = Integer.parseInt(line1[1]);
            newID = Long.parseLong(line1[2]);
        }
        TETile[][] ret = new TETile[newidth][newheight];
        World.fillWithNothing(ret);
        int y = newheight - 1;
        while (file.hasNextLine()) {
            String[] line = file.readLine().split("");
            if (line[0] == "b") {
                break;
            }
            for (int x = 0; x < newidth; x++) {
                if (line[x] == "#") {
                    ret[x][y] = Tileset.WALL;
                } else if (line[x] == ".") {
                    ret[x][y] = Tileset.FLOOR;
                } else if (line[x] == "@") {
                    ret[x][y] = Tileset.AVATAR;
                    aX = x;
                    aY = y;
                }
            }
            y--;
        }
        load.add(ret);


        TETile[][] base = new TETile[newidth][newheight];
        World.fillWithNothing(ret);
        y = newheight - 1;
        while (file.hasNextLine()) {
            String[] line = file.readLine().split("");
            if (line[0] == "b") {
                break;
            }
            for (int x = 0; x < newidth; x++) {
                if (line[x] == "#") {
                    base[x][y] = Tileset.WALL;
                } else if (line[x] == ".") {
                    base[x][y] = Tileset.FLOOR;
                } else if (line[x] == "@") {
                    base[x][y] = Tileset.AVATAR;
                }
            }
            y--;
        }
        load.add(base);

        Avatar mayberet = new Avatar(load.get(1), load.get(0), aX, aY, newID);
        return mayberet;
    }

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
