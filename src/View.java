import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * A View connected to a controller, observing a Model. Generates the GUI, an interactable grid of buttons.
 * @author Ben
 */
public class View implements Observer{
    
    private static final Dimension PANEL_SIZE = new Dimension (500,500);
    private final int GRID_SIZE = 11;
    
    // Holds references to the button cells
    // Accounts for labels by being sized against the playable area, GRID_SIZE - 1
    private JButton[][] cellButtons;
    private final Controller controller;

    /**
     * Creates a new View connected to a controller, observing a Model. Generates the GUI, a grid of interactable cell buttons.
     * @param model The model to be observed by the controller. Must be a Model instance.
     * @param controller The controller to be notified of cell clicks. Must be a Controller instance.
     */
    public View(Model model, Controller controller) {
        model.addObserver(this);
        
        // Main function for displaying the GUI on the screen
        createGUI();
        
        this.controller = controller;
    }

    /**
     * Creates the GUI and displays it on the screen.
     */
    private void createGUI(){
        JFrame frame = new JFrame("BATTLESHIPS");
        // Halts the program when the JFrame is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Creates the grid layout
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        // Creates the content of the panel
        contentPane.add(createGridPanel());
        // handles sizing
        frame.pack();
        frame.setResizable(false);
        // Needed to show the JFrame on the screen
        frame.setVisible(true);
        // Displays the GUI at the front
        frame.toFront();
        frame.requestFocus();
        frame.setAlwaysOnTop(true);
    }

    /**
     * Creates the grid and cells of the GUI.
     * @return A JPanel containing the grid and cells.
     */
    private JPanel createGridPanel(){
        JPanel panel = new JPanel();
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
        for (int y = 0; y < GRID_SIZE - 1; y++) {
            // Adds the number label
            panel.add(new JLabel(String.valueOf(y + 1)));
            // Adds the button cells to the array
            for (int x = 0; x < GRID_SIZE - 1; x++) {
                JButton cellButton = createCellButton(x, y);
                cellButtons[x][y] = cellButton;
                panel.add(cellButton);
            }
        }
        // Sets the size of the content pane as this.pane will be the only child
        panel.setPreferredSize(PANEL_SIZE);
        return panel;
    }

    /**
     * Creates a cell button with an action listener linked to the controller.
     * @param x The x coordinate of the cell to be created.
     * @param y The y coordinate of the cell to be created.
     * @return A JButton with an action listener linked to the controller.
     */
    private JButton createCellButton(int x, int y){
        JButton button = new JButton("");
        button.addActionListener((ActionEvent e) -> { controller.handleCellClick(x,y); });
        
        return button;
    }

    /**
     * Updates the GUI when the model changes.
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers} method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Model.CellUpdate update) {
            updateCell(update.x(), update.y(), update.isHit());
            if (update.isShipSunk()) {
                System.out.println("Ship sunk!");
            }
        } else if (arg instanceof Model.GameEndUpdate(int tries)) {
            System.out.println("Game end!\n| Tries: " + tries);
        } else {
            System.out.println("ERROR | Unexpected arg: " + arg);
        }
    }

    /**
     * Updates a single cell on the grid.
     * @param x The x coordinate of the cell to be updated.
     * @param y The y coordinate of the cell to be updated.
     * @param isHit Whether the cell has been hit or not.
     */
    private void updateCell(int x, int y, boolean isHit){
        if (isHit) {
            cellButtons[x][y].setText("H");
        } else {
            cellButtons[x][y].setText("M");
        }
    }
}
