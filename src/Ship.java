public class Ship {
    private final int size;
    private int hits;

    public Ship(int size) {
        this.size = size;
        hits = 0;
    }

    // Increments hits and sends updates when a ship sinks; it is only called when a new cell is struck.
    public void hit() {
        hits++;
    }

    public boolean isSunk() {
        return hits >= size;
    }

    public int getSize() {
        return size;
    }
}
