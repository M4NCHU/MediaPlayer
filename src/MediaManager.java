import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.util.*;

public class MediaManager {
    private List<String> songPaths;
    private MediaPlayer mediaPlayer;
    private int songNumber = 0;
    private boolean isPlaying = false;
    private String songText = "";
    private JFXPanel fxPanel;
    private JProgressBar songProgressBar;
    private Timer timer;
    private TimerTask task;
    private Media media;
    private JLabel remainingTimeLabel;
    private JLabel totalTimeLabel;


    long remainingMinutes;
    long remainingSeconds;

    public MediaManager(List<String> songsList) {
        songPaths = songsList;
        fxPanel = new JFXPanel();
    }

    public void playSong(String selectedSongPath) {
        Thread thread = new Thread(() -> {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }

                media = new Media(new File(selectedSongPath).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                mediaPlayer.setOnEndOfMedia(() -> {
                    stopSong();
                });

                isPlaying = true;
                mediaPlayer.play();
                songText = selectedSongPath;

                getSongProgressPercentage();

                startTimerTask();

            } catch (MediaException e) {
                System.out.println("Nie można odtworzyć pliku: " + selectedSongPath);
            }
        });

        thread.start();
    }


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
                remainingTimeLabel.setText(formatTime(getRemainingTime()));
                totalTimeLabel.setText(formatTime(getTotalTime()));
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void updateProgressBar() {
        if (mediaPlayer != null && songProgressBar != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration totalTime = mediaPlayer.getTotalDuration();

            double progress = currentTime.toSeconds() / totalTime.toSeconds();
            int percentage = (int) (progress * 100);

            // Obliczanie czasu pozostałego do końca utworu
            Duration remainingTime = totalTime.subtract(currentTime);
            remainingMinutes = (long) remainingTime.toMinutes();
            remainingSeconds = (long) remainingTime.toSeconds() % 60;
            System.out.println(remainingSeconds);

            // Obliczanie całkowitej długości utworu
            long totalMinutes = (long) totalTime.toMinutes();
            long totalSeconds = (long) totalTime.toSeconds() % 60;

            String remainingTimeFormatted = String.format("%02d:%02d", remainingMinutes, remainingSeconds);
            String totalTimeFormatted = String.format("%02d:%02d", totalMinutes, totalSeconds);


            long currentMinutes = (long) currentTime.toMinutes();
            long currentSeconds = (long) currentTime.toSeconds() % 60;
            String currentTimeFormatted = String.format("%02d:%02d", currentMinutes, currentSeconds);


            songProgressBar.setValue(percentage);
            songProgressBar.setForeground(Color.BLACK);
            songProgressBar.setString(currentTimeFormatted + " / " + totalTimeFormatted);



        }
    }

    public int getRemainingTime() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            Duration remaining = mediaPlayer.getTotalDuration().subtract(mediaPlayer.getCurrentTime());
            return (int) remaining.toSeconds();
        }
        return 0;
    }

    public int getTotalTime() {
        if (mediaPlayer != null) {
            Duration totalDuration = mediaPlayer.getTotalDuration();
            return (int) totalDuration.toSeconds();
        }
        return 0;
    }


    public void getSongProgressPercentage() {
        if (mediaPlayer != null && songProgressBar != null) {
            double progress = mediaPlayer.getCurrentTime().toSeconds() /
                    mediaPlayer.getTotalDuration().toSeconds();
            int percentage = (int) (progress * 100);
            songProgressBar.setValue(percentage);
        }
    }

    public void setSongProgressBar(JProgressBar progressBar) {
        songProgressBar = progressBar;
    }

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

    public void stopSong() {
        if (mediaPlayer != null) {
            try {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
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

    public String timeRemaining(int timeInSeconds) {
        long minutes = remainingMinutes;
        long seconds = remainingSeconds;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String formatTime(int timeInSeconds) {
        long minutes = remainingMinutes;
        long seconds = remainingSeconds;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public boolean getMediaPlayerStatus() {
        return isPlaying;
    }

    public String getSongLabel() {
        return songText;
    }
}
