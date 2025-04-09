import java.util.Observable;

/**
 *
 * @author Ben
 */
class BModel extends Observable{
    // Holds all ship and cell states plus grid-specific functionality
    private BGrid grid;

    public BModel() {
        grid = new BGrid();
        init();
    }

    public BGrid getGrid() { return grid; }

    // TODO add ship placement + observer logic
    public void init(){

        return;
    }

    public boolean attack(int x, int y) {
        boolean hit = grid.attackCell(x,y);
        notifyObservers();
        return hit;
    }
}
