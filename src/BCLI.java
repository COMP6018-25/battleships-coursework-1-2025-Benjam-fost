import java.io.File;
import java.util.Locale;
import java.util.Scanner;

public class BCLI {
    private final Scanner scanner;
    private BModel model;

    public BCLI() {
        scanner = new Scanner(System.in);
        boolean selected = false;

        String instructions = "- There are 5 ships hidden in a 10x10 grid.\n" +
                "- There is one carrier, one battleship, one cruiser, one submarine and two destroyers.\n" +
                "- Every turn you can attack a cell and view the grid, showing your hits and misses.\n" +
                "- To attack a cell, enter a capital column letter then a row number.\n" +
                "- When a ship is sunk, or all ships are sunk, you will be notified.\n" +
                "| | Press 0 to start a new game || Press 1 to load a save file | |";

        System.out.println("\n\n/\\/ CLI Battleships /\\/\n\n| | Instructions | |");
        System.out.println(instructions);

        String line = scanner.nextLine();
        while (!selected) {
            // Starts a game with a random board
            if (line.contains("0")) {
                selected = true;
                model = new BModel();
            }
            // Starts a game with a user-created board
            else if (line.contains("1")) {
                // Creates a model with no ships
                model = new BModel(false);
                loadFile();
                selected = true;
            } else {
                System.out.println("| | Press 0 to start a new game || Press 1 to load a save file | |");
                line = scanner.nextLine();
            }
        }
        gameLoop();
    }

    private void loadFile() {
        System.out.println("All save files must be in the saves folder");
        for(;;) {
            System.out.print("Enter save file name -> ");
            // Processes the file path
            String filePath = "saves/" + scanner.nextLine().trim();
            if (!filePath.endsWith(".csv")) filePath += ".csv";
            // Loads the custom board and handles any failures
            boolean success = model.getGrid().loadShips(new File(filePath));
            if (success) break;
        }
    }

    private void gameLoop() {
        boolean running = true;
        int localSunkShips = 0;
        int size = model.getGrid().getSize() - 1;

        while(running) {
            // Shows the grid at every turn, with hits and misses
            System.out.println(model.getGrid());
            System.out.print("\nAttack cell -> ");
            int row, col;
            String input = scanner.nextLine().trim().toUpperCase();
            // Gets the column letter, then converts it into a column index on the grid
            try {
                col = input.charAt(0) - 'A';
            } catch (Exception e) {
                System.out.println("Enter a column letter, then a row number, with no spaces like A1 or J10");
                continue;
            }
            // Validates index
            if (col > size || col < 0) {
                System.out.println("Invalid column letter");
                continue;
            }

            // Gets the row index
            try {
                row = Integer.parseInt(input.substring(1)) - 1;
            } catch (Exception e) {
                System.out.println("Enter a column letter, then a row number, with no spaces like A1 or J10");
                continue;
            }
            // Validates the index
            if (row > size || row < 0) {
                System.out.println("Enter a row number between 1-10");
                continue;
            }

            model.attack(col, row);

            // Checks if a ship is sunk
            int modelSunkShips = model.getShipsSunk();
            if (modelSunkShips > localSunkShips) {
                localSunkShips = modelSunkShips;
                System.out.println("\n| | Ship sunk! | |");
            }
            // Checks if the game is won
            if (modelSunkShips == 5) {
                running = false;
                System.out.println("\n| | You win! | |\nAll ships sunk");
            }
        }
    }

    public static void main(String[] args) {
        new BCLI();
    }
}
