/**
 * Stores an individual cell state, paired with a Ship object reference, co-managing game state with a Ship array in Grid.java.
 * @author Ben
 */
public class Cell {
    private boolean hit;
    private Ship ship;

    public Cell(){
        hit = false;
        ship = null;
    }

    public boolean isHit(){
        return hit;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean hasShip() {
        return ship != null;
    }

    public boolean attack() {
        // If there is a ship, and it has not been hit on this cell
        if (hasShip() && !hit) {
            ship.hit();
            hit = true;
            return true;
        }

        hit = true;
        return false;
    }
}
