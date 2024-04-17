package core;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        //World fake = new World(56848903);
        //fake.groWorld();
        //fake.runGame();

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter valid string with format \"N#######S\"");
        String info = scan.nextLine();
        World world = getWorldFromInput(info);
        world.groWorld();
        world.runGame();
    }
    public static World getWorldFromInput(String info) {
        String numberStr = info.substring(1, info.length() - 1);  // Remove the first and last character
        long number = Long.parseLong(numberStr);
        return new World(number);
    }
}
