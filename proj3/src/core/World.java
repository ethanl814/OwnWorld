package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    static final int DEFAULT_WIDTH = 100;
    static final int DEFAULT_HEIGHT = 100;
    TERenderer ter;
    TETile[][] board;
    Random seed;
    int width;
    int height;
    int total_area;
    int area_used;
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

    public String showWorld() {
        String ret = "";
        for (int y = height - 1; y >= 0; y--) {
            ret = ret + "\n";
            for (int x = 0; x < width; x++) {
                if (board[x][y] == Tileset.WALL) {
                    ret = ret + "#";
                } else {
                    ret = ret + "*";
                }
            }
        }
        ret = ret + "\n";
        FileUtils.writeFile(SAVE_FILE, ret);
        return ret;
    }

    public TETile[][] grow_World() {
        grow_Rooms();
        return board;
    }

    public void putHallway() {

    }
    private void grow_Rooms() {
        List<Node> rooms = createPoints();
        for (int i = 0; i < rooms.size(); i++) {
            Node now = rooms.get(i);
            if (check(now, rooms)) {
                populateRoom(now);
            }
        }
    }

    private List<Node> createPoints() {
        int amt = RandomUtils.uniform(seed, 10, 16);
        List<Node> mids = new ArrayList<>();
        for (int i = 0; i < amt; i++) {
            int x = RandomUtils.uniform(seed, 0, 95);
            int y = RandomUtils.uniform(seed, 0, 95);
            Node now = new Node(x, y);
            int passed = 0;
            for (Node other: mids) {
                if (other.compareTo(now) > 0) {
                    passed++;
                }
            }
            if (passed == mids.size()) {
                mids.add(now);
            }
        }
        return mids;
    }

    private void populateRoom(Node node) {
        for (int i = node.x; i < node.x_bound; i++) {
            for (int j = node.y; j < node.y_bound; j++) {
                if (i == node.x || i == node.x_bound || j == node.y || j == node.y_bound) {
                    board[i][j] = Tileset.WALL;
                } else {
                    board[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    private boolean check(Node node, List<Node> nodes) {
        TETile topL = board[node.x][node.y_bound];
        TETile botL = board[node.x][node.y];
        TETile topR = board[node.x_bound][node.y_bound];
        TETile botR = board[node.x_bound][node.y];
        if (node.area() < 16) {
            nodes.remove(node);
            return false;
        }
        if (topL != Tileset.NOTHING || topR != Tileset.NOTHING) {
            node.y_bound = node.y_bound - 1;
            check(node, nodes);
        } else if (botR != Tileset.NOTHING && botL != Tileset.NOTHING) {
            node.y = node.y + 1;
            check(node, nodes);
        }
        return true;
    }

    private void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private class Node implements Comparable<Node> {
        public int x;
        public int y;
        public int x_bound;
        public int y_bound;
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            x_bound = x + RandomUtils.uniform(seed, 5, 12);
            y_bound = y + RandomUtils.uniform(seed, 5, 12);
            if (x_bound >= 100) {
                x_bound = 99;
            } else if (y_bound >= 100) {
                y_bound = 99;
            }
        }
        private int area() {
            return (x_bound - x) * (y_bound - y);
        }
        @Override
        public int compareTo(Node other) {
            if ((Math.abs(this.x - other.x) <= 4) && (Math.abs(this.y - other.y) <= 4)) {
                return -1;
            } else return 1;
        }
    }
}