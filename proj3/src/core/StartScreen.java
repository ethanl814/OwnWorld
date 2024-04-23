package core;

import edu.princeton.cs.algs4.In;
import tileengine.TERenderer;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
//import java.util.Scanner;

public class StartScreen {
    private final int DEFAULT_WIDTH = 800;
    private final int DEFAULT_HEIGHT = 800;
    private TERenderer ter;
    private Avatar aang;
    private String currTile;
    private String fakeTile;
    private String theme;
    private final String SAVE_FILE = "save.txt";
    private final String THEME_SAVE = "theme.txt";
    public StartScreen() { }
    public StartScreen(Avatar vivek, String currTile, String fakeTile, String theme) {
        aang = vivek;
        this.currTile = currTile;
        this.fakeTile = fakeTile;
        this.theme = theme;
    }

    public void realStartScreen() {
        StdDraw.setCanvasSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        StdDraw.setXscale(0, DEFAULT_WIDTH);
        StdDraw.setYscale(0, DEFAULT_HEIGHT);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Brush Script MT", Font.BOLD, 80));
        StdDraw.text(400, 550, "CS61B: THE GAME");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        StdDraw.text(400, 400, "New Game (N)");
        StdDraw.text(400, 350, "Load Game (L)");
        StdDraw.text(400, 300, "Quit (Q)");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 32));
        StdDraw.text(400, 200, "if you are reading this you are literate");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 28));
        StdDraw.text(80, 780, "look top right");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 23));
        StdDraw.text(715, 780, "look bottom right");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        StdDraw.text(736, 10, "look bottom left");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        StdDraw.text(35, 10, "good job");
        getInput();
    }
    public void getInput() {
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 'n' || input == 'N') {
                    inputSeedScreen();
                    break;
                }
                if (input == 'l' || input == 'L') {
                    loadGameScreen();
                    break;
                }
                if (input == 'q' || input == 'Q') {
                    System.exit(0);
                }
            }
            //StdDraw.pause(100); same as below
        }
    }
    public void inputSeedScreen() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 60));
        StdDraw.text(400, 450, "PUT YOUR SEED IN");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        StdDraw.text(400, 400, "(pause)");
        String in = "";
        StdDraw.setFont(new Font("Times New Roman", Font.ITALIC, 35));
        StdDraw.text(400, 300, "type a number");
        StdDraw.text(400, 260, "press 's' to confirm");
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 's' || input == 'S') {
                    //themeScreen();
                    if (!in.equals("")) {
                        makeWorld(in);
                    }
                }
                if (input == '1' || input == '2' || input == '3' || input == '4' || input == '5' || input == '6'
                        || input == '7' || input == '8' || input == '9' || input == '0') {
                    in += input;
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.setFont(new Font("Arial", Font.BOLD, 60));
                    StdDraw.text(400, 450, "PUT YOUR SEED IN");
                    StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                    StdDraw.text(400, 400, "(pause)");
                    StdDraw.setFont(new Font("Arial", Font.PLAIN, 40));
                    StdDraw.text(400, 350, in);
                    StdDraw.setFont(new Font("Times New Roman", Font.ITALIC, 35));
                    StdDraw.text(400, 300, "type a number");
                    StdDraw.text(400, 260, "press 's' to confirm");
                }
                if (input == '\b') {
                    in = in.substring(0, in.length() - 1);
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.setFont(new Font("Arial", Font.BOLD, 60));
                    StdDraw.text(400, 450, "PUT YOUR SEED IN");
                    StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                    StdDraw.text(400, 400, "(pause)");
                    StdDraw.setFont(new Font("Arial", Font.PLAIN, 40));
                    StdDraw.text(400, 350, in);
                    StdDraw.setFont(new Font("Times New Roman", Font.ITALIC, 35));
                    StdDraw.text(400, 300, "type a number");
                    StdDraw.text(400, 260, "press 's' to confirm");
                }
            }
            //StdDraw.pause(100); //rethan says this makes it better for gpu but might bring pauses
        }
    }
    public void loadGameScreen() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 60));
        StdDraw.text(400, 450, "PUT IN YOUR LOAD");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        StdDraw.text(400, 405, "(mega pause)");
        StdDraw.setFont(new Font("Arial", Font.BOLD, 30));
        StdDraw.text(400, 300, "press enter to confirm");
        String in = "";
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == '\n') {
                    loadWorld();
                }
            }
            //StdDraw.pause(100); //ethan says this makes it better for gpu but might bring pauses
        }
    }
    public void themeScreen() {
        StdDraw.setCanvasSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        StdDraw.setXscale(0, DEFAULT_WIDTH);
        StdDraw.setYscale(0, DEFAULT_HEIGHT);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Brush Script MT", Font.BOLD, 80));
        StdDraw.text(400, 550, "CHANGE DA THEME");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        StdDraw.text(400, 400, "Press 1 for Forest");
        StdDraw.text(400, 350, "Press 2 for Desert");
        StdDraw.text(400, 300, "Press 3 for Default");
        StdDraw.show();
        boolean run = true;
        while (run) {
            while (StdDraw.hasNextKeyTyped()) {
                aang.changeTheme(StdDraw.nextKeyTyped()); //themechanged
                run = false;
            }
        }
        aang = aang.saveFile(THEME_SAVE);
        aang = aang.loadFile(THEME_SAVE);
        ter = new TERenderer();
        ter.initialize(aang.getWorld().length, aang.getWorld()[0].length + 2);
        ter.drawTiles(aang.getWorld());
        aang.runGame(); //aang.runGame();
    }
    public void gameOverScreen() {
        StdDraw.setCanvasSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        StdDraw.setXscale(0, DEFAULT_WIDTH);
        StdDraw.setYscale(0, DEFAULT_HEIGHT);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 55));
        StdDraw.text(400, 650, "GAME OVER");
        StdDraw.text(400, 500, "YOU HAVE BEAT THE");
        if (overtheme().equals("default")) {
            StdDraw.setFont(new Font("Arial", Font.BOLD, 75));
            StdDraw.text(400, 420, "DEFAULT");
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 18));
            StdDraw.text(650, 100, "you really didn't change the theme??");
            StdDraw.text(650, 80, "looks like you are the default here");
            StdDraw.text(650, 60, "*default dances*");
        } else if (overtheme().equals("forest")) {
            StdDraw.setFont(new Font("Papyrus", Font.BOLD, 75));
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.text(400, 455, "FOREST");
            StdDraw.setFont(new Font("Papyrus", Font.PLAIN, 18));
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(650, 100, "your avatar has discovered a new");
            StdDraw.text(650, 80, "style of bending in the forest...");
            StdDraw.text(650, 60, "wood bending");
            StdDraw.text(650, 40, "season 5 coming soon");
        } else if (overtheme().equals("desert")) {
            StdDraw.setFont(new Font("Algerian", Font.BOLD, 75));
            StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.text(400, 420, "DESERT");
            StdDraw.setFont(new Font("Algerian", Font.PLAIN, 18));
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(650, 130, "have some 'dessert'");
            StdDraw.text(650, 110, "hahaha");
            StdDraw.text(650, 90, "what, was that joke too 'dry'?");
            StdDraw.text(650, 70, "hehehe");
            StdDraw.text(650, 50, "well you better find it funny");
            StdDraw.text(650, 30, "because i am the lisan al gaib");
            StdDraw.setFont(new Font("Algerian", Font.PLAIN, 22));
            StdDraw.text(650, 10, "SILENCE");
        }
        StdDraw.setFont(new Font("Comic Sans MS", Font.BOLD, 55));
        StdDraw.text(400, 350, "THEMED GAME");
    }

    public void makeWorld(String in) {
        long number = Long.parseLong(in);
        World prev = new World(number);
        aang = new Avatar(prev);
        //world.groWorld();
        aang.runGame();
    }
    public void loadWorld() {
        aang = load(SAVE_FILE);
        aang.runGame();
    }

    public void hud() {
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        StdDraw.setPenColor(Color.white);
        StdDraw.text(8, 52, "press 't' to change theme");
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        StdDraw.text(13, 51, "cores left: " + aang.getCoresLeft());
        StdDraw.setPenColor(Color.green);
        StdDraw.text(23, 52, "current theme: " + theme);
        StdDraw.setPenColor(Color.orange);
        StdDraw.text(28, 51, "seed: " + aang.getID());
        StdDraw.setPenColor(Color.magenta);
        StdDraw.text(38, 52, "press 'o' to toggle sight");
        StdDraw.setPenColor(Color.cyan);
        StdDraw.text(43, 51, "current tile: " + currTile);
        //StdDraw.text(33, 51, "current tile: " + fakeTile);
    }

    public Avatar load(String filename) {
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

    private String overtheme() {
        In file = new In(THEME_SAVE);
        int newtheme = 0;

        if (file.hasNextLine()) {
            String[] line1 = file.readLine().split(" ");
            newtheme = Integer.parseInt(line1[4]);
        }

        if (newtheme == 1) {
            return "forest";
        }
        if (newtheme == 2) {
            return "desert";
        }

        return "default";
    }
}
