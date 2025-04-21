public class Cell {

    private boolean hit;
    private boolean containsShip;

    public Cell(){
        hit = false;
        containsShip = false;
    }

    public boolean isHit(){
        return hit;
    }

    public boolean attack() {
        hit = true;

        return containsShip;
    }
}
