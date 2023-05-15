import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ButtonEditor extends DefaultCellEditor {
    private MusicPlayer musicPlayer;
    private JButton playButton, addToPlaylist, serduszkoButton;
    private String label;
    private boolean isPushed;
    private DefaultTableModel model;
    private JTable table;
    List<String> songPaths;
    public JList<Song> songList;
    private MediaManager mediaManager;
    private ImageIcon playIcon, pauseIcon;
    public static JLabel titleLabel;


    public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, JTable table, MediaManager mediaManager, MusicPlayer musicPlayer) {

        super(checkBox);
        this.musicPlayer = musicPlayer;
        this.model = model;
        this.table = table;
        this.mediaManager = mediaManager;
        IconGenerator iconGenerator = new IconGenerator(20, 20);
        playIcon = iconGenerator.createIcon("./resources/play.png");
        pauseIcon = iconGenerator.createIcon("./resources/pause.png");
        playButton = new JButton();
        playButton.setOpaque(true);
        playButton.setText("pause");

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int row = table.convertRowIndexToModel(table.getEditingRow());
                String songPath = (String) model.getValueAt(row, 1);
                mediaManager.playSong(songPath);
                fireEditingStopped();
                musicPlayer.setSongName(songPath);
            }
        });
        serduszkoButton = new JButton();
        serduszkoButton.setOpaque(true);
        serduszkoButton.setText("Polubiono");
        serduszkoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSongToPlaylist("Ulubione");
            }
        });

        addToPlaylist = new JButton();
        addToPlaylist.setOpaque(true);
        addToPlaylist.setText("Dodaj do playlisty");

        addToPlaylist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> modelPlaylist = new DefaultListModel<>();
                JList<String> playlistList = new JList<>(modelPlaylist);

                // Wyświetlanie okna dialogowego z listą playlist
                showPlaylistDialog(playlistList);

                // Pobranie wybranej playlisty
                String selectedPlaylist = playlistList.getSelectedValue();

                // Sprawdzenie, czy wybrano playlistę
                if (selectedPlaylist != null) {
                    // Dodanie ścieżki do pliku dla wybranej playlisty
                    addSongToPlaylist(selectedPlaylist);
                }
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {

            serduszkoButton.setForeground(table.getForeground());
            serduszkoButton.setBackground(UIManager.getColor("Button.background"));
        }
        label = (value == null) ? "" : value.toString();
        if (column == 2) {
            return playButton;
        } else if (column == 3) {
            return serduszkoButton;
        } else {
            return addToPlaylist;
        }

    }

    public Object getCellEditorValue() {
        if (isPushed) {
            //
        }
        isPushed = false;
        return new String(label);
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }

    public void addSongToPlaylist(String playlistName) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        String songPath = (String) model.getValueAt(row, 1);
        try {
            File file = new File("./resources/playlists/"+playlistName+".txt");
            if (file.exists() && file.isFile()) {
                List<String> lines = Files.readAllLines(file.toPath());
                if (lines.contains(songPath)) {
                    lines.remove(songPath);
                    FileWriter writer = new FileWriter(file, false);
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                    writer.close();
                    JOptionPane.showMessageDialog(table, "Usunięto  z "+playlistName);
                    return;
                }
            }

            FileWriter writer = new FileWriter(file, true);
            writer.write(songPath + "\n");
            writer.close();
            JOptionPane.showMessageDialog(table, "Dodano  do  "+playlistName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void showPlaylistDialog(JList<String> playlistList) {
        DefaultListModel<String> modelPlaylist = (DefaultListModel<String>) playlistList.getModel();
        File folder = new File("./resources/playlists/");
        File[] playlistFiles = folder.listFiles();
        modelPlaylist.clear();

        for (File playlistFile : playlistFiles) {
            if (playlistFile.isFile() && playlistFile.getName().endsWith(".txt")) {
                modelPlaylist.addElement(playlistFile.getName().replace(".txt", ""));
            }
        }

        // Wyświetlanie okna dialogowego z listą playlist
        JOptionPane.showMessageDialog(null, playlistList);
    }
}