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
        System.out.println("Welcome to Battleships!");
        System.out.println("Press 0 for the CLI version | Press 1 for the GUI version");
        if (scanner.nextLine().equals("1")) {
            javax.swing.SwingUtilities.invokeLater(Battleships::createAndShowGUI);
        } else {
            BCLI cli = new BCLI();
        }
    }

    private static void createAndShowGUI() {
        BModel model = new BModel();
        BController controller = new BController(model);
        BView view = new BView(model, controller);
    }
    
}
