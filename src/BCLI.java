import java.util.Locale;
import java.util.Scanner;

public class BCLI {
    private final Scanner scanner;
    private final BModel model;

    public BCLI() {
        model = new BModel();
        scanner = new Scanner(System.in);
        String instructions = "- There are 5 ships hidden in a 10x10 grid.\n" +
                "- There is one carrier, one battleship, one cruiser, one submarine and two destroyers.\n" +
                "- Every turn you can attack a cell and view the grid, showing your hits and misses.\n" +
                "- To attack a cell, enter a capital column letter then a row number.\n" +
                "- When a ship is sunk, or all ships are sunk, you will be notified.\n" +
                "| | Press any key to continue | |";
        System.out.println("\n\n/\\/ CLI Battleships /\\/\n\n| | Instructions | |");
        System.out.println(instructions);
        scanner.nextLine();
        gameLoop();
    }

    private void gameLoop() {
        boolean running = true;
        int sunkShips = 0;
        int size = model.getGrid().getSize() - 1;

        while(running) {
            // Shows the grid at every turn, with hits and misses
            System.out.println(model.getGrid());
            System.out.print("\nAttack cell -> ");
            int x, y;
            String input = scanner.nextLine().trim().toUpperCase();
            // Gets the column letter, then converts it into a y-position on the grid
            try {
                y = input.charAt(0) - 'A';
            } catch (Exception e) {
                System.out.println("Enter a column letter, then a row number, with no spaces like A1 or J10");
                continue;
            }
            // Validates y-position
            if (y > size || y < 0) {
                System.out.println("Invalid column letter");
                continue;
            }

            // Gets the x-position
            try {
                x = Integer.parseInt(input.substring(1)) - 1;
            } catch (Exception e) {
                System.out.println("Enter a column letter, then a row number, with no spaces like A1 or J10");
                continue;
            }
            // Validates the x-position
            if (x > size || x < 0) {
                System.out.println("Enter a row number between 1-10");
                continue;
            }

            model.attack(x, y);
            // Checks if a ship is sunk
            if (model.getShipsSunk() > sunkShips) {
                System.out.println("| | Ship sunk! | |");
            }
            // Checks if the game is won
            if (model.getShipsSunk() == 5) {
                running = false;
                System.out.println("| | You win! | |\nAll ships sunk");
            }
        }
    }

    public static void main(String[] args) {
        new BCLI();
    }
}
