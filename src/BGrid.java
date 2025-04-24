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

    public BGrid(boolean randomShips) {
        cells = new Cell[GRID_SIZE][GRID_SIZE];
        ships = new Ship[]{new Ship(5), new Ship(4), new Ship(3), new Ship(2), new Ship(2)};
        initGrid();
        if (randomShips) {
            placeShips();
        }
    }

    public BGrid() {
        this(true);
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder("\n");
        // Displays the column headers
        output.append("◪  ");
        for (int col = 0; col < GRID_SIZE; col++) {
            output.append((char)('A' + col)).append(' ');
        }
        output.append("\n");

        for (int y = 0; y < GRID_SIZE; y++) {
            // Display the row numbers
            String rowNumber = String.valueOf(y + 1);
            output.append(rowNumber).append(rowNumber.length() == 1 ? "  " : " ");
            // Display the grid cells, with hits and misses
            for (int x = 0; x < GRID_SIZE; x++) {
                Cell cell = cells[x][y];
                if (cell.isHit()) {
                    if (cell.hasShip()){
                        output.append("H ");
                    } else {
                        output.append("M ");
                    }
                } else {
                    output.append("□ ");
                }
            }
            output.append("\n");
        }
        return output.toString();
    }

    private void initGrid(){
        System.out.println("Creating the board...");
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                cells[x][y] = new Cell();
            }
        }
    }

    private int randomIndex() {
        return (int)(Math.random() * GRID_SIZE);
    }

    private boolean noHorizontalBisections(int x, int y, int size) {
        for (int pointer = 0; pointer < size; pointer++) {
            int index = pointer + x;
            // If a ship bisects the chosen cells, re-select
            if (cells[index][y].hasShip()) {
                return false;
            }
        }
        return true;
    }

    private boolean noVerticalBisections(int x, int y, int size) {
        for (int pointer = 0; pointer < size; pointer++) {
            int index = pointer + y;
            if (cells[x][index].hasShip()) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPlacement(int x, int y, int size, boolean horizontal) {
        // omits pos >= 0 check as both randomX and size never go below 0
        if (horizontal) {
            // If the ship can't fit, placement is invalid
            if (!((x + size < GRID_SIZE) && (y < GRID_SIZE))) {
                return false;
            } else {
                // If the placement does not bisect another ship, the placement is valid
                return noHorizontalBisections(x, y, size);
            }
        } else {
            // Identical logic but for the other axis
            if (!((x < GRID_SIZE) && (y + size < GRID_SIZE))) {
                return false;
            } else {
                return noVerticalBisections(x, y, size);
            }
        }
    }

    /**
     * Places ships at valid, random positions and orientations.
     */
    private void placeShips() {
        System.out.print("Placing ships");
        for (Ship ship : ships) {
            System.out.print('.');
            boolean horizontal = Math.random() > 0.5;
            int randomX = randomIndex();
            int randomY = randomIndex();

            // Finds a valid, random position at the set orientation and places the ship
            for (;;) {
                if (placeShip(randomX, randomY, ship, horizontal)) {
                    break;
                } else {
                    randomX = randomIndex();
                    randomY = randomIndex();
                }
            }
        }
        System.out.println();
    }

    /**
     * First checks if the ship can be placed in a position, then places the ship.
     * @param x The x start position of the ship
     * @param y The y start position of the ship
     * @param ship The ship to place
     * @param horizontal Represents the ship's orientation, vertical if not horizontal
     * @return The success or failure of the operation
     */
    public boolean placeShip(int x, int y, Ship ship, boolean horizontal) {
        int size = ship.getSize();
        // Checks if placement is valid before continuing
        if (!isValidPlacement(x, y, size, horizontal)) return false;

        if (horizontal) {
            for (int pointer = 0; pointer < size; pointer++) {
                cells[x + pointer][y].setShip(ship);
            }
        } else {
            for (int pointer = 0; pointer < size; pointer++) {
                cells[x][y + pointer].setShip(ship);
            }
        }
        return true;
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
        // The ship is not present or did not sink
        return false;
    }

    public Boolean isCellHit(int x, int y) {
        return cells[x][y].isHit();
    }
}
