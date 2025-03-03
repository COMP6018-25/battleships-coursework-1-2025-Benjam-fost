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
    private final int GRID_SIZE = 11;
    
    // Holds references to the button cells
    // Accounts for labels by being sized against the playable area, GRID_SIZE - 1
    private JButton[][] cellButtons;
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
        panel.setLayout(new GridLayout(GRID_SIZE,GRID_SIZE));
        cellButtons = new JButton[GRID_SIZE - 1][GRID_SIZE - 1];
        
        /*
        Below dynamically adds the grid content
        Used x (columns) and y (rows) to avoid confusion
        x = 0 and y = 0 at the top left-most cell
        */
        
        // Adds the empty cell at the top right
        panel.add(new JLabel("◿"));
        // Adds the column labels
        for (int x = 1; x < GRID_SIZE; x++){
            // Adds letter labels across the top of the grid
            panel.add(new JLabel(Character.toString('A' + x - 1)));
        }
        
        // Fills the rest of the grid
        for (int y = 0; y < GRID_SIZE - 1; y++){
            // Adds the number label
            panel.add(new JLabel(String.valueOf(y + 1)));
            // Adds the button cells to the array
            for (int x = 0; x < GRID_SIZE - 1; x++){
                JButton cellButton = createCellButton(x,y);
                cellButtons[x][y] = cellButton;
                panel.add(cellButton);
            } 
        }
        
        // Sets the size of the content pane as this.pane will be the only child
        panel.setPreferredSize(PANEL_SIZE);
    }
    
    // Factory method that creates a button for a grid cell, links to the controller
    private JButton createCellButton(int x, int y){
        JButton button = new JButton("");
        button.addActionListener((ActionEvent e) -> {controller.handleCellClick(x,y);});
        
        return button;
    }
    
    /**
     * @param
     *
     */
    @Override
    public void update(Observable o, Object arg) {
        System.out.println(cellButtons);
        System.out.println("UPDATE");
    }
    // TODO: INTEGRATE with update
    public void updateCell(int x, int y){
        System.out.println("updateCell (" + x + ", " + y + "): to H" );
        cellButtons[x][y].setText("H");
    }
}
