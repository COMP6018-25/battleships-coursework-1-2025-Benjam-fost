import javax.swing.*;
import java.io.File;

public class Menu extends JPanel {

    public Menu(Controller controller) {
        super();
        initMenu(controller);
    }

    private void initMenu(Controller controller) {
        JButton randomButton = new JButton("Random game");
        JButton customButton = new JButton("Custom game");
        JButton exitButton = new JButton("Exit");

        randomButton.addActionListener(e -> controller.randomGameStart());

        customButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(new File("."));
            int result = fc.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                controller.loadGame(fc.getSelectedFile());
            }
        });

        exitButton.addActionListener((e) -> {
            System.exit(0);
        });

        this.add(randomButton);
        this.add(customButton);
        this.add(exitButton);
    }
}
