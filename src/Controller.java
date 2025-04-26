/**
 * A controller for the View
 * @author Ben
 */
class Controller {
    
    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    /**
     * Handles a cell click by updating the model, which notifies the view of the change.
     * @param x The x coordinate of the cell that was clicked.
     * @param y The y coordinate of the cell that was clicked.
     */
    public void handleCellClick(int x, int y) {
        if (!model.getGrid().isCellHit(x, y)) {
            model.attack(x, y);
        }
    }
    
}
