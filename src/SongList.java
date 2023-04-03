import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongList {

    private List<String> songPaths;

    public SongList() {
        songPaths = new ArrayList<>();
        loadSongs();
    }

    private void loadSongs() {
        File songsFolder = new File("./resources/songs");

        if (songsFolder.exists() && songsFolder.isDirectory()) {
            for (File file : songsFolder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    songPaths.add(file.getPath());
                }
            }
        }
    }

    public List<String> getSongPaths() {
        return songPaths;
    }
}
