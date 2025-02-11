/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleships;

import java.util.Observer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;

/**
 *
 * @author Ben9H
 */
public class BView implements Observer{
    
    private static final Dimension PANEL_SIZE = new Dimension (500,500);
    
    private final BModel model;
    private final BController controller;
    private JFrame frame;
    private JPanel panel;
    
    
    public BView(BModel model, BController controller) {
        this.model = model;
        model.addObserver(this);
        
        // Main function for displaying the GUI on the screen
        createGUI();
        
        this.controller = controller;
        controller.setView(this);
        update(model, null);
    }
    
    // Handles top level Swing code, calls another function for the minutiae
    public void createGUI(){
        frame = new JFrame("BATTLESHIPS");
        // Halts the program when the JFrame is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Creates the grid layout
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        // Creates the content of the panel
        createPanel();
        contentPane.add(panel);
        
        // handles sizing
        frame.pack();
        frame.setResizable(false);
        // Needed to show the JFrame on the screen
        frame.setVisible(true);
    }
    
    // Initialises the panel and adds content, to be added to the contentPanel of frame
    private void createPanel(){
        panel = new JPanel();
        panel.setLayout(new GridLayout(11,11));
        
        /*
        Below dynamically adds the grid content
        Used x (columns) and y (rows) to avoid confusion
        x = 0 and y = 0 at the top left-most cell
        */
        
        // Adds the row numbers
        for (int y = 0; y < 11; y++){
            // Adds the empty cell at the top right
            if (y == 0){
                panel.add(new JLabel("◿"));
            } else {
                // Uses the row iteration as the row number, converted into string format
                panel.add(new JLabel(String.valueOf(y)));
            }
        }
        
        // Fills the rest of the grid
        for (int x = 1; x < 11; x++){
            for (int y = 0; y < 11; y++){
                if (y == 0){
                    panel.add(new JLabel(Character.toString('A' + x - 1)));
                } else {
                    panel.add(createCellButton());
                }
                
            }
        }
        
        // Sets the size of the content pane as this.pane will be the only child
        panel.setPreferredSize(PANEL_SIZE);
    }
    
    // TODO
    // Factory method that creates a button for a grid cell, will be expanded with more logic
    private JButton createCellButton(){
        JButton button = new JButton("");
        button.addActionListener((ActionEvent e) -> {button.setText("H");});
        
        return button;
    }
    
    /**
     * @param
     *
     */
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("UPDATE");
    }
}
