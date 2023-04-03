import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {

    private JPanel leftPanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JSplitPane splitPane;

    private int sidebarButtonsHeight = 20;
    private String sidebarBtnColor = "#2C3E50";
    private String sidebarBtnForeColor = "#ffffff";

    public SidebarPanel() {
        setLayout(new BorderLayout());

        leftPanel = new JPanel();
        leftPanel.setBackground(Color.decode("#7F8C8D"));
        leftPanel.setPreferredSize(new Dimension(200, 0));

        leftPanel.add(new JLabel("Prawy pasek"));

        button1 = new JButton("Element 1");
        button1.setMaximumSize(new Dimension(Integer.MAX_VALUE, sidebarButtonsHeight));
        button1.setBackground(Color.decode(sidebarBtnColor));
        button1.setForeground(Color.decode(sidebarBtnForeColor));
        leftPanel.add(button1);

        button2 = new JButton("Element 2");
        button2.setMaximumSize(new Dimension(Integer.MAX_VALUE, sidebarButtonsHeight));
        button2.setBackground(Color.decode(sidebarBtnColor));
        button2.setForeground(Color.decode(sidebarBtnForeColor));
        leftPanel.add(button2);

        button3 = new JButton("Element 3");
        button3.setMaximumSize(new Dimension(Integer.MAX_VALUE, sidebarButtonsHeight));
        button3.setBackground(Color.decode(sidebarBtnColor));
        button3.setForeground(Color.decode(sidebarBtnForeColor));
        leftPanel.add(button3);

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        add(leftPanel, BorderLayout.WEST);
    }
}
