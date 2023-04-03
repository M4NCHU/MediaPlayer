import javax.swing.*;
import java.awt.*;

public class IconGenerator {
    private final int toolbarIconsWidth;
    private final int toolbarIconsHeight;

    // Constructor
    public IconGenerator(int width, int height) {
        this.toolbarIconsWidth = width;
        this.toolbarIconsHeight = height;
    }

    // Create Icon Method
    public ImageIcon createIcon(String filePath) {
        ImageIcon icon = new ImageIcon(new ImageIcon(filePath).getImage()
                .getScaledInstance(toolbarIconsWidth, toolbarIconsHeight, Image.SCALE_SMOOTH));
        return icon;
    }
}
