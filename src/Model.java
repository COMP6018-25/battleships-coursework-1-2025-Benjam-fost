import java.io.*;
import java.util.Observable;

/**
 * A composite Model which stores and manages the game state.
 * @author Ben
 */
public class Model extends Observable{
    // Holds all ship and cell states plus grid-specific functionality
    private Grid grid;
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
    public record GameStartUpdate() {}
    public record GameLoadUpdate(boolean success) {}
    public record CellUpdate(int x, int y, boolean isHit, boolean isShipSunk) {}
    public record GameEndUpdate(int tries) {}

    public Grid getGrid() { return grid; }

    public int getShipsSunk() { return shipsSunk; }

    public int getTries() { return tries; }

    public void reset() {
        grid = new Grid(true);
        shipsSunk = 0;
        tries = 0;
        setChanged();
        notifyObservers(new GameStartUpdate());
    }

    /**
     * Loads a grid of ships from a CSV file.
     * @param file A CSV file containing ship data.
     * @return Whether the ships were successfully loaded or not.
     */
    protected boolean loadGrid(File file) {
        boolean loaded = grid.loadShips(file);
        setChanged();
        notifyObservers(new GameLoadUpdate(loaded));
        return loaded;
    }

    /**
     * Attacks a cell in the grid.
     * @param x The x coordinate of the cell to be attacked.
     * @param y The y coordinate of the cell to be attacked.
     */
    public void attack(int x, int y) {
        assert x >= 0 && x < grid.getSize() : "x index out of bounds";
        assert y >= 0 && y < grid.getSize() : "y index out of bounds";
        // Invariant
        assert shipsSunk <= 5 : "More ships sunk than expected!";
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
