import java.io.*;
import java.util.Observable;

/**
 * A composite Model which stores and manages the game state.
 * @author Ben
 */
class Model extends Observable{
    // Holds all ship and cell states plus grid-specific functionality
    private final Grid grid;
    private int tries;
    private int shipsSunk;

    /**
     * Creates a new Model with a grid of either randomly generated or pre-set ships.
     * @param randomShips Whether the ships should be randomly placed or not.
     */
    public Model(boolean randomShips) {
        grid = new Grid(randomShips);
        tries = 0;
        shipsSunk = 0;
    }

    /**
     * A default constructor.Creates a randomly generated grid of ships.
     */
    public Model() {
        this(true);
    }

    // Data transfer objects
    public record GameLoad(boolean success) {}
    public record CellUpdate(int x, int y, boolean isHit, boolean isShipSunk) {}
    public record GameEndUpdate(int tries) {}

    public Grid getGrid() { return grid; }

    public int getShipsSunk() { return shipsSunk; }

    public int getTries() { return tries; }

    /**
     * Loads a grid of ships from a CSV file.
     * @param file A CSV file containing ship data.
     * @return Whether the ships were successfully loaded or not.
     */
    protected boolean loadGrid(File file) {
        boolean loaded = grid.loadShips(file);
        setChanged();
        notifyObservers(new GameLoad(loaded));
        return loaded;
    }

    /**
     * Attacks a cell in the grid.
     * @param x The x coordinate of the cell to be attacked.
     * @param y The y coordinate of the cell to be attacked.
     */
    public void attack(int x, int y) {
        // Checks if the cell has been hit already
        if (grid.isCellHit(x, y)) {
            System.out.println("You have already attacked this cell!");
            return;
        }
        tries++;
        boolean hit = grid.attackCell(x,y);
        boolean shipSunk = grid.isShipSunkAt(x, y);
        if (shipSunk) {
            shipsSunk++;
        }
        setChanged();
        notifyObservers(new CellUpdate(x, y, hit, shipSunk));

        if (shipsSunk == 5) {
            setChanged();
            notifyObservers(new GameEndUpdate(tries));
        }
    }
}
