import javafx.scene.media.MediaPlayer;

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
    private ImageIcon playIcon, pauseIcon, heartIconFilled, heartIconEmpty;
    public static JLabel titleLabel;

    public Boolean isPlaying = false;


    public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, JTable table, MediaManager mediaManager, MusicPlayer musicPlayer) {

        super(checkBox);
        this.musicPlayer = musicPlayer;
        this.model = model;
        this.table = table;
        this.mediaManager = mediaManager;
        IconGenerator iconGenerator = new IconGenerator(15, 15);
        playIcon = iconGenerator.createIcon(Globals.imageFolder + "play.png");
        pauseIcon = iconGenerator.createIcon(Globals.imageFolder + "pause.png");
        heartIconFilled = iconGenerator.createIcon(Globals.imageFolder + "heart.png");
        heartIconEmpty = iconGenerator.createIcon(Globals.imageFolder + "heartEmpty.png");
        playButton = new JButton();
        playButton.setOpaque(true);

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int row = table.convertRowIndexToModel(table.getEditingRow());
                String songPath = (String) model.getValueAt(row, 1);

                Boolean isPlaying = mediaManager.getMediaPlayerStatus();
                System.out.println(isPlaying);

                if (isPlaying) {
                    mediaManager.stopSong();
                    playButton.setIcon(playIcon);
                } else {
                    mediaManager.playSong(songPath);

                    playButton.setIcon(pauseIcon);

                }
                musicPlayer.setSongName(songPath);
            }
        });






        serduszkoButton = new JButton();
        serduszkoButton.setOpaque(true);

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

        String playlistFilePath = Globals.playlistFolder  + playlistName + ".dat";
        File playlistFile = new File(playlistFilePath);
        SongList playlist = new SongList(playlistFilePath);
        playlist.loadPlaylist();
        boolean isFavorite = playlist.isSongInPlaylist(songPath, playlistName);
//        System.out.println("liked: " + isFavorite);
        Song song = new Song(songName, songPath, isFavorite);

        // Check if the song already exists in the playlist
        if (isFavorite) {

            playlist.removeSong(song);
            playlist.savePlaylist();
            if (!playlistName.equals("Ulubione")) {
                JOptionPane.showMessageDialog(table, "Usunięto z " + playlistName);

            }
            if (playlistName.equals("Ulubione")) serduszkoButton.setIcon(heartIconEmpty);
        } else {
            playlist.addSong(song);
            playlist.savePlaylist();
            if (!playlistName.equals("Ulubione")) {
                JOptionPane.showMessageDialog(table, "Dodano do " + playlistName);
            }
            if (playlistName.equals("Ulubione")) serduszkoButton.setIcon(heartIconFilled);
        }
    }

    private static void showPlaylistDialog(JList<String> playlistList) {
        DefaultListModel<String> modelPlaylist = (DefaultListModel<String>) playlistList.getModel();
        File folder = new File(Globals.playlistFolder);
        File[] playlistFiles = folder.listFiles();
        modelPlaylist.clear();

        assert playlistFiles != null;
        for (File playlistFile : playlistFiles) {
            if (playlistFile.isFile() && playlistFile.getName().endsWith(".dat")) {
                modelPlaylist.addElement(playlistFile.getName().replace(".dat", ""));
            }
        }

        // Dialog with playlist list
        JOptionPane.showMessageDialog(null, playlistList);
    }


}