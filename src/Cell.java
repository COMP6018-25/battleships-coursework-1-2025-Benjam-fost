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

    /**
     * Marks a Cell as hit. If the Cell has a Ship, and it has not been hit yet, the Ship is hit.
     * @return Whether the attack missed or hit a Ship.
     */
    public boolean attack() {
        if (hasShip() && !hit) {
            ship.hit();
            hit = true;
            return true;
        }
        hit = true;
        return false;
    }
}
