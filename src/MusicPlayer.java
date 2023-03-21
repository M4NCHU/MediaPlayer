import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MusicPlayer extends JFrame implements ActionListener {

    DefaultListModel<String> listModel;
    public JPanel leftPanel;
    JList<String> songList;
    private JFXPanel fxPanel;



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

    private File[] files;

    JFileChooser fileChooserPath = new JFileChooser();

    private List<File> songs;
    private int currentSongIndex;

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


        songPaths = new ArrayList<>();
        File songsFolder = new File("./resources/songs");
        System.out.println(songsFolder.getAbsolutePath());
        if (songsFolder.exists() && songsFolder.isDirectory()) {

            for (File file : songsFolder.listFiles()) {

                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    songPaths.add(file.getPath());

                }
            }
        }

        // create list model and add elements to it
        listModel = new DefaultListModel<>();
        for (String songPath : songPaths) {
            listModel.addElement(new File(songPath).getName());
        }

        // create song list
        songList = new JList<>(listModel);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.setBackground(Color.decode("#2C3E50"));
        songList.setForeground(Color.WHITE);

        // create list selection listener
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedSongPath = songPaths.get(songList.getSelectedIndex());
                    playSong(selectedSongPath);

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

        songLabel = new JLabel();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(songLabel);
        buttonPanel.add(previousBtn);
        buttonPanel.add(playBtn);
        buttonPanel.add(nextBtn);

        buttonPanel.setBackground(Color.decode("#34495E"));


        JPanel bottomPanel = new JPanel(new BorderLayout());
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(50);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);



        // Dodawanie paneli, listy i paska narzędzi do kontenera
        container.add(leftPanel, BorderLayout.WEST);
        container.add(rightPanel, BorderLayout.EAST);
        container.add(songList, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);
        container.setForeground(Color.WHITE);

        setVisible(true);
    }

    private void playSong(String selectedSongPath) {
        System.out.println(selectedSongPath);
        // tworzenie obiektu Media z podanego pliku

        Media media = new Media(new File(selectedSongPath).toURI().toString());

        // tworzenie obiektu MediaPlayer i przypisanie do niego obiektu Media
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        // odtwarzanie muzyki
        mediaPlayer.play();
    }

    public static void main(String[] args) {

        new MusicPlayer();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}



