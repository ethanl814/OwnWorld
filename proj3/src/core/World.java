package core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;
import utils.RandomUtils;

import java.util.*;

import edu.princeton.cs.algs4.StdDraw;

//This is unnecessarily complicated ngl, took way to long
public class World {
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;
    private TERenderer ter;
    private TETile[][] board;
    private Random seed;
    private int width;
    private int height;
    private int total_area;
    private int area_used;
    private List<Room> rooms = new ArrayList<>();
    private WeightedQuickUnionUF halls;
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

    //does jacks*** ethan is dumb
    public void runGame() {
        renderBoard();
    }

    //renders the board(for main)
    private void renderBoard() {
        System.out.println(board);
        ter.drawTiles(board);
        StdDraw.show();
    }

    //Lelouch VI Britannia commands you, grow, grow WORLD!
    public TETile[][] grow_World() {
        grow_Rooms();
        halls = new WeightedQuickUnionUF(rooms.size());
        growHallways();
        return board;
    }
            
    //grows hallways starting at a random room
    private void growHallways() { 
        int i = RandomUtils.uniform(seed, 0, rooms.size());
        Room base = rooms.get(i);
        growHallwaysHelper(base, 0);
    }
    
    //recursively picks which rooms to connect and fuses them
    private void growHallwaysHelper(Room curr, int iter) {
        int connectedness = 0;
        for (int i = 0; i < rooms.size(); i++) {
            if (halls.connected(rooms.indexOf(curr), i)) {
                connectedness++;
            }
        }
        if (connectedness == rooms.size()) {
            return;
        }
        for (int j = 0; j < curr.closest().size(); j++) {
            Room nex = curr.closest().get(j);
            int nexIndex = rooms.indexOf(nex);
            int currIndex = rooms.indexOf(curr);
//            if (iter == 13) { //exists for debugging purposes
//                return;
//            }
            if (!halls.connected(currIndex, nexIndex)) {
                halls.union(currIndex, nexIndex);
                connect(curr.center, nex.center);
                growHallwaysHelper(nex, iter + 1);
                break;
            }
        }
    }

    //grows all valid rooms
    private void grow_Rooms() {
        grow_RoomsHelper(0);
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
            grow_RoomsHelper(1);
            grow_Rooms();
        }
    }

    //recursively grows rooms
    private void grow_RoomsHelper(int count) {
        int i = createPoint();
        if (i > 0) {
            Room now = rooms.get(i);
            if (check(now)) {
                populateRoom(now);
                count++;
            }
        }
        if (area_used < (total_area / 3)) {
            grow_RoomsHelper(count);
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
                if ((i == room.x || i == room.x_bound || j == room.y || j == room.y_bound) && board[i][j] != Tileset.FLOOR) {
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
            return check(room);
        }
        if (botR != Tileset.NOTHING || botL != Tileset.NOTHING) {
            room.y = room.y + 1;
            return check(room);
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
        public int[] center = new int[2]; //x, y coord of center in that order
        public HashMap<Integer, Integer> edges;

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

            center[0] = x + (x_bound - x) / 2;
            center[1] = y + (y_bound - y) / 2;



//            for (int i = x + 1; i < x_bound; i++) {
//                topXs.add(i);
//                botXs.add(i);
//            }
//
//            for (int j = y + 1; j < y_bound; j++) {
//                rightYs.add(j);
//                leftYs.add(j);
//            }
        }

        //area of a room
        private int area() {
            return (x_bound - x) * (y_bound - y);
        }

        //distance between 2 rooms
        private double distance(Room room) {
            return Math.sqrt(Math.pow((center[0] - room.center[0]), 2) + Math.pow((center[1] - room.center[1]), 2));
        }

        //list of rooms from closest to farthest based on the center of each
        private List<Room> closest() {
            List<Room> ret = new ArrayList<>();
            TreeMap<Double, Room> penalties = new TreeMap<>();
            for (Room room: rooms) {
                penalties.put(distance(room), room);
            }
            List<Double> keys = new ArrayList<>(penalties.keySet());
            Collections.sort(keys);
            for (Double key: keys) {
                ret.add(penalties.get(key));

            }
            return ret;
        }
    }

    //helper function that connects 2 rooms based on their center coordinates
    public void connect(int[] center1, int[] center2) {
        int startX = Math.min(center1[0], center2[0]);
        int startY;
        int endX;
        int endY;
        if (center1[0] == startX) {
            endX = center2[0];
            endY = center2[1];
            startY = center1[1];
        } else {
            endX = center1[0];
            endY= center1[1];
            startY = center2[1];
        }

        int x = startX;
        if (endY > startY) {
            for (int y = startY + 1; y <= endY; y++) {
                if (board[x + 1][y] != Tileset.FLOOR) {
                    board[x + 1][y] = Tileset.WALL;
                }
                board[x][y] = Tileset.FLOOR;
                if (board[x - 1][y] != Tileset.FLOOR) {
                    board[x - 1][y] = Tileset.WALL;
                }
            }
            if (board[startX - 1][endY + 1] != Tileset.FLOOR) {
                board[startX - 1][endY + 1] = Tileset.WALL;
            }
        } else {
            for (int y = startY; y >= endY; y--) {
                if (board[x + 1][y] != Tileset.FLOOR) {
                    board[x + 1][y] = Tileset.WALL;
                }
                board[x][y] = Tileset.FLOOR;
                if (board[x - 1][y] != Tileset.FLOOR) {
                    board[x - 1][y] = Tileset.WALL;
                }
            }
            if (board[startX - 1][endY - 1] != Tileset.FLOOR) {
                board[startX - 1][endY - 1] = Tileset.WALL;
            }
        }

        int y = endY;
        for (x = startX; x < endX; x++) {
            if(board[x][y + 1] != Tileset.FLOOR) {
                board[x][y + 1] = Tileset.WALL;
            }
            board[x][y] = Tileset.FLOOR;
            if(board[x][y - 1] != Tileset.FLOOR) {
                board[x][y - 1] = Tileset.WALL;
            }
        }

//        exists for debugging purposes:
//        board[center1[0]][center1[1]] = Tileset.AVATAR;
//        board[center2[0]][center2[1]] = Tileset.CELL;
//
//        board[startX][startY] = Tileset.FLOWER;
//        board[endX][endY] = Tileset.GRASS;
    }
}