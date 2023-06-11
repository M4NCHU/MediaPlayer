import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.*;
import java.util.List;

public class ButtonRenderer extends JButton implements TableCellRenderer {
    ImageIcon ic;
    String type = "";


    public ButtonRenderer(ImageIcon icon, String type) {
        setOpaque(true);
        ic = icon;
        if (type != null) {
            this.type = type;
        }

    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if (type == "heart") {
            Object songLabelValue = table.getModel().getValueAt(row, 0);
            Object songPath = table.getModel().getValueAt(row, 1);
            String playlistFilePath = Globals.playlistFolder + "Ulubione.dat";
            SongList playlist = new SongList(playlistFilePath);

            boolean isFavorite = playlist.isSongInPlaylist(songPath.toString(), "Ulubione");

            IconGenerator iconGenerator = new IconGenerator(15, 15);
            ImageIcon heartIconFilled = iconGenerator.createIcon(Globals.imageFolder + "heart.png");
            ImageIcon heartIconEmpty = iconGenerator.createIcon(Globals.imageFolder + "heartEmpty.png");

            if (isFavorite) {
                setIcon(heartIconFilled); // Set full heart icon
            } else {
                setIcon(heartIconEmpty); // Set empty heart icon
            }
        } else {
            setIcon(ic);
        }


        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        return this;
    }

}