import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;


public class MediaManager {
    List<String> songPaths;
    private MediaPlayer currentMediaPlayer;

    private MediaPlayer mediaPlayer;

    private int songNumber = 0;

    private boolean isPlaying = false;
    private String songText = "";
    private JFXPanel fxPanel;


    public MediaManager(){
        String songPath = "";

    }

    public MediaManager(List<String> songsList){
        songPaths = songsList;
        fxPanel = new JFXPanel();

    }

    public void playSong(String selectedSongPath) {

        try {

            if (currentMediaPlayer != null) {
                currentMediaPlayer.stop();
            }
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            // Create media object from file
            Media media = new Media(new File(selectedSongPath).toURI().toString());
            System.out.println(media);
            // Create New MediaPlayer object and place the media object as argument
            mediaPlayer = new MediaPlayer(media);
            // Save Current MediaPlayer object to currentMediaPlayer
            currentMediaPlayer = mediaPlayer;

            // setting the function called after the end of song playback
            mediaPlayer.setOnEndOfMedia(() -> {
                // when the song finishes playing, delete the currently playing MediaPlayer object
                currentMediaPlayer = null;
            });

            // Play Music

            isPlaying = true;
            mediaPlayer.play();
            songText = "siema";
        } catch (MediaException e) {
            System.out.println("Nie można odtworzyć pliku: " + selectedSongPath);
        }
    }


    // Next song method
    public void playNextSong() {
        System.out.println(songNumber);
        if (songNumber < songPaths.size() - 1) {
            songNumber++;
        } else {
            songNumber = 0;
        }

        try {
            String selectedSongPath = songPaths.get(songNumber);
            playSong(selectedSongPath);
        } catch (MediaException e) {

            System.out.println(e.getMessage());
        }
    }


    // Previous song method
    public void playPreviousSong() {
        if (songNumber > 0) {
            songNumber--;
        } else {
            songNumber = songPaths.size() - 1;
        }

        try {
            String selectedSongPath = songPaths.get(songNumber);
            playSong(selectedSongPath);
        } catch (MediaException e) {
            System.out.println(e.getMessage());
        }
    }

    // Stop Music Method
    public void stopSong() {

        if (currentMediaPlayer != null) {
            try {
                currentMediaPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        // Remove object
                        currentMediaPlayer = null;
                    }
                });
                if (isPlaying) {
                    currentMediaPlayer.pause();
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    currentMediaPlayer.play();
                    mediaPlayer.play();
                    isPlaying = true;
                }
            } catch (NullPointerException e) {
                System.out.println("Błąd: " + e.getMessage());
            }
        } else {
            String selectedSongPath = songPaths.get(songNumber);
            playSong(selectedSongPath);
            isPlaying = true;
        }
    }

    public void setSongPaths(List<String> songPaths) {
        this.songPaths = songPaths;
        if (songPaths != null && !songPaths.isEmpty()) {
            String selectedSongPath = songPaths.get(0);

        }
    }



    // Check status of media player
    public boolean getMediaPlayerStatus() {
        return isPlaying;
    }

    public String getSongLabel() {
        return songText;
    }
}
