import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Mp3FileSaver {

    public void saveMp3File(JPanel parentFrame) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki MP3", "mp3");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileInputStream inputStream = new FileInputStream(selectedFile);
                File destinationDirectory = new File(Globals.songsFolder);
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdir();
                }
                File destinationFile = new File(destinationDirectory.getAbsolutePath() + "/" + selectedFile.getName());
                FileOutputStream outputStream = new FileOutputStream(destinationFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
                outputStream.close();
                System.out.println("Plik zapisany w katalogu" + Globals.imageFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
