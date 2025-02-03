/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleships;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;

/**
 *
 * @author Ben9H
 */
public class BView implements Observer{
    
    private static final Dimension PANEL_SIZE = new Dimension (200,200);
    
    private final BModel model;
    private final BController controller;
    private JFrame frame;
    private Grid grid;
    
    
    public BView(BModel model, BController controller) {
        this.model = model;
        this.controller = controller;
        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
