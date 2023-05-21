import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

                // Show playlist dialog
                showPlaylistDialog(playlistList);

                // Get new playlist
                String selectedPlaylist = playlistList.getSelectedValue();

                // Check if playlist is selected
                if (selectedPlaylist != null) {
                    // Add path to playlist
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
        String songName = (String) model.getValueAt(row, 0);
        File file = new File("./resources/playlists/" + playlistName + ".dat");
        SongList playlist = new SongList(file.getPath());
        playlist.loadPlaylist();
        Song song = new Song(songName, songPath, false);

        // Check if the song already exists in the playlist
        if (playlist.isSongInPlaylist(songPath)) {
            playlist.removeSong(song);
            playlist.savePlaylist();
            if (!playlistName.equals("Ulubione")) {
                JOptionPane.showMessageDialog(table, "Usunięto z " + playlistName);
            }
        } else {
            playlist.addSong(song);
            playlist.savePlaylist();
            if (!playlistName.equals("Ulubione")) {
                JOptionPane.showMessageDialog(table, "Dodano do " + playlistName);
            }
        }
    }

    private static void showPlaylistDialog(JList<String> playlistList) {
        DefaultListModel<String> modelPlaylist = (DefaultListModel<String>) playlistList.getModel();
        File folder = new File("./resources/playlists/");
        File[] playlistFiles = folder.listFiles();
        modelPlaylist.clear();

        for (File playlistFile : playlistFiles) {
            if (playlistFile.isFile() && playlistFile.getName().endsWith(".dat")) {
                modelPlaylist.addElement(playlistFile.getName().replace(".dat", ""));
            }
        }

        // Dialog with playlist list
        JOptionPane.showMessageDialog(null, playlistList);
    }
}