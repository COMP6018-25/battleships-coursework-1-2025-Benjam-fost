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
    private final Ship[] ships;

    public BGrid() {
        cells = new Cell[GRID_SIZE][GRID_SIZE];
        ships = new Ship[]{new Ship(5), new Ship(4), new Ship(3), new Ship(2), new Ship(2)};
        init();
    }

    private void init(){
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                cells[x][y] = new Cell();
            }
        }
        placeShips();
    }

    /**
     * Places ships at valid, random positions and orientations.
     */
    private void placeShips() {
        for (Ship ship : ships) {
            int size = ship.getSize();
            boolean horizontal = Math.random() > 0.5;
            int randomX = (int)(Math.random() * GRID_SIZE);
            int randomY = (int)(Math.random() * GRID_SIZE);
            boolean valid = false;

            // Finds a valid, random position at the set orientation
            while (!valid) {
                // omits pos >= 0 check as both randomX and size never go below 0
                if (horizontal) {
                    // If the ship can't fit, re-select
                    if (!((randomX + size < GRID_SIZE) && (randomY < GRID_SIZE))) {
                        randomX = (int)(Math.random() * GRID_SIZE);
                        randomY = (int)(Math.random() * GRID_SIZE);
                    } else {
                        valid = true;
                        // Check if the placement would bisect another ship
                        for (int pointer = 0; pointer < size; pointer++) {
                            int index = pointer + randomX;
                            if (cells[index][randomY].hasShip()) {
                                valid = false;
                            }
                        }
                    }
                } else {
                    // Identical logic but for the other axis
                    if (!((randomX < GRID_SIZE) && (randomY + size < GRID_SIZE))) {
                        randomX = (int)(Math.random() * GRID_SIZE);
                        randomY = (int)(Math.random() * GRID_SIZE);
                    } else {
                        valid = true;
                        for (int pointer = 0; pointer < size; pointer++) {
                            int index = pointer + randomY;
                            if (cells[randomX][index].hasShip()) {
                                valid = false;
                            }
                        }
                    }
                }
            }
            // A valid, random position has been found, now place the ship
            if (horizontal) {
                for (int pointer = 0; pointer < size; pointer++) {
                    cells[randomX + pointer][randomY].setShip(ship);
                }
            } else {
                for (int pointer = 0; pointer < size; pointer++) {
                    cells[randomX][randomY + pointer].setShip(ship);
                }
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
