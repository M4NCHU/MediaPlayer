import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class MusicPlayer extends JFrame implements ActionListener {

    JPanel mainPanel;
    DefaultListModel<String> listModel;
    public JPanel leftPanel;
    JList<String> songList;



    private int toolbarIconsWidth = 20;
    private int toolbarIconsHeight = 20;
    private int sidebarButtonsHeight = 20;
    private String sidebarBtnColor = "#2C3E50";
    private String sidebarBtnForeColor = "#ffffff";

    private JLabel songLabel;

    private JButton playBtn, pauseBtn, nextBtn, previousBtn;


    //  JavaFX


    private Media media;
    private MediaPlayer mediaPlayer;

    private FileInputStream fileInputStream;
    private File[] files;

    private int MusicPlayerStatus = 0;
    private int filepathresponse;

    BufferedInputStream bufferedInputStream;
    JFileChooser fileChooserPath = new JFileChooser();

    private List<File> songs;
    private int currentSongIndex;
    private JList<File> list;
    private JList<String> listNew;

    FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("MP3 File","mp3");
    private int[] speeds;


    private Timer timer;

    private boolean running;
    private ArrayList<String> songPaths;

    public MusicPlayer() {

        this.songs = new ArrayList<>();
        this.currentSongIndex = -1;

        Toolkit tk=Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setTitle("Odtwarzacz muzyczny");
        setSize(screenSize.width/2, screenSize.height/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        UIManager.put("text", Color.WHITE);




        // Kontener
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Pasek boczny
        leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        leftPanel.setBackground(Color.decode("#7F8C8D"));
        rightPanel.setBackground(Color.decode("#7F8C8D"));
        leftPanel.setPreferredSize(new Dimension(200, 0));
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(Color.decode("#7F8C8D"));
        navigationPanel.setLayout(new GridLayout(0, 1, 0, 10));
        navigationPanel.add(new JLabel("Nawigacja"));

        JButton button1 = new JButton("Strona główna");
        button1.setMaximumSize(new Dimension(Integer.MAX_VALUE, sidebarButtonsHeight));
        button1.setBackground(Color.decode(sidebarBtnColor));
        button1.setForeground(Color.decode(sidebarBtnForeColor));
        navigationPanel.add(button1);

        JButton button2 = new JButton("Playlisty");
        button2.setMaximumSize(new Dimension(Integer.MAX_VALUE, sidebarButtonsHeight));
        button2.setBackground(Color.decode(sidebarBtnColor));
        button2.setForeground(Color.decode(sidebarBtnForeColor));
        navigationPanel.add(button2);


        JButton button3 = new JButton("Biblioteka");
        button3.setMaximumSize(new Dimension(Integer.MAX_VALUE, sidebarButtonsHeight));
        button3.setBackground(Color.decode(sidebarBtnColor));
        button3.setForeground(Color.decode(sidebarBtnForeColor));
        navigationPanel.add(button3);

        // Dodawanie listy playlist do panelu bocznego po lewej stronie
        JPanel playlistPanel = new JPanel();
        playlistPanel.setBackground(Color.decode("#7F8C8D"));
        playlistPanel.setLayout(new GridLayout(0, 1, 0, 10));
        playlistPanel.add(new JLabel("Playlisty"));

        JButton button4 = new JButton("Playlista 1");
        button4.setBackground(Color.decode(sidebarBtnColor));
        button4.setMaximumSize(new Dimension(Integer.MAX_VALUE, sidebarButtonsHeight));
        button4.setForeground(Color.decode(sidebarBtnForeColor));
        playlistPanel.add(button4);

        JButton button5 = new JButton("Playlista 2");
        button5.setMaximumSize(new Dimension(Integer.MAX_VALUE, sidebarButtonsHeight));
        button5.setBackground(Color.decode(sidebarBtnColor));
        button5.setForeground(Color.decode(sidebarBtnForeColor));
        playlistPanel.add(button5);

        JButton button6 = new JButton("Playlista 3");
        button6.setMaximumSize(new Dimension(Integer.MAX_VALUE, sidebarButtonsHeight));
        button6.setBackground(Color.decode(sidebarBtnColor));
        button6.setForeground(Color.decode(sidebarBtnForeColor));
        playlistPanel.add(button6);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, navigationPanel, playlistPanel);
        splitPane.setDividerLocation(150); // początkowa pozycja dzielnika

        // Dodawanie elementów do panelu bocznego po lewej stronie
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(splitPane, BorderLayout.CENTER);

        // Elementy panelu bocznego po prawej stronie
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        rightPanel.add(new JLabel("Prawy pasek"));
        rightPanel.add(new JButton("Element 1"));
        rightPanel.add(new JButton("Element 2"));
        rightPanel.add(new JButton("Element 3"));

        // Tworzenie listy utworów
        // Tworzenie modelu tabeli
        //        String[] columnNames = {"Tytuł", "Wykonawca", "Album", "Czas trwania"};
        //        Object[][] data = {
        //                {"Utwór 1", "Wykonawca 1", "Album 1", "3:47"},
        //                {"Utwór 2", "Wykonawca 2", "Album 2", "4:12"},
        //                {"Utwór 3", "Wykonawca 3", "Album 3", "5:23"},
        //                {"Utwór 4", "Wykonawca 4", "Album 4", "2:55"},
        //                {"Utwór 5", "Wykonawca 5", "Album 5", "3:15"}
        //        };
        //        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        //
        //// Tworzenie tabeli i ustawianie jej właściwości
        //        JTable table = new JTable(tableModel);
        //        table.setFillsViewportHeight(true);
        //
        //        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //        table.getTableHeader().setReorderingAllowed(false);
        //        table.setEnabled(false);
        //
        //// Dodawanie tabeli do panelu i ustawianie jego właściwości
        //        JPanel centerPanel = new JPanel(new BorderLayout());
        //        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        songPaths = new ArrayList<String>();
        File songsFolder = new File("songs");
        if (songsFolder.exists() && songsFolder.isDirectory()) {
            for (File file : songsFolder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    songPaths.add(file.getPath());
                }
            }
        }

        listModel = new DefaultListModel<>();
        for (String songPath : songPaths) {
            listModel.addElement(new File(songPath).getName());
        }
        songList = new JList<>(listModel);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.setBackground(Color.decode("#2C3E50"));
        songList.setForeground(Color.WHITE);
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedSongPath = songPaths.get(songList.getSelectedIndex());
                    playMusic(selectedSongPath);
                }
            }
        };
        songList.addListSelectionListener(listSelectionListener);



        // Zmiana rozmiaru ikon
        ImageIcon playIcon = new ImageIcon(new ImageIcon("./resources/play.png").getImage().getScaledInstance(toolbarIconsWidth, toolbarIconsHeight, Image.SCALE_SMOOTH));
        ImageIcon nextIcon = new ImageIcon(new ImageIcon("./resources/next.png").getImage().getScaledInstance(toolbarIconsWidth, toolbarIconsHeight, Image.SCALE_SMOOTH));
        ImageIcon previousIcon = new ImageIcon(new ImageIcon("./resources/previous.png").getImage().getScaledInstance(toolbarIconsWidth, toolbarIconsHeight, Image.SCALE_SMOOTH));

        playBtn = new JButton(playIcon);
        previousBtn = new JButton(previousIcon);
        nextBtn = new JButton(nextIcon);

        // Adding action listeners





        songLabel = new JLabel();



        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(songLabel);
        buttonPanel.add(previousBtn);
        buttonPanel.add(playBtn);
        buttonPanel.add(nextBtn);


        playBtn.addActionListener(e -> playCurrentSong());
        previousBtn.addActionListener(this);
        nextBtn.addActionListener(e -> playNextSong());
        buttonPanel.setBackground(Color.decode("#34495E"));


        JPanel bottomPanel = new JPanel(new BorderLayout());
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(50);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);

        mediaPlayer = new MediaPlayer(new Media(new File(songPaths.get(0)).toURI().toString()));
        mediaPlayer.setOnEndOfMedia(() -> playNextSong());

        // Dodawanie paneli, listy i paska narzędzi do kontenera
        container.add(leftPanel, BorderLayout.WEST);
        container.add(rightPanel, BorderLayout.EAST);
        container.add(songList, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);
        container.setForeground(Color.WHITE);

        setVisible(true);
    }

    private void playNextSong() {
        int selectedIndex = songList.getSelectedIndex();
        if (selectedIndex == -1) {
            playMusic(songPaths.get(0));
            songList.setSelectedIndex(0);
        } else if (selectedIndex < songPaths.size() - 1) {
            playMusic(songPaths.get(selectedIndex + 1));
            songList.setSelectedIndex(selectedIndex + 1);
        } else {
            playMusic(songPaths.get(0));
            songList.setSelectedIndex(0);
        }
    }

    private void playCurrentSong() {
        int selectedIndex = songList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedSongPath = songPaths.get(selectedIndex);
            playMusic(selectedSongPath);
        }
    }

    // initialize method, allows user to choose files to play
    public void init() {
        File folder = new File("songs");
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".mp3")) {
                songs.add(file);
                listModel.addElement(songs.toString());
            }
        }

//        playNextSong();
//        if (MusicPlayerStatus == 0) {
//            // limit file types on FileChooser
//            fileChooserPath.setFileFilter(fileNameExtensionFilter);
//            // allow user to choose multiple files
//            fileChooserPath.setMultiSelectionEnabled(true);
//            filepathresponse = fileChooserPath.showOpenDialog(leftPanel);
//            if (filepathresponse == JFileChooser.APPROVE_OPTION) {
//                files = null;
//                songNumber = 0;
//                files = fileChooserPath.getSelectedFiles();
//                songPath = files[0].getAbsolutePath();
//
//
//                for (int i = 0; i < files.length; i++){
//                    listModel.addElement(files[i].getName());
//                }
//
//                MusicPlayerStatus = 1;
//                songList.setSelectedIndex(0);
//
//            }
//        }
    }

    private void playNextSong(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("Nie podano ścieżki do pliku.");
            return;
        }

        File file = new File(filePath);
        if (!file.isFile() || !file.getName().endsWith(".mp3")) {
            System.out.println("Nieprawidłowa ścieżka do pliku.");
            return;
        }

        Media media = new Media(file.toURI().toString());

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> playNextSong(filePath));
        mediaPlayer.play();

        System.out.println("Gra teraz: " + file.getName());
        currentSongIndex++;
        Platform.runLater(() -> list.setSelectedIndex(currentSongIndex));
    }

    public void playMusic(String songPath) {

        Media song = new Media(new File(songPath).toURI().toString());
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(song);
        mediaPlayer.play();

    }

    public void pauseMusic() {
        mediaPlayer.pause();
    }



    public static void main(String[] args) {

        new MusicPlayer();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

//        if (o == playBtn){
//            playMusic();
//        }
         if (o == nextBtn) {
            init();
        }
    }

}



