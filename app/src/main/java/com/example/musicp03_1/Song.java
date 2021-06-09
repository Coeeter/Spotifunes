package com.example.musicp03_1;

public class Song {

    // the atributes
    private String id;
    private String title;
    private String artist;
    private String fileLink;
    private double songLength;
    private int drawable;
    public int resid;

    //constructor
    public Song(String id, String title, String artist, String fileLink, double songLength, int drawable) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.fileLink = fileLink;
        this.songLength = songLength;
        this.drawable = drawable;
    }

    //getter methods
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFileLink() {
        return fileLink;
    }

    public double getSongLength() {
        return songLength;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getResid() {
        if(!(resid == 0)) {
            return resid;
        }
        return -1;
    }
}
