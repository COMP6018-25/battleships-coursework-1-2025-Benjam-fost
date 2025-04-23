/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.util.Scanner;

/**
 *
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
                BCLI cli = new BCLI();
            } else {
                System.out.println("Please press 0 for the CLI version || Press 1 for the GUI version");
                input = scanner.nextLine();
            }
        }
    }

    private static void launchGUI() {
        BModel model = new BModel();
        BController controller = new BController(model);
        BView view = new BView(model, controller);
    }
    
}
