package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class World {
    static final int DEFAULT_WIDTH = 50;
    static final int DEFAULT_HEIGHT = 50;
    TERenderer ter;
    TETile[][] board;
    Random random;
    int width;
    int height;
    private boolean isGameOver;
    public World(long seed) {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        ter = new TERenderer();
        ter.initialize(width, height);
        random = new Random(seed);
        TETile[][] nothingWorld = new TETile[width][height];
        fillWithNothing(nothingWorld);
        board = nothingWorld;
    }
    public void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }
    public void putHallway() {

    }
    public void putRoom() {

    }
}
