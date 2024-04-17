package core;

import tileengine.TETile;
import tileengine.Tileset;


public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        boolean load = false;
        String seed = "";
        boolean seeded = false;
        String movement = "";
        boolean moving = false;
        input.toLowerCase();
        if (input.charAt(0) == 'l') {
//            loadS
        }
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == ':' && (input.charAt(i + 1) == 'Q')) {
//                break;
            }
            if (input.charAt(i) == 'L') {
//                loadFile();
            }
            if (input.charAt(i) == 'N') {
                seeded = true;
            } else if (input.charAt(i) == 'S') {
                seeded = false;
                moving = true;
            }
            if (seeded) {
                seed = seed + input.charAt(i);
            } else if (moving) {
                movement = movement + input.charAt(i);
            }
        }
        long number = Long.parseLong(seed);
        World ret = new World(number);
        Avatar aang = new Avatar(ret);

        for (int j = 0; j < movement.length(); j++) {
            aang.move(movement.charAt(j));
        }

//        return aang.getWorld();
        return ret.getWorld();

    }


    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
