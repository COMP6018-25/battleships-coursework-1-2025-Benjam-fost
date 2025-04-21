/**
 *
 * @author Ben
 */
class BController {
    
    private final BModel model;

    public BController(BModel model) {
        this.model = model;
    }

    public void handleCellClick(int x, int y) {

        if (!model.getGrid().isCellHit(x, y)) {
            model.attack(x, y);
        }
    }
    
}
