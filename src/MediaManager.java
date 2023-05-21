import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MediaManager {
    List<String> songPaths;
    private MediaPlayer currentMediaPlayer;
    private MediaPlayer mediaPlayer;
    private int songNumber = 0;
    private boolean isPlaying = false;
    private String songText = "";
    private JFXPanel fxPanel;
    private JProgressBar songProgressBar;
    private Timer timer;
    private TimerTask task;
    private Media media;



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
            songText = selectedSongPath;

            getSongProgressPercentage();

            startTimerTask();

        } catch (MediaException e) {
            System.out.println("Nie można odtworzyć pliku: " + selectedSongPath);
        }
    }

    // Start the timer task to update the progress bar
    private void startTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }



        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                updateProgressBar();
            }
        };

        // Schedule the task to run every second
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    // Update the progress bar based on the current playback time
    public void updateProgressBar() {
        if (currentMediaPlayer != null && songProgressBar != null) {
            double progress = currentMediaPlayer.getCurrentTime().toSeconds() /
                    currentMediaPlayer.getTotalDuration().toSeconds();
            int percentage = (int) (progress * 100);

            songProgressBar.setValue(percentage);
            System.out.println("Progress: " + percentage + "%");
        }
    }

    public void  getSongProgressPercentage() {
        if (currentMediaPlayer != null && songProgressBar != null) {
            double progress = currentMediaPlayer.getCurrentTime().toSeconds() /
                    currentMediaPlayer.getTotalDuration().toSeconds();
            int percentage = (int) (progress * 100);
            songProgressBar.setValue(percentage);

        }

    }


    public void setSongProgressBar(JProgressBar progressBar) {
        songProgressBar = progressBar;
    }





    // Next song method
    public void playNextSong() {

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
