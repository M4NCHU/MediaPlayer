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

        String selectedPlaylist;

        public SidebarPanel() {
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
                                        DefaultListModel<String> model = (DefaultListModel<String>) playlistList.getModel();
                                        model.addElement(playlistName);

                                }
                        }
                });
                // Przykładowa lista odtwarzania
                DefaultListModel<String> playlistModel = new DefaultListModel<>();


                playlistList = new JList<>(playlistModel);
                playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                playlistList.setBackground(Color.decode(sidebarBtnColor));
                playlistList.setForeground(Color.decode(sidebarBtnForeColor));
                playlistList.setSelectionBackground(Color.decode("#3498DB"));
                playlistList.setSelectionForeground(Color.WHITE);
                JScrollPane scrollPane = new JScrollPane(playlistList);
                refreshPlaylist();
                // Dodanie tytułu, przycisku i listy do sidebara
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
                File allSongsFile = new File("./resources/playlists/All Songs.txt");

                // Check if All Songs.txt exists
                if (!allSongsFile.exists()) {
                        try {
                                allSongsFile.createNewFile();
                        } catch (IOException e) {
                                e.printStackTrace();
                                return;
                        }
                }

                // Read existing paths from All Songs.txt
                List<String> existingPaths = new ArrayList<>();
                try {
                        existingPaths = Files.readAllLines(allSongsFile.toPath());
                } catch (IOException e) {
                        e.printStackTrace();
                }

                // Find new paths in the songs directory
                List<String> newPaths = new ArrayList<>();
                File[] songFiles = songsDir.listFiles();
                if (songFiles != null) {
                        for (File songFile : songFiles) {
                                String songPath = "./resources/songs/"+songFile.getName();

                                if (!existingPaths.contains(songPath)) {
                                        newPaths.add(songPath);
                                }
                        }
                }

                // Append new paths to All Songs.txt
                if (!newPaths.isEmpty()) {
                        try {
                                Files.write(allSongsFile.toPath(), newPaths, StandardOpenOption.APPEND);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }




        public static void createNewPlaylist(String fileName) {
                try {
                        String path = "./resources/playlists/" + fileName + ".txt";
                        File file = new File(path);
                        if (file.exists()) {
                                System.out.println("Playlist already exists, try another name: " + path);
                                return;
                        }
                        boolean success = file.createNewFile();
                        if (success) {
                                System.out.println("New playlist created: " + path);
                        } else {
                                System.out.println("Failed to create playlist: " + path);
                        }
                } catch (IOException e) {
                        System.err.println("Error creating new playlist file: " + e.getMessage());
                }
        }

        public void refreshPlaylist() {

                DefaultListModel<String> model = (DefaultListModel<String>) playlistList.getModel();
                File folder = new File("./resources/playlists/");
                File[] playlistFiles = folder.listFiles();
                model.clear();
                for (File playlistFile : playlistFiles) {
                        if (playlistFile.isFile() && playlistFile.getName().endsWith(".txt")) {
                                model.addElement(playlistFile.getName().replace(".txt", ""));
                        }
                }

        }

}


