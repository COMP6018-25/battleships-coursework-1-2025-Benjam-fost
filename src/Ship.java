public class Ship {
    private final int size;
    private int hits;
    private int sternCell;

    public Ship(int size, int sternCell) {
        this.size = size;
        hits = 0;
        //sternCell = (sternCell.x, sternCell.y);
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
