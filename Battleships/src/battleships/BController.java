/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleships;

/**
 *
 * @author Ben9H
 */
class BController {
    
    private BModel model;
    private BView view;
    
    public BController(BModel model) {
        this.model = model;
    }
    
    public void setView(BView view) {
        this.view = view;
    }
    
    public void change() {
        model.change();
    }
    
    public void init() {
        model.init();
    }

    void handleCellClick(int x, int y) {
        view.updateCell(x,y);
    }
    
}
