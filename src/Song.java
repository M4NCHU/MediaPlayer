import java.io.IOException;
import java.io.Serializable;

public class Song implements Serializable {

    private String songName;
    private String songPath;
    private boolean isFavorite;
    private static int nextSongId = 0;
    private int songId;

    public Song(String songName, String songPath, boolean isFavorite) {
        this.songId = nextSongId++;
        this.songName = songName;
        this.songPath = songPath;
        this.isFavorite = isFavorite;
    }

    public int getSongId() {
        return songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}