import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Stores game state and functionality related to the playable grid. Part of a composite Model. Co-manages game state with Cells.
 * @author Ben
 */
class Grid {
    /*
    Defines the size of the play area
    1 column and row is reserved for column headers
    */
    private final int GRID_SIZE = 10;
    // Holds the virtual state of each cell
    private final Cell[][] cells;
    private final ArrayList<Ship> ships = new ArrayList<>();

    public Grid(boolean randomShips) {
        cells = new Cell[GRID_SIZE][GRID_SIZE];
        initGrid();
        if (randomShips) {
            placeShips();
        }
    }

    public Grid() {
        this(true);
    }

    /**
     * Loads ships from a CSV file.
     * @param file A CSV file containing ship data.
     * @return Whether the ships were successfully loaded or not.
     */
    protected boolean loadShips(File file) {
        assert file.exists() : "File does not exist!";

        ships.clear();
        initGrid();
        List<Integer> sizes = new ArrayList<>();
        List<Integer> expected = Arrays.asList(5, 4, 3, 2, 2);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Skips comments or empty lines
                if (line.isEmpty() || line.startsWith("#")) continue;
                // Gets data from each line and checks if that line is of the required length
                String[] data = line.split(",");
                if (data.length != 4) {
                    System.out.printf("| | Invalid file format! | |\nExpected 4 columns but got %d\n", data.length);
                    return false;
                }

                // Parsing data
                int size = Integer.parseInt(data[0]);
                sizes.add(size);
                int x = Integer.parseInt(data[1]);
                int y = Integer.parseInt(data[2]);
                boolean horizontal = data[3].equals("H");
                Ship ship = new Ship(size);
                ships.add(ship);

                if (!placeShip(x, y, ship, horizontal)) {
                    System.out.printf("| | Error placing ship! | |\nSize: %d, X: %d, Y: %d, Horizontal: %b\n", size, x, y, horizontal);
                    return false;
                }
            }
            Collections.sort(sizes);
            Collections.sort(expected);

            if (!sizes.equals(expected)) {
                System.out.println("| | Invalid fleet makeup! | |\nExpected 5 ships of sizes 5, 4, 3, 2, 2");
                return false;
            }
        } catch (FileNotFoundException e) {
            System.out.println("| | File not found! | |");
            return false;
        } catch (IOException e) {
            System.out.println("| | File read error! | |");
            return false;
        }
        assert ships.size() == 5 : "| | Invalid fleet makeup | |\nExpected 5 ships but got " + ships.size() + "!";
        return true;
    }

    /**
     * Collates the current state of the grid into a string.
     * @return A string representation of the grid.
     */
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

    /**
     * Initialises the grid by creating all cells and placing them in an array.
     */
    private void initGrid(){
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                cells[x][y] = new Cell();
            }
        }
    }

    /**
     * Returns a random index within the bounds of the grid.
     * @return An integer between 0 and GRID_SIZE - 1, inclusive.
     */
    private int randomIndex() {
        return (int)(Math.random() * GRID_SIZE);
    }

    /**
     * Checks if a ship horizontally bisects the prospective ship.
     * @param x The x coordinate of the ship's start position.
     * @param y The y coordinate of the ship's start position.
     * @param size The size (type) of the ship.
     * @return Whether the ship does not bisect any cells in the grid or not.
     */
    private boolean noHorizontalBisections(int x, int y, int size) {
        assert x >= 0 && x < GRID_SIZE : "x index out of bounds";
        assert y >= 0 && y < GRID_SIZE : "y index out of bounds";
        assert size >= 2 && size <= 5 : "Invalid size: " + size + " for ship at (" + x + ", " + y + ")";

        for (int pointer = 0; pointer < size; pointer++) {
            int index = pointer + x;
            // If a ship bisects the chosen cells, re-select
            if (cells[index][y].hasShip()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a ship vertically bisects the prospective ship.
     * @param x The x coordinate of the ship's start position.
     * @param y The y coordinate of the ship's start position.
     * @param size The size (type) of the ship.
     * @return Whether the ship does not bisect any cells in the grid or not.
     */
    private boolean noVerticalBisections(int x, int y, int size) {
        assert x >= 0 && x < GRID_SIZE : "x index out of bounds";
        assert y >= 0 && y < GRID_SIZE : "y index out of bounds";
        assert size >= 2 && size <= 5 : "Invalid size: " + size + " for ship at (" + x + ", " + y + ")";

        for (int pointer = 0; pointer < size; pointer++) {
            int index = pointer + y;
            if (cells[x][index].hasShip()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a ship can be placed at a given position and orientation.
     * @param x The x coordinate of the ship's start position.
     * @param y The y coordinate of the ship's start position.
     * @param size The size (type) of the ship.
     * @param horizontal Whether the ship is placed horizontally or not.
     * @return Whether the placement is valid or not.
     */
    private boolean isValidPlacement(int x, int y, int size, boolean horizontal) {
        assert x >= 0 && x < GRID_SIZE : "x index out of bounds";
        assert y >= 0 && y < GRID_SIZE : "y index out of bounds";
        assert size >= 2 && size <= 5 : "Invalid size: " + size + " for ship at (" + x + ", " + y + ")";

        // omits pos >= 0 check as both randomX and size never go below 0
        if (horizontal) {
            // If the ship can't fit, placement is invalid
            if (!(x + size < GRID_SIZE)) {
                return false;
            } else {
                // If the placement does not bisect another ship, the placement is valid
                return noHorizontalBisections(x, y, size);
            }
        } else {
            // Identical logic but for the other axis
            if (!(y + size < GRID_SIZE)) {
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
        assert ships.isEmpty() : "Ships array not empty!";
        // Creates and adds each Ship to Ships and grid cells
        int[] sizes = {5, 4, 3, 2, 2};
        for (int size : sizes) {
            Ship ship = new Ship(size);
            ships.add(ship);
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
        assert ships.size() == 5 : "Ships array not filled correctly!";
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
        assert x >= 0 && x < GRID_SIZE : "x index out of bounds";
        assert y >= 0 && y < GRID_SIZE : "y index out of bounds";
        assert ship != null : "Ship is null!";
        assert ships.contains(ship) : "Ship is not in the ships array!";
        assert ship.getSize() > 0 : "Invalid ship size: " + ship.getSize() + " for ship at (" + x + ", " + y + ")";

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
        assert cells[x][y].hasShip() : "Ship was not placed at (" + x + ", " + y + ")";
        return true;
    }

    /**
     * Attacks a cell in the grid.
     * @param x The x coordinate of the cell to be attacked.
     * @param y The y coordinate of the cell to be attacked.
     * @return Whether a ship was hit or not.
     */
    public boolean attackCell(int x, int y) {
        assert x >= 0 && x < GRID_SIZE : "x index out of bounds";
        assert y >= 0 && y < GRID_SIZE : "y index out of bounds";
        assert cells[x][y] != null : "Cell at (" + x + ", " + y + ") is null!";

        return cells[x][y].attack();
    }

    public int getSize() { return GRID_SIZE; }

    /**
     * Checks if a ship has been sunk at a given position.
     * @param x The x coordinate of the cell to be checked.
     * @param y The y coordinate of the cell to be checked.
     * @return Whether the ship has been sunk or not.
     */
    public Boolean isShipSunkAt(int x, int y) {
        assert x >= 0 && x < GRID_SIZE : "x index out of bounds";
        assert y >= 0 && y < GRID_SIZE : "y index out of bounds";
        assert cells[x][y] != null : "Cell at (" + x + ", " + y + ") is null!";

        Cell cell = cells[x][y];
        if (cell.hasShip()) {
            return cell.getShip().isSunk();
        }
        assert cell.getShip() == null : "Cell at (" + x + ", " + y + ") has a ship but the ship is null!";
        // A ship is not present or did not sink
        return false;
    }

    public Boolean isCellHit(int x, int y) {
        assert x >= 0 && x < GRID_SIZE : "x index out of bounds";
        assert y >= 0 && y < GRID_SIZE : "y index out of bounds";
        assert cells[x][y] != null : "Cell at (" + x + ", " + y + ") is null!";

        return cells[x][y].isHit();
    }
}
