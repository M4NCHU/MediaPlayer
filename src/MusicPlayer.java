import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;


public class MusicPlayer extends JFrame implements ActionListener {

    DefaultListModel<Song> listModel;
    public JList<Song> songList;
    private JFXPanel fxPanel;
    boolean checkStatus = false;

    private int toolbarIconsWidth = 20;
    private int toolbarIconsHeight = 20;

    private JLabel songLabel;
    private String songTitle;

    private JButton playBtn, pauseBtn, nextBtn, previousBtn;
//    JButton favouritesBtn;
//
    //  JavaFX

    List<String> songPaths;

    // icons
    private ImageIcon playIcon, nextIcon, previousIcon, pauseIcon;

    // Sidebar components
    private SidebarPanel sidebarPanel;
    private RightPanel rightPanel;

    public MusicPlayer() {
        // initialize SongList Class
        SongList songsList = new SongList();

        Toolkit tk=Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setTitle("Odtwarzacz muzyczny");
        setSize(screenSize.width/2, screenSize.height/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        UIManager.put("text", Color.WHITE);

        // Container
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Sidebars
        sidebarPanel = new SidebarPanel();
        rightPanel = new RightPanel();


        songPaths = songsList.getSongPaths(); // Get songs from SongList Class
        MediaManager mediaManager = new MediaManager(songPaths);



        // create list model and add elements to it
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Nazwa", "Ścieżka", "Dodaj do ulubionych"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // make all cells uneditable
                return false;
            }
        };

        JTable songTable = new JTable(tableModel);

// add songs to table model
        for (String songPath : songPaths) {
            String songName = new File(songPath).getName().replace(".mp3", "");
            Song song = new Song(songName, songPath, false);

            JButton favouritesBtn = new JButton("Dodaj do ulubionych");
            favouritesBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = songTable.getSelectedRow();
                    String selectedSongName = (String) tableModel.getValueAt(selectedRow, 0);
                    System.out.println("Dodano do ulubionych: " + selectedSongName);
                }
            });
            Object[] row = new Object[]{song.getSongName(), song.getSongPath(), favouritesBtn};
            tableModel.addRow(row);
        }



        // create table with song list

        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.setBackground(Color.decode("#2C3E50"));
        songTable.setForeground(Color.WHITE);




        // add list selection listener to play selected song
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = songTable.getSelectedRow();
                    String selectedSongPath = (String) tableModel.getValueAt(selectedRow, 1);

//                    System.out.println(selectedSongPath);
                    mediaManager.playSong(selectedSongPath);
                    songTitle = (String) tableModel.getValueAt(selectedRow, 0);

                    checkStatus = true;
                }
            }
        };
        songTable.getSelectionModel().addListSelectionListener(listSelectionListener);

        songTable.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
            private final JButton button = new JButton("Dodaj do ulubionych");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                button.setBackground(Color.decode("#3498DB"));
                button.setForeground(Color.WHITE);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
//                        String selectedSongId = (String) tableModel.getValueAt(row, 0);
                        System.out.println("Selected song id: ");
                    }
                });

                return button;
            }
        });

        // custom cell renderer for "Dodaj do ulubionych" column




        // Icons - Generated by IconGenerator Class
        IconGenerator iconGenerator = new IconGenerator(toolbarIconsWidth, toolbarIconsHeight);
        playIcon = iconGenerator.createIcon("./resources/play.png");
        nextIcon = iconGenerator.createIcon("./resources/next.png");
        previousIcon = iconGenerator.createIcon("./resources/previous.png");
        pauseIcon = iconGenerator.createIcon("./resources/pause.png");

        // Music control btns
        playBtn = new JButton(playIcon);
        previousBtn = new JButton(previousIcon);
        nextBtn = new JButton(nextIcon);
        pauseBtn = new JButton(pauseIcon);


        songLabel = new JLabel();
        songLabel.setText(songTitle);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.add(previousBtn);
        buttonPanel.add(playBtn);
        buttonPanel.add(nextBtn);


        JPanel songPanel = new JPanel(new BorderLayout());
        songPanel.add(songLabel, BorderLayout.NORTH);
        songPanel.add(buttonPanel, BorderLayout.CENTER);
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

        nextBtn.addActionListener(e->mediaManager.playNextSong());
        previousBtn.addActionListener(e->mediaManager.playPreviousSong());

        // Change BG color of Music Control panel
        buttonPanel.setBackground(Color.decode("#34495E"));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(50);


        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(songPanel, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.CENTER);

        // !! Important !! Initialize JavaFX Panel - Allow to play music
        fxPanel = new JFXPanel();

        // UI Container - Components
        container.add(sidebarPanel, BorderLayout.WEST);
        container.add(rightPanel, BorderLayout.EAST);
        container.add(songTable, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);
        container.add(fxPanel, BorderLayout.PAGE_START);
        container.setForeground(Color.WHITE);

        setVisible(true);
    }

    public static void main(String[] args) {
        // initialize main class
        new MusicPlayer();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}



