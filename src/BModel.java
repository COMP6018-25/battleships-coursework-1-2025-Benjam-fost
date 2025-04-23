import java.io.File;
import java.util.Observable;

/**
 * A composite, standalone Model which stores and manages the game state.
 * @author Ben
 */
class BModel extends Observable{
    // Holds all ship and cell states plus grid-specific functionality
    private final BGrid grid;
    private int tries;
    private int shipsSunk;

    public BModel() {
        grid = new BGrid();
        tries = 0;
        shipsSunk = 0;
    }

    // Data transfer objects
    public record CellUpdate(int x, int y, boolean isHit, boolean isShipSunk) {}
    public record GameEndUpdate(int tries) {}

    public BGrid getGrid() { return grid; }

    public int getShipsSunk() { return shipsSunk; }

    public int getTries() { return tries; }

    // TODO load functionality
    private void load(File file){
        return;
    }
    // TODO save functionality
    private void save(File file){
        return;
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
