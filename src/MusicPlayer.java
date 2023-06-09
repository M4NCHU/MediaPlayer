import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;


public class MusicPlayer extends JFrame implements ActionListener {

    boolean checkStatus = false;
    private int toolbarIconsWidth = 20;
    private int toolbarIconsHeight = 20;
    private JLabel songLabel;
    private Container container;
    private JButton playBtn, pauseBtn, nextBtn, previousBtn;
    List<String> songPaths;
    // icons
    private ImageIcon playIcon, nextIcon, previousIcon, pauseIcon;

    // Sidebar components
    private SidebarPanel sidebarPanel;
    private RightPanel rightPanel;
    MediaManager mediaManager;
    JPanel songPanel;
    private int songPanelX;
    private int songPanelY;
    SongList songsList;
    JPanel buttonPanel;
    public JLabel titleLabel;
    String songTitle = "-";

    JProgressBar progressBar;
    JLabel remainingTimeLabel;
    JLabel totalTimeLabel;
    private JLabel currentTimeLabel;
    private boolean seeking = false;

    public MusicPlayer() {
        // initialize SongList Class
        Toolkit tk=Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setTitle("Odtwarzacz muzyczny");
        setSize(screenSize.width/2, screenSize.height/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        UIManager.put("text", Color.WHITE);
        // Container
        container = getContentPane();
        container.setLayout(new BorderLayout());

        songsList = new SongList("");
        mediaManager = new MediaManager(songPaths);
        // Sidebars
        sidebarPanel = new SidebarPanel();
        sidebarPanel.playlistList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // get the selected playlist
                String selectedPlaylist = sidebarPanel.getSelectedPlaylist();

                // update the table with the new song list
                refreshTable(selectedPlaylist);
            }
        });

        rightPanel = new RightPanel();
        songPanel = new JPanel(new BorderLayout());

        refreshTable("");

        // Icons - Generated by IconGenerator Class
        IconGenerator iconGenerator = new IconGenerator(toolbarIconsWidth, toolbarIconsHeight);
        playIcon = iconGenerator.createIcon(Globals.imageFolder + "play.png");
        nextIcon = iconGenerator.createIcon(Globals.imageFolder + "next.png");
        previousIcon = iconGenerator.createIcon(Globals.imageFolder + "previous.png");
        pauseIcon = iconGenerator.createIcon(Globals.imageFolder + "pause.png");

        System.out.println(Globals.playlistFolder + "play.png");
        // Music control btns
        playBtn = new JButton(playIcon);
        previousBtn = new JButton(previousIcon);
        nextBtn = new JButton(nextIcon);
        pauseBtn = new JButton(pauseIcon);

        songLabel = new JLabel();
        buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.decode("#34495E"));

        // Panel z przyciskami - FlowLayout (do umieszczenia przycisków na środku)
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setBackground(Color.decode("#34495E"));

        // Dodanie przycisków do buttonContainer
        buttonContainer.add(previousBtn);
        buttonContainer.add(playBtn);
        buttonContainer.add(nextBtn);

//         Suwak głośności


        // Dodanie suwaka głośności i buttonContainer do buttonPanel
        buttonPanel.add(buttonContainer, BorderLayout.CENTER);


        playBtn.setIcon(checkStatus ? playIcon : pauseIcon);

        checkStatus =  mediaManager.getMediaPlayerStatus();

        // Music controls - Action Listeners
        playBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaManager.stopSong();

                checkStatus = !checkStatus;
                playBtn.setIcon(checkStatus ? playIcon : pauseIcon);
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaManager.playNextSong();
                songTitle = mediaManager.getSongLabel();
                setSongName(songTitle);

            }
        });

        previousBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaManager.playPreviousSong();
                songTitle = mediaManager.getSongLabel();
                setSongName(songTitle);


            }
        });

        // Change BG color of Music Control panel
        buttonPanel.setBackground(Color.decode("#34495E"));

        progressBar = new JProgressBar();
        mediaManager.setSongProgressBar(progressBar);

        titleLabel = new JLabel(songTitle);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(1);
        volumeSlider.setPaintTicks(true);
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int volume = volumeSlider.getValue();
                // Zmiana głośności na podstawie wartości suwaka
                mediaManager.setVolume(volume);
            }
        });

        JButton speedUpButton = new JButton("2.0x");
        speedUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Zwiększ prędkość odtwarzania
                mediaManager.setPlaybackSpeed(2.0);
            }
        });

        JButton slowDownButton = new JButton("1.0x");
        slowDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Zmniejsz prędkość odtwarzania
                mediaManager.setPlaybackSpeed(1.0);
            }
        });

// Panel z titleLabel i sliderem
        JPanel titleAndSliderPanel = new JPanel();
//        titleAndSliderPanel.setBackground(Color.decode("#34495E"));
        titleAndSliderPanel.setLayout(new BorderLayout());

// Panel z przyciskami do zmiany prędkości odtwarzania i suwakiem głośności
        JPanel controlPanel = new JPanel();
//        controlPanel.setBackground(Color.decode("#34495E"));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.LINE_AXIS));

        controlPanel.add(slowDownButton);
        controlPanel.add(speedUpButton);

        controlPanel.add(Box.createHorizontalStrut(10)); // Dodatkowy odstęp między przyciskami a suwakiem
        controlPanel.add(volumeSlider);

// Dodanie titleLabel i controlPanel do panelu titleAndSliderPanel
        titleAndSliderPanel.add(titleLabel, BorderLayout.WEST);
        titleAndSliderPanel.add(controlPanel, BorderLayout.EAST);




        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(createProgressPanel(), BorderLayout.NORTH);
        bottomPanel.add(titleAndSliderPanel, BorderLayout.SOUTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        // UI Container - Components
        container.add(sidebarPanel, BorderLayout.WEST);
//        container.add(rightPanel, BorderLayout.EAST);
        container.add(songPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);
        container.setForeground(Color.WHITE);

        setVisible(true);
    }

    public static void main(String[] args) {

        // initialize main class
        new MusicPlayer();
    }

    public void refreshTable(String selectedPlaylist) {
        SongList songsList;

        if (selectedPlaylist == null || selectedPlaylist.isEmpty() || selectedPlaylist.equals("All Songs")) {
            songsList = new SongList(Globals.playlistFolder + "All Songs.dat");
        } else {
            songsList = new SongList(Globals.playlistFolder + selectedPlaylist + ".dat");
        }


        songsList.loadPlaylist(); // Wczytaj playlistę z pliku
        List<String> songPaths = songsList.getSongPaths();

        mediaManager.setSongPaths(songPaths);
        mediaManager.getSongProgressPercentage();
        songPanelX = songPanel.getX();
        songPanelY = songPanel.getY();
        String[] columnNames = {"Nazwa", "Ścieżka", "Odtwarzaj", "Dodaj do ulubionych", "Dodaj do playlisty"};

        if (songPaths.isEmpty()) {
            Object[][] data = {{"Pusta playlista", "", "", "", ""}};
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable table = new JTable(model);
            songPanel.setLocation(songPanelX, songPanelY);
            songPanel.removeAll();
            songPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            songPanel.revalidate();
            songPanel.repaint();
            return;
        }

        Object[][] data = new Object[songPaths.size()][5];

        for (int i = 0; i < songPaths.size(); i++) {
            String songPath = songPaths.get(i);
            String songName = new File(songPath).getName().replace(".mp3", "");
            Song song = new Song(songName, songPath, false);

            String songLabel = song.getSongName();
            String songAuthor = song.getSongPath();
            JButton playButton = new JButton("Play");
            JButton serduszkoButton = new JButton("Dodaj do ulubionych");
            JButton addToPlaylist = new JButton("Dodaj do playlisty");

            data[i][0] = songLabel;
            data[i][1] = songAuthor;
            data[i][2] = playButton;
            data[i][3] = serduszkoButton;
            data[i][4] = addToPlaylist;
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        TableColumn playColumn = table.getColumnModel().getColumn(2);


        IconGenerator iconGenerator = new IconGenerator(15, 15);
        ImageIcon playIcon = iconGenerator.createIcon(Globals.imageFolder + "play.png");
        playColumn.setCellRenderer(new ButtonRenderer(playIcon, ""));
        playColumn.setCellEditor(new ButtonEditor(new JCheckBox(), model, table, mediaManager, this));

        TableColumn serduszkoColumn = table.getColumnModel().getColumn(3);

        ImageIcon heart = iconGenerator.createIcon(Globals.imageFolder + "heart.png");
        serduszkoColumn.setCellRenderer(new ButtonRenderer(heart, "heart"));
        serduszkoColumn.setCellEditor(new ButtonEditor(new JCheckBox(), model, table, mediaManager, this));

        TableColumn addToPlaylistColumn = table.getColumnModel().getColumn(4);
        ImageIcon playlist = iconGenerator.createIcon(Globals.imageFolder + "plus.png");
        addToPlaylistColumn.setCellRenderer(new ButtonRenderer(playlist, ""));
        addToPlaylistColumn.setCellEditor(new ButtonEditor(new JCheckBox(), model, table, mediaManager, this));


        songPanel.setLocation(songPanelX, songPanelY);

        songPanel.removeAll();
        songPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        songPanel.revalidate();
        songPanel.repaint();
    }

    private JPanel createProgressPanel() {
        JPanel progressPanel = new JPanel(new BorderLayout());

        remainingTimeLabel = new JLabel("00:00");
        remainingTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        totalTimeLabel = new JLabel("00:00");
        totalTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        progressBar.setPreferredSize(new Dimension(200, 20));
        progressBar.setStringPainted(true);
        progressBar.setString("");

        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mediaManager.getMediaPlayerStatus()) {
                    int mouseX = e.getX();
                    int progressBarWidth = progressBar.getWidth();
                    double progress = (double) mouseX / progressBarWidth;
                    int totalTime = mediaManager.getTotalTime();
                    int seekTime = (int) (progress * totalTime);
                    mediaManager.seekTo(seekTime);
                }
            }
        });

        JPanel labelsPanel = new JPanel(new GridLayout(1, 2));
        labelsPanel.add(remainingTimeLabel);
        labelsPanel.add(totalTimeLabel);

        JPanel progressContainerPanel = new JPanel(new BorderLayout());
//        progressContainerPanel.add(labelsPanel, BorderLayout.NORTH);
        progressContainerPanel.add(progressBar, BorderLayout.CENTER);

        progressPanel.add(progressContainerPanel, BorderLayout.CENTER);


        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProgressBar();
                remainingTimeLabel.setText(mediaManager.formatTime(mediaManager.getRemainingTime()));
                totalTimeLabel.setText(mediaManager.formatTime(mediaManager.getTotalTime()));
            }
        });
        timer.start();

        return progressPanel;
    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void updateProgressBar() {
        mediaManager.updateProgressBar();
    }

    public void setSongName(String songName) {
        songTitle = new File(songName).getName().replace(".mp3", "");
        titleLabel.setText(songTitle);
    }
}



