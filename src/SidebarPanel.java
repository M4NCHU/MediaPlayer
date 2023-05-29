import javax.swing.*;

import java.awt.*;
import java.nio.file.StandardOpenOption;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {
    private JPanel leftPanel;
    public JList<String> playlistList;
        private int sidebarButtonsHeight = 20;
        private String sidebarBtnColor = "#2C3E50";
        private String sidebarBtnForeColor = "#ffffff";

        private SongList allSongsList;
        String selectedPlaylist;

        public SidebarPanel() {
                allSongsList = new SongList("./resources/playlists/All Songs.dat");
                allSongsList.loadPlaylist();
                checkMainPlaylist();
                Mp3FileSaver saveSong = new Mp3FileSaver();

                setLayout(new BorderLayout());

                leftPanel = new JPanel();
                leftPanel.setBackground(Color.decode("#7F8C8D"));
                leftPanel.setPreferredSize(new Dimension(200, 0));

                // Tytuł sidebara
                JLabel titleLabel = new JLabel("Playlisty", SwingConstants.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
                titleLabel.setForeground(Color.WHITE);
                JPanel titlePanel = new JPanel(new BorderLayout());
                titlePanel.setBackground(Color.decode(sidebarBtnColor));
                titlePanel.add(titleLabel, BorderLayout.CENTER);
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridLayout(2, 1, 0, 5));

                // Przycisk dodawania nowych playlist
                JButton addPlaylistBtn = new JButton("Dodaj playlistę");
                addPlaylistBtn.setPreferredSize(new Dimension(200, sidebarButtonsHeight));
                addPlaylistBtn.setBackground(Color.decode(sidebarBtnColor));
                addPlaylistBtn.setForeground(Color.decode(sidebarBtnForeColor));

                JButton addSongBtn = new JButton("Dodaj piosenkę");
                addSongBtn.setPreferredSize(new Dimension(200, sidebarButtonsHeight));
                addSongBtn.setBackground(Color.decode(sidebarBtnColor));
                addSongBtn.setForeground(Color.decode(sidebarBtnForeColor));

                buttonPanel.add(addPlaylistBtn);
                buttonPanel.add(addSongBtn);

                addSongBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                saveSong.saveMp3File(leftPanel);
                                checkMainPlaylist();
                        }
                });

                addPlaylistBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                String playlistName = JOptionPane.showInputDialog(leftPanel, "Podaj nazwę nowej playlisty:");
                                if (playlistName != null && !playlistName.isEmpty()) {
                                        createNewPlaylist(playlistName);
                                        refreshPlaylist();
                                }
                        }
                });
                // playlist
                DefaultListModel<String> playlistModel = new DefaultListModel<>();


                playlistList = new JList<>(playlistModel);
                playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                playlistList.setBackground(Color.decode(sidebarBtnColor));
                playlistList.setForeground(Color.decode(sidebarBtnForeColor));
                playlistList.setSelectionBackground(Color.decode("#3498DB"));
                playlistList.setSelectionForeground(Color.WHITE);
                JScrollPane scrollPane = new JScrollPane(playlistList);
                refreshPlaylist();

                leftPanel.setLayout(new BorderLayout());
                leftPanel.add(titlePanel, BorderLayout.NORTH);
                leftPanel.add(buttonPanel, BorderLayout.SOUTH);
//                leftPanel.add(addSongBtn, BorderLayout.SOUTH);

                leftPanel.add(scrollPane, BorderLayout.CENTER);

                add(leftPanel, BorderLayout.WEST);
        }

    public String getSelectedPlaylist() {
        return playlistList.getSelectedValue();
    }

        public void checkMainPlaylist() {
                File songsDir = new File("./resources/songs/");
                File allSongsFile = new File("./resources/playlists/All Songs.dat");

                // ...

                // Check if All Songs.dat exists
                if (!allSongsFile.exists()) {
                        try {
                                allSongsFile.createNewFile();
                        } catch (IOException e) {
                                e.printStackTrace();
                                return;
                        }
                }

                // Read existing songs from All Songs.dat
                SongList allSongsList = new SongList(allSongsFile.getPath());
                allSongsList.loadPlaylist();
                List<String> existingPaths = allSongsList.getSongPaths();
                for (Song song : allSongsList.getSongs()) {
                        existingPaths.add(song.getSongPath());
                }

                // Find new paths in the songs directory
                List<String> newPaths = new ArrayList<>();
                File[] songFiles = songsDir.listFiles();
                if (songFiles != null) {
                        for (File songFile : songFiles) {
                                String songPath = "./resources/songs/" + songFile.getName();

                                if (!existingPaths.contains(songPath)) {
                                        newPaths.add(songPath);
                                }
                        }
                }

                // Create new Song objects for new paths and add them to the playlist
                if (!newPaths.isEmpty()) {
                        for (String newPath : newPaths) {
                                String songName = newPath.substring(newPath.lastIndexOf("/") + 1);
                                Song newSong = new Song(songName, newPath, false);
                                allSongsList.addSong(newSong);
                        }

                        // Save the updated playlist
                        allSongsList.savePlaylist();
                }
        }




        public static void createNewPlaylist(String fileName) {
                try {
                        String path = "./resources/playlists/" + fileName + ".dat";
                        File file = new File(path);
                        if (file.exists()) {
                                System.out.println("Playlista istnieje. Spróbuj innej nazwy: " + path);
                                return;
                        }
                        boolean success = file.createNewFile();
                        if (success) {
                                System.out.println("Utworzono playlistę: " + path);
                        } else {
                                System.out.println("Wystąpił błąd podczas tworzenia playlisty: " + path);
                        }
                } catch (IOException e) {
                        System.err.println("Wystąpił błąd podczas tworzenia playlisty: " + e.getMessage());
                }
        }

        public void refreshPlaylist() {
                DefaultListModel<String> model = (DefaultListModel<String>) playlistList.getModel();
                File folder = new File("./resources/playlists/");
                File[] playlistFiles = folder.listFiles();
                model.clear();
                for (File playlistFile : playlistFiles) {
                        if (playlistFile.isFile() && playlistFile.getName().endsWith(".dat")) {
                                model.addElement(playlistFile.getName().replace(".dat", ""));
                        }
                }
        }

}


