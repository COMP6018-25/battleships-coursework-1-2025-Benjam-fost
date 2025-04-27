import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GUI extends JPanel{
    private static final Dimension PANEL_SIZE = new Dimension (500,500);
    // Holds references to the button cells
    private final JButton[][] cellButtons;
    // Accounts for labels by being sized against the playable area, GRID_SIZE - 1
    private final int GRID_SIZE;


    public GUI(Controller controller, int size) {
        super();
        GRID_SIZE = size + 1;
        cellButtons = new JButton[GRID_SIZE - 1][GRID_SIZE - 1];
        initBoard(controller);
    }

    /**
     * Creates the grid and cells of the GUI.
     * @return A JPanel containing the grid and cells.
     */
    private void initBoard(Controller controller){
        this.setLayout(new GridLayout(GRID_SIZE,GRID_SIZE));

        /*
        Below dynamically adds the grid content
        Used x (columns) and y (rows) to avoid confusion
        x = 0 and y = 0 at the top left-most cell
        */

        // Adds the empty cell at the top right
        this.add(new JLabel("◿"));
        // Adds the column labels
        for (int x = 1; x < GRID_SIZE; x++){
            // Adds letter labels across the top of the grid
            this.add(new JLabel(Character.toString('A' + x - 1)));
        }

        // Fills the rest of the grid
        for (int y = 0; y < GRID_SIZE - 1; y++) {
            // Adds the number label
            this.add(new JLabel(String.valueOf(y + 1)));
            // Adds the button cells to the array
            for (int x = 0; x < GRID_SIZE - 1; x++) {
                JButton cellButton = createCellButton(controller, x, y);
                cellButtons[x][y] = cellButton;
                this.add(cellButton);
            }
        }
        // Sets the size of the content pane as this.pane will be the only child
        this.setPreferredSize(PANEL_SIZE);
    }

    /**
     * Creates a cell button with an action listener linked to the controller.
     * @param x The x coordinate of the cell to be created.
     * @param y The y coordinate of the cell to be created.
     * @return A JButton with an action listener linked to the controller.
     */
    private JButton createCellButton(Controller controller, int x, int y){
        JButton button = new JButton("");
        button.addActionListener((ActionEvent e) -> { controller.handleCellClick(x,y); });

        return button;
    }

    /**
     * Updates a single cell on the grid.
     * @param x The x coordinate of the cell to be updated.
     * @param y The y coordinate of the cell to be updated.
     * @param isHit Whether the cell has been hit or not.
     */
    public void updateCell(int x, int y, boolean isHit){
        JButton cellButton = cellButtons[x][y];
        if (isHit) {
            cellButton.setText("H");
            cellButton.setBackground(Color.RED);
        } else {
            cellButton.setText("M");
        }
        cellButton.setEnabled(false);
    }

    public void disableCells() {
        for (int x = 0; x < GRID_SIZE - 1; x++) {
            for (int y = 0; y < GRID_SIZE - 1; y++) {
                cellButtons[x][y].setEnabled(false);
            }
        }
    }
}
