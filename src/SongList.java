import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SongList implements Serializable {

    private List<Song> songs;
    private File playlistFile;

    public SongList(String playlistFilePath) {
        songs = new ArrayList<>();
        playlistFile = new File(playlistFilePath);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.removeIf(s -> s.equals(song));
    }

    public List<Song> getSongs() {
        return songs;
    }

    public boolean isSongInPlaylist(String songPath, String playlistName ) {
        File playlistCheck = new File(Globals.playlistFolder + playlistName + ".dat");
//        System.out.println(playlistCheck);
        try {
            if (playlistCheck.exists() && playlistCheck.isFile()) {
//                System.out.println("siema");
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(playlistCheck));
                List<Song> loadedSongs = (List<Song>) inputStream.readObject();
                inputStream.close();

                for (Song song : loadedSongs) {

                    if (song.getSongPath().equals(songPath)) {
                        return true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            if (!(e instanceof EOFException)) {
                e.printStackTrace();
            }
        }


        return false;
    }

    public void savePlaylist() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(playlistFile));
            outputStream.writeObject(songs);
            outputStream.close();
            System.out.println("Zapisano playlistę.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSongPaths() {
        List<String> songPaths = new ArrayList<>();
        for (Song song : songs) {
            songPaths.add(song.getSongPath());
        }
        return songPaths;
    }

    public void loadPlaylist() {
        if (playlistFile.exists() && playlistFile.isFile()) {
            try {
                if (playlistFile.length() == 0) {
                    System.out.println("Playlista pusta.");
                    return;
                }
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(playlistFile));
                List<Song> loadedSongs = (List<Song>) inputStream.readObject();
                inputStream.close();

                songs.clear(); // Wyczyść istniejące utwory w SongList
                songs.addAll(loadedSongs); // Dodaj wczytane utwory do SongList

                if (loadedSongs.isEmpty()) {
                    System.out.println("Playlista pusta.");
                } else {
                    System.out.println("Załadowano playlistę.");
                }

            } catch (EOFException e) {
                System.out.println("Playlista pusta.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nie znaleziono. Tworzę nową playlistę.");
        }
    }
    public void setPlaylistFile(String playlistFilePath) {
        playlistFile = new File(playlistFilePath);
    }
}