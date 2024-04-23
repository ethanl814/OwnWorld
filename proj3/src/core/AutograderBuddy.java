package core;


import tileengine.TETile;
import tileengine.Tileset;




public class AutograderBuddy {

    private static final String SAVE_FILE = "save.txt";


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
        Avatar aang;
        String saveFile = "save.txt";
        boolean load = false;
        String seed = "";
        boolean seeded = false;
        String movement = "";
        boolean moving = false;
        input.toLowerCase();
        char first = input.charAt(0);
        if (first == 'n') {
            seeded = true;
        } else {
            moving = true;
        }
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == 's') {
                seeded = false;
                moving = true;
            } else if (seeded) {
                seed = seed + input.charAt(i);
            } else if (moving) {
                movement = movement + input.charAt(i);
            }
        }
        if (seed.equals("")) {
            World ret = new World(1);
            aang = new Avatar(ret);
        } else {
            long number = Long.parseLong(seed);
            World ret = new World(number);
            aang = new Avatar(ret);
        }
        if (first == 'l') {
            aang = aang.loadFile(saveFile);
        }


        for (int j = 0; j < movement.length(); j++) {
            if (movement.charAt(j) == ':' && movement.charAt(j + 1) == 'q') {
                aang.saveFile(SAVE_FILE);
                return aang.getWorld();
            }
            aang.move(movement.charAt(j));
        }


        return aang.getWorld();
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

