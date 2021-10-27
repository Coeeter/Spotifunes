package com.example.musicp03_1;

import java.util.ArrayList;

public class Playlist {

    //attributes of our class
    public String name;
    public String imageLink;
    public ArrayList<Song> playlist = new ArrayList<Song>();

    //constructor
    public Playlist(String name, ArrayList<Song> playlist) {

        //giving the attributes values
        this.name = name;
        this.playlist.addAll(playlist);
        this.imageLink = this.playlist.get((int) (Math.random() * this.playlist.size())).getImageLink();

    }

}
