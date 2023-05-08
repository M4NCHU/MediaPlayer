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
    private JButton playButton;
    private JButton serduszkoButton;
    private JButton button;
    private String label;
    private boolean isPushed;
    private DefaultTableModel model;
    private JTable table;
    List<String> songPaths;
    public JList<Song> songList;
    private MediaManager mediaManager;
    private ImageIcon playIcon, pauseIcon;


    public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, JTable table, MediaManager mediaManager) {

        super(checkBox);
        this.model = model;
        this.table = table;
        this.mediaManager = mediaManager;
        IconGenerator iconGenerator = new IconGenerator(20, 20);
        playIcon = iconGenerator.createIcon("./resources/play.png");
        pauseIcon = iconGenerator.createIcon("./resources/pause.png");
        playButton = new JButton("playIcon");
        playButton.setOpaque(true);

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int row = table.convertRowIndexToModel(table.getEditingRow());
                String songPath = (String) model.getValueAt(row, 1);
                mediaManager.playSong(songPath);
                fireEditingStopped();
            }
        });
        serduszkoButton = new JButton("Serduszko");
        serduszkoButton.setOpaque(true);
        serduszkoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                String songPath = (String) model.getValueAt(row, 1);
                try {
                    File file = new File("songPaths.txt");
                    if (file.exists() && file.isFile()) {
                        List<String> lines = Files.readAllLines(file.toPath());
                        if (lines.contains(songPath)) {
                            lines.remove(songPath);
                            FileWriter writer = new FileWriter(file, false);
                            for (String line : lines) {
                                writer.write(line + "\n");
                            }
                            writer.close();
                            JOptionPane.showMessageDialog(table, "Ścieżka usunięta z pliku songPaths.txt!");
                            return;
                        }
                    }

                    FileWriter writer = new FileWriter(file, true);
                    writer.write(songPath + "\n");
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            playButton.setForeground(table.getSelectionForeground());
            playButton.setBackground(table.getSelectionBackground());
            if (mediaManager.getMediaPlayerStatus()) {
                playButton.setIcon(pauseIcon);
            } else {
                playButton.setIcon(playIcon);
            }
            serduszkoButton.setForeground(table.getSelectionForeground());
            serduszkoButton.setBackground(table.getSelectionBackground());
        } else {
            playButton.setForeground(table.getForeground());
            playButton.setBackground(UIManager.getColor("Button.background"));
            if (mediaManager.getMediaPlayerStatus()) {
                playButton.setIcon(pauseIcon);
            } else {
                playButton.setIcon(playIcon);
            }
            serduszkoButton.setForeground(table.getForeground());
            serduszkoButton.setBackground(UIManager.getColor("Button.background"));
        }
        label = (value == null) ? "" : value.toString();
        if (column == 2) {
            return playButton;
        } else {
            return serduszkoButton;
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
}