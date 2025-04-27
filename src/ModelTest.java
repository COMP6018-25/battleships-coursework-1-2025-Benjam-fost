import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;

import java.io.File;


class ModelTest {
    private Model model;
    private File validFile;
    private File invalidFile;

    @BeforeEach
    void setUp() {
        validFile = new File("saves/valid_ships.csv");
        invalidFile = new File("saves/invalid_ships.csv");
    }

    @Test
    @DisplayName("Tests Reset clears state")
    void testReset() {
        model = new Model();
        model.attack(0, 0);
        assertTrue(model.getTries() > 0);
        model.reset();
        Grid grid = model.getGrid();
        // Check if the model has reset
        assertEquals(0, model.getTries(), "Tries should be 0 after reset" );
        assertEquals(0, model.getShipsSunk(), "Ships sunk should be 0 after reset" );
        assertFalse(grid.isCellHit(0, 0));
        assertFalse(grid.isShipSunkAt(0, 0));
    }

    @Test
    @DisplayName("Loading an invalid file should return false and ensure the grid is empty before loading, allowing a valid file to be read")
    void testInvalidFileLoad() {
        model = new Model();
        boolean loaded = model.loadGrid(invalidFile);
        assertFalse(loaded, "Invalid file should have failed to be loaded");
        loaded = model.loadGrid(validFile);
        assertTrue(loaded, "Valid file should have loaded successfully despite partial load from invalid file prior");
        assertEquals(0, model.getShipsSunk(), "Ships sunk should always be 0 as the game has not started yet" );
        assertEquals(0, model.getTries(), "Tries should always be 0 after invalid file load, as the game has not started yet" );
    }

    @Test
    @DisplayName("A valid file should load and ships should sunk if struck as many times as their size, on new cells")
    void testValidFileLoadAndShipSinking() {
        model = new Model();
        boolean loaded = model.loadGrid(validFile);
        assertTrue(loaded, "Valid file should have loaded successfully");
        assertEquals(0, model.getShipsSunk(), "Ships sunk should always be 0 as the game has just started" );
        assertEquals(0, model.getTries(), "Tries should always be 0 after valid file load, as the game has just started" );

        for (int x = 0; x < 5; x++) {
            model.attack(x, 0);
        }
        assertEquals(5, model.getTries(), "The correct number of tries should have been recorded");
        assertEquals(1, model.getShipsSunk(), "The carrier should have been sunk");
        assertTrue(model.getGrid().isShipSunkAt(0, 0));
        assertEquals(1, model.getShipsSunk(), "The sinking of the battleship should have been recorded");
        // Attacking an empty cell should not increment state variables
        model.attack(9,9);
        assertEquals(6, model.getTries(), "The correct number of tries should have been recorded");
    }
}