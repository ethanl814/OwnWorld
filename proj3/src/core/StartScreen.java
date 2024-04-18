package core;

import tileengine.TERenderer;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.*;
import java.util.Scanner;

public class StartScreen {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 800;
    private TERenderer ter;
    public StartScreen() {
        StdDraw.setCanvasSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        StdDraw.setXscale(0, DEFAULT_WIDTH);
        StdDraw.setYscale(0, DEFAULT_HEIGHT);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Brush Script MT", Font.BOLD, 80));
        StdDraw.text(400,550, "CS61B: THE GAME");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        StdDraw.text(400, 400, "New Game (N)");
        StdDraw.text(400, 350, "Load Game (L)");
        StdDraw.text(400, 300, "Quit (Q)");
        StdDraw.text(400, 200, "if you are reading this you ass at basketball");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 28));
        StdDraw.text(80, 780, "look top right");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 23));
        StdDraw.text(715, 780, "look bottom right");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        StdDraw.text(736, 10, "look bottom left");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        StdDraw.text(35, 10, "good boy");
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

                }
                if (input == 'q' || input == 'Q') {

                }
            }
            //StdDraw.pause(100); same as below
        }
    }
    public void inputSeedScreen(){
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 60));
        StdDraw.text(400,450, "PUT YOUR SEED IN");
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        StdDraw.text(400, 400, "(pause)");
        String in = "";
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 's' || input == 'S') {
                    makeWorld(in);
                }
                if (input == '1' || input == '2' || input == '3' || input == '4' || input == '5' || input == '6' || input == '7' || input == '8' || input == '9' || input == '0') {
                    in += input;
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.setFont(new Font("Arial", Font.BOLD, 60));
                    StdDraw.text(400,450, "PUT YOUR SEED IN");
                    StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                    StdDraw.text(400, 400, "(pause)");
                    StdDraw.setFont(new Font("Arial", Font.PLAIN, 40));
                    StdDraw.text(400, 350, in);
                }
            }
//            StdDraw.pause(100); //rethan says this makes it better for gpu but might bring pauses
        }
    }
    public void makeWorld(String in) {
        long number = Long.parseLong(in);
        World prev = new World(number);
        Avatar aang = new Avatar(prev);
        //world.groWorld();
        aang.runGame();
    }
}
