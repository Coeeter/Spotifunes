package com.example.musicp03_1;

public class Song {

    // the atributes
    private String id;
    private String title;
    private String artist;
    private String fileLink;
    private double songLength;
    private int drawable;

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

    // setter methods
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public void setSongLength(double songLength) {
        this.songLength = songLength;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
