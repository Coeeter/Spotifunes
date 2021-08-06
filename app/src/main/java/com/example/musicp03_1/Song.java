package com.example.musicp03_1;

public class Song {

    // the atributes
    private String id;
    private String title;
    private String artist;
    private String fileLInk;
    private String imageLink;
    public int resid;

    //constructor
    public Song(String id, String title, String artist, String fileLink, String imageLink) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.fileLInk = fileLink;
        this.imageLink = imageLink;
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
        return fileLInk;
    }

    public String getImageLink() {
        return imageLink;
    }

    public int getResid() {
        if(!(resid == 0)) {
            return resid;
        }
        return -1;
    }
}
