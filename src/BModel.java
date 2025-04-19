import java.io.File;
import java.util.Observable;

/**
 *
 * @author Ben
 */


class BModel extends Observable{
    // Holds all ship and cell states plus grid-specific functionality
    private BGrid grid;
    private int tries;

    public BModel() {
        grid = new BGrid();
        tries = 0;
        init();
    }

    public record CellUpdate(int x, int y, boolean isHit, boolean isShipSunk) {}

    public record GameEndUpdate(int tries) {}

    public BGrid getGrid() { return grid; }

    // TODO add ship placement + observer logic
    public void init(){

        return;
    }

    // TODO load functionality
    private void load(File file){
        return;
    }
    // TODO save functionality
    private void save(File file){
        return;
    }

    public boolean attack(int x, int y) {
        tries++;
        boolean hit = grid.attackCell(x,y);
        setChanged();
        notifyObservers(new CellUpdate(x,y,hit,false));

        // TODO implement ships + game over
        if (false) {
            setChanged();
            notifyObservers(new GameEndUpdate(tries));
        }
        return hit;
    }
}
