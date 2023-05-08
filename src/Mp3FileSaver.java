import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Mp3FileSaver {

    public void saveMp3File(JPanel parentFrame) {
        // Ustawienie filtra plików na .mp3
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki MP3", "mp3");
        // Utworzenie okna wyboru pliku
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Pobranie wybranego pliku
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Otwarcie strumienia wejściowego dla wybranego pliku
                FileInputStream inputStream = new FileInputStream(selectedFile);
                // Utworzenie ścieżki do katalogu ./resources/songs
                File destinationDirectory = new File("./resources/songs");
                // Sprawdzenie, czy katalog ./resources/songs istnieje
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdir();
                }
                // Utworzenie ścieżki do pliku w katalogu ./resources/songs
                File destinationFile = new File(destinationDirectory.getAbsolutePath() + "/" + selectedFile.getName());
                // Otwarcie strumienia wyjściowego dla pliku w katalogu ./resources/songs
                FileOutputStream outputStream = new FileOutputStream(destinationFile);
                // Kopiowanie pliku z wejściowego do wyjściowego strumienia
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                // Zamknięcie strumieni
                inputStream.close();
                outputStream.close();
                System.out.println("Plik zapisany w katalogu ./resources/songs");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
