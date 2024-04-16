package core;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


//        World fake = new World(81957893);
//        fake.grow_World();
//        fake.runGame();

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter valid string with format \"N#######S\"");
        String info = scan.nextLine();
        World world = getWorldFromInput(info);
        world.grow_World();
        world.runGame();
    }
    public static World getWorldFromInput(String info) {
        String numberStr = info.substring(1, info.length() - 1);  // Remove the first and last character
        int number = Integer.parseInt(numberStr);
        return new World(number);
    }
}
