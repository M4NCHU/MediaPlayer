public class Song {
    private String title;
    private String artist;
    private String album;
    private String duration;

    public Song(String title, String artist, String album, String duration) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return title + " - " + artist + " [" + album + "] (" + duration + ")";
    }
}