package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import edu.princeton.cs.algs4.StdDraw;

public class World {
    static final int DEFAULT_WIDTH = 50;
    static final int DEFAULT_HEIGHT = 50;
    TERenderer ter;
    TETile[][] board;
    Random seed;
    int width;
    int height;
    int total_area;
    int area_used;
    private List<Room> rooms = new ArrayList<>();
    private static final String SAVE_FILE = "src/save.txt";
    private boolean isGameOver;

    public World(long seed) {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        ter = new TERenderer();
        ter.initialize(width, height);
        this.seed = new Random(seed);
        TETile[][] nothingWorld = new TETile[width][height];
        fillWithNothing(nothingWorld);
        board = nothingWorld;
        total_area = width * height;
        area_used = 0;
    }

    public void runGame() {
        renderBoard();
    }

    //renders the board(for main)
    private void renderBoard() {
        System.out.println(board);
        ter.drawTiles(board);
        StdDraw.show();
    }

    public TETile[][] grow_World() {
        grow_Rooms();
        return board;
    }

    public void putHallway() {

    }

    //grows all valid rooms
    private void grow_Rooms() {
        grow_RoomsHelper();
        for (int i = rooms.size() - 1; i >= 0; i--) {
                if (!isValidRoom(rooms.get(i))) {
                    rooms.remove(rooms.get(i));
                }
            }
        fillWithNothing(board);
        for (Room room: rooms) {
            if (check(room)) {
                populateRoom(room);
            }
        }
        if (area_used < (total_area / 3)) {
            fillWithNothing(board);
            grow_RoomsHelper();
            grow_Rooms();
        }
    }

    //recursively grows rooms
    private void grow_RoomsHelper() {
        int i = createPoint();
        if (i > 0) {
            Room now = rooms.get(i);
            if (check(now)) {
                populateRoom(now);
            }
        }
        if (area_used < (total_area / 3)) {
            grow_RoomsHelper();
        }
    }

    //creates a valid room object
    private int createPoint() {
        int x = RandomUtils.uniform(seed, 1, width - 1);
        int y = RandomUtils.uniform(seed, 1, height - 1);
        Room now = new Room(x, y);
        if (check(now)) {
            rooms.add(now);
            return rooms.indexOf(now);
        }
        return -1;
    }

    //Populates a room with tiles
    private void populateRoom(Room room) {
        area_used = area_used + room.area();
        for (int i = room.x; i <= room.x_bound; i++) {
            for (int j = room.y; j <= room.y_bound; j++) {
                if (i == room.x || i == room.x_bound || j == room.y || j == room.y_bound) {
                    board[i][j] = Tileset.WALL;
                } else {
                    board[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    //checks if the room created is valid(doesn't work for overlapping rooms)
    private boolean check(Room room) {
        TETile topL = board[room.x][room.y_bound];
        TETile botL = board[room.x][room.y];
        TETile topR = board[room.x_bound][room.y_bound];
        TETile botR = board[room.x_bound][room.y];
        if ((room.x_bound - room.x - 2) < 3 || (room.y_bound - room.y - 2) < 3) {
            return false;
        }
        if (topL != Tileset.NOTHING || topR != Tileset.NOTHING) {
            room.y_bound = room.y_bound - 1;
            check(room);
        }
        if (botR != Tileset.NOTHING || botL != Tileset.NOTHING) {
            room.y = room.y + 1;
            check(room);
        }
        return true;
    }

    //fills the board with nothing
    private void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    //checks if a room is valid(meant for deleting overlapping rooms)
    private boolean isValidRoom(Room room) {
        for(int x = room.x; x <= room.x_bound; x++) {
            for (int y = room.y; y <= room.y_bound; y++) {
                if (x == room.x || x == room.x_bound || y == room.y || y == room.y_bound) {
                    if (board[x][y] != Tileset.WALL) {
                        return false;
                    }
                } else {
                    if (board[x][y] != Tileset.FLOOR) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //class for rooms, each room is a Room object
    private class Room {
        public int x; //bottom left x coord
        public int y; //bottom left y coord
        public int x_bound; //top right x coord
        public int y_bound; //top right y coord

        //initializes a room, makes sure it fits in the board
        public Room(int x, int y) {
            this.x = x;
            this.y = y;
            x_bound = x + RandomUtils.uniform(seed, 4, 9); // binds a room with the values in randomutils
            y_bound = y + RandomUtils.uniform(seed, 4, 9);
            if (x_bound >= width - 1) { //ensures room fits
                x_bound = width - 2;
            }
            if (y_bound >= height - 1) {
                y_bound = height - 2;
            }
        }

        //area of a room
        private int area() {
            return (x_bound - x) * (y_bound - y);
        }
    }
}