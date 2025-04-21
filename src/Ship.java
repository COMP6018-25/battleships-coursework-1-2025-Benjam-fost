public class Ship {
    private final int size;
    private boolean isSunk;
    private int sternCell;

    public Ship(int size, int sternCell) {
        this.size = size;
        isSunk = false;
        //sternCell = (sternCell.x, sternCell.y);
    }

    public int getSize() {
        return size;
    }

    public boolean isSunk() {
        return isSunk;
    }
}
