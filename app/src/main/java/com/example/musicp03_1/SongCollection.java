package com.example.musicp03_1;

import android.view.GestureDetector;

public class SongCollection {

    // making the song list
    private Song[] songs = new Song[4];

    // constructor method
    public SongCollection() {

        // Our song objects
        Song theWayYouLookTonight = new Song(
                "S1001",
                "The Way You Look Tonight",
                "Michael Buble",
                "https://p.scdn.co/mp3-preview/a5b8972e764025020625bbf9c1c2bbb06e394a60?cid=2afe87a64b0042dabf51f37318616965",
                4.66,
                R.drawable.michael_buble_collection
        );
        Song billieJean = new Song(
                "S1002",
                "Billie Jean",
                "Michael Jackson",
                "https://p.scdn.co/mp3-preview/f504e6b8e037771318656394f532dede4f9bcaea?cid=2afe87a64b0042dabf51f37318616965",
                4.99,
                R.drawable.billie_jean
        );
        Song one = new Song(
                "S1003",
                "One",
                "Ed Sheeran",
                "https://p.scdn.co/mp3-preview/daa8679253ba20620db6e1db9c88edfcf1f4069f?cid=2afe87a64b0042dabf51f37318616965",
                4.21,
                R.drawable.photograph
        );
        Song unravel = new Song(
                "S1004",
                "Unravel",
                "TK from Ling tosite sigure",
                "https://p.scdn.co/mp3-preview/7b56a38dfc7ae953db793b2dfd4ec110828a0455?cid=2afe87a64b0042dabf51f37318616965",
                //"https://soundcloud.com/marcobofficial/tky-ghl-unravel-marco-b-remix",
                //"https://www.youtube.com/watch?v=zmzPhm9wpdQ",
                //"C:\\Users\\nasru\\Music\\Anime Music\\Unravel.mp3",
                4.01,
                R.drawable.unravel);

        //inputting into the list
        songs[0] = theWayYouLookTonight;
        songs[1] = billieJean;
        songs[2] = one;
        songs[3] = unravel;
    }

    // method to find Song instance in songs     list(array)
    public int searchSongById(String id) {
        for (int i = 0; i < songs.length; i++) {
            Song tempSong = songs[i];
            if (tempSong.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public Song getCurrentSong(int currentSongId){
        return songs[currentSongId];
    }
}
