import java.io.*;
import java.util.Observable;

/**
 * A composite Model which stores and manages the game state.
 * @author Ben
 */
class BModel extends Observable{
    // Holds all ship and cell states plus grid-specific functionality
    private final BGrid grid;
    private int tries;
    private int shipsSunk;

    public BModel(boolean randomShips) {
        grid = new BGrid(randomShips);
        tries = 0;
        shipsSunk = 0;
    }

    public BModel() {
        this(true);
    }

    // Data transfer objects
    public record GameLoad(boolean success) {}
    public record CellUpdate(int x, int y, boolean isHit, boolean isShipSunk) {}
    public record GameEndUpdate(int tries) {}

    public BGrid getGrid() { return grid; }

    public int getShipsSunk() { return shipsSunk; }

    public int getTries() { return tries; }

    // TODO load functionality
    protected boolean loadBoard(File file) {
        boolean loaded = grid.loadShips(file);
        setChanged();
        notifyObservers(new GameLoad(loaded));
        return loaded;
    }

    public void attack(int x, int y) {
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
