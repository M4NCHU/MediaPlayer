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
        String playlistFilePath = "./resources/playlists/Ulubione.dat";
        String songPath = "./resources/songs/"+song+".mp3";

        try {
            File playlistFile = new File(playlistFilePath);
            if (playlistFile.exists() && playlistFile.isFile()) {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(playlistFile));
                List<Song> songs = (List<Song>) inputStream.readObject();
                inputStream.close();

                for (Song playlistSong : songs) {
                    if (playlistSong.getSongPath().equals(songPath)) {
                        return true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}