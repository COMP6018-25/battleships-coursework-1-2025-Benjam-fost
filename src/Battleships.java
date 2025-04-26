/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.util.Scanner;

/**
 * The main class for the Battleships game.
 * @author Ben Foster
 */
public class Battleships {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("|| Welcome to Battleships! ||");
        System.out.println("Press 0 for the CLI version || Press 1 for the GUI version");
        String input = scanner.nextLine();
        boolean selecting = true;

        while (selecting) {
            if (input.equals("1")) {
                selecting = false;
                javax.swing.SwingUtilities.invokeLater(Battleships::launchGUI);
            } else if (input.equals("0")){
                selecting = false;
                new CLI();
            } else {
                System.out.println("Please press 0 for the CLI version || Press 1 for the GUI version");
                input = scanner.nextLine();
            }
        }
    }

    /**
     * Creates a new View instance, which starts a new game or loads a custom board.
     */
    private static void launchGUI() {
        Model model = new Model();
        Controller controller = new Controller(model);
        new View(model, controller);
    }
    
}
