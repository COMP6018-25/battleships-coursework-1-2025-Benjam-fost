public class Cell {

    private boolean hit;

    public Cell(){
        hit = false;
    }

    public boolean isHit(){
        return hit;
    }

    public boolean attack() {
        if (hit){
            return false;
        }

        hit = true;

        return true;
    }
}
