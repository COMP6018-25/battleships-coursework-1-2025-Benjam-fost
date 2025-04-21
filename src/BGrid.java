/**
 * Stores game state and functionality related to the playable grid. Part of a composite BModel. Co-manages game state with Cells.
 * @author Ben
 */
class BGrid {
    /*
    Defines the size of the play area
    1 column and row is reserved for column headers
    */
    private final int GRID_SIZE = 10;
    // Holds the virtual state of each cell
    private final Cell[][] cells;

    public BGrid() {
        cells = new Cell[GRID_SIZE][GRID_SIZE];
        init();
    }

    private void init(){
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                cells[x][y] = new Cell();
            }
        }
    }

    public boolean attackCell(int x, int y) {
        return cells[x][y].attack();
    }

    public int getSize() { return GRID_SIZE; }

    public Boolean isShipSunkAt(int x, int y) {
        Cell cell = cells[x][y];
        if (cell.hasShip()) {
            return cell.getShip().isSunk();
        }
        // Ship is not present or did not sink
        return false;
    }

    public Boolean isCellHit(int x, int y) {
        return cells[x][y].isHit();
    }
}
