import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
public class MediaPlayer extends JFrame {

    private int toolbarIconsWidth = 20;
    private int toolbarIconsHeight = 20;
    private int sidebarButtonsHeight = 20;
    private String sidebarBtnColor = "#2C3E50";
    private String sidebarBtnForeColor = "#ffffff";

    private DefaultListModel<Song> listModel;
    private JList<Song> songList;

    public MediaPlayer() {

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
        JPanel leftPanel = new JPanel();
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

        listModel = new DefaultListModel<>();
        listModel.addElement(new Song("Utwór 1", "Wykonawca 1", "Album 1", "3:47"));
        listModel.addElement(new Song("Utwór 2", "Wykonawca 2", "Album 2", "4:12"));
        listModel.addElement(new Song("Utwór 3", "Wykonawca 3", "Album 3", "5:23"));
        listModel.addElement(new Song("Utwór 4", "Wykonawca 4", "Album 4", "2:55"));
        listModel.addElement(new Song("Utwór 5", "Wykonawca 5", "Album 5", "3:15"));

        // Tworzenie listy i ustawianie jej właściwości
        songList = new JList<>(listModel);
        songList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        songList.setBackground(Color.decode("#2C3E50"));
        songList.setForeground(Color.WHITE);




        // Zmiana rozmiaru ikon
        ImageIcon playIcon = new ImageIcon(new ImageIcon("./resources/play.png").getImage().getScaledInstance(toolbarIconsWidth, toolbarIconsHeight, Image.SCALE_SMOOTH));
        ImageIcon nextIcon = new ImageIcon(new ImageIcon("./resources/next.png").getImage().getScaledInstance(toolbarIconsWidth, toolbarIconsHeight, Image.SCALE_SMOOTH));
        ImageIcon previousIcon = new ImageIcon(new ImageIcon("./resources/previous.png").getImage().getScaledInstance(toolbarIconsWidth, toolbarIconsHeight, Image.SCALE_SMOOTH));


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(new JButton(previousIcon));
        buttonPanel.add(new JButton(playIcon));
        buttonPanel.add(new JButton(nextIcon));

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

    public static void main(String[] args) {
        new MediaPlayer();
    }



}
