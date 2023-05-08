import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongList {

    private List<String> songPaths;
    private String playList;
    private File songsFile;

    public SongList(String songsFilePath) {
        songPaths = new ArrayList<>();
        songsFile = new File(songsFilePath);
        loadSongs();
    }



    private void loadAllSongs() {
        File songsFolder = new File("./resources/songs");

        if (songsFolder.exists() && songsFolder.isDirectory()) {
            for (File file : songsFolder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    songPaths.add(file.getPath());
                }
            }
        }
    }

    private void loadSongs() {
        if (songsFile.exists() && songsFile.isFile()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(songsFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    songPaths.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loadAllSongs();
        }

    }

    public void setSongsFilePath(String songsFilePath) {
        songsFile = new File(songsFilePath);
        songPaths.clear();
        loadSongs();
    }

    public List<String> getSongPaths() {
        return songPaths;
    }
}
