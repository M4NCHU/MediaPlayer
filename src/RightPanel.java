import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {


    public RightPanel() {
        setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.decode("#7F8C8D"));

        // Elementy panelu bocznego po prawej stronie
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        rightPanel.add(new JLabel("Prawy pasek"));
        rightPanel.add(new JButton("Element 1"));
        rightPanel.add(new JButton("Element 2"));
        rightPanel.add(new JButton("Element 3"));
        add(rightPanel, BorderLayout.CENTER);

    }
}
