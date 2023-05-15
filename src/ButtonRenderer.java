import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
            boolean isFavorite = isSongInPlaylist(songLabelValue.toString());
            IconGenerator iconGenerator = new IconGenerator(15, 15);
            ImageIcon heartIconFilled = iconGenerator.createIcon("./resources/heart.png");
            ImageIcon heartIconEmpty = iconGenerator.createIcon("./resources/images/heartEmpty.png");

            if (isFavorite) {
                setIcon(heartIconFilled); // Ustawienie ikony pełnego serduszka
            } else {
                setIcon(heartIconEmpty); // Ustawienie ikony pustego serduszka
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

    public boolean isSongInPlaylist(String song) {
        String playlistFilePath = "./resources/playlists/Ulubione.txt";
        String songPath = "./resources/songs/"+song+".mp3";

        try (BufferedReader reader = new BufferedReader(new FileReader(playlistFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(songPath)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}