package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaySongActivity extends AppCompatActivity {
    // The attributes to store Song object attributes
    private String title = "";
    private String artist = "";
    private String filelink = "";
    private int drawable;
    private int currentIndex = -1;

    //to play the song
    private MediaPlayer player = new MediaPlayer();
    private Button btnPlayPause = null;
    private SongCollection songCollection = new SongCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        //getting the index in array
        Bundle songData = this.getIntent().getExtras();
        currentIndex = songData.getInt("index");
        Log.d("poly","The index of the array is " + currentIndex);
        displaySongBasedOnIndex(currentIndex);
    }

    public void displaySongBasedOnIndex(int selectedIndex){
        //saving and accessing the properties of the song being played
        Song song = songCollection.getCurrentSong(selectedIndex);
        title = song.getTitle();
        artist = song.getArtist();
        filelink = song.getFileLink();
        drawable = song.getDrawable();
        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);
        TextView txtArtist = findViewById(R.id.txtArtist);
        txtArtist.setText(artist);
        ImageView iCoverArt = findViewById(R.id.imgCoverArt);
        iCoverArt.setImageResource(drawable);
    }
}