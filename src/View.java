import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * A View connected to a controller, observing a Model. Generates the GUI, an interactable grid of buttons.
 * @author Ben
 */
public class View extends JFrame implements Observer{
    private final Controller controller;
    private final CardLayout cards;
    private final JPanel cardPanel;
    private final GUI gui;
    private final Menu menu;

    /**
     * Creates a new View connected to a controller, observing a Model. Generates the GUI, a grid of interactable cell buttons.
     * @param model The model to be observed by the controller. Must be a Model instance.
     * @param controller The controller to be notified of cell clicks. Must be a Controller instance.
     */
    public View(Model model, Controller controller) {
        super("Battleships");
        this.controller = controller;
        cards = new CardLayout();
        // This panel holds the main menu and game view
        cardPanel = new JPanel(cards);
        // Holds the visual game grid
        gui = new GUI(controller);
        // Holds the main menu functionality
        menu = new Menu(controller);
        model.addObserver(this);
        initGUI();
    }

    /**
     * Creates the GUI and displays it on the screen.
     */
    private void initGUI(){
        // Halts the program when the JFrame is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardPanel.add(menu, "Menu");
        cardPanel.add(gui, "Game");
        setContentPane(cardPanel);

        // Creates the grid layout
        /*Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        // Creates the content of the panel
        contentPane.add(createGridPanel());
        */
        // handles sizing
        this.pack();
        this.setResizable(false);
        // Needed to show the JFrame on the screen
        this.setVisible(true);
        // Displays the GUI at the front
        this.toFront();
        this.requestFocus();
        this.setAlwaysOnTop(true);

        cards.show(cardPanel, "Menu");
    }

    /**
     * Updates the GUI when the model changes.
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers} method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Model.CellUpdate update) {
            gui.updateCell(update.x(), update.y(), update.isHit());
            if (update.isShipSunk()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ship sunk!",
                        "Attack result",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else if (arg instanceof Model.GameEndUpdate(int tries)) {
            JOptionPane.showMessageDialog(
                    this,
                    "You sank all the ships after " + tries + " tries!",
                    "You win",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else if (arg instanceof Model.GameLoadUpdate(boolean success)) {
            if (success) {
                cards.show(getContentPane(), "Game");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Error loading file",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else if (arg instanceof Model.GameStartUpdate) {
            cards.show(getContentPane(), "Game");
        } else {
            System.out.println("ERROR | Unexpected arg: " + arg);
        }
    }
}
