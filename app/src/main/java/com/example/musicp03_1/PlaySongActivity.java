package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private SongCollection songCollection = new SongCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        //getting the index in array
        Bundle songData = this.getIntent().getExtras();
        currentIndex = songData.getInt("index");
        Log.d("poly", "The index of the array is " + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        playSong(filelink);
    }

    public void displaySongBasedOnIndex(int selectedIndex) {
        //saving and accessing the properties of the song being played
        Song song = songCollection.getCurrentSong(selectedIndex);
        title = song.getTitle();
        artist = song.getArtist();
        filelink = song.getFileLink();
        drawable = song.getDrawable();

        //changing title of the song by using id of the song
        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);

        //changing artist
        TextView txtArtist = findViewById(R.id.txtArtist);
        txtArtist.setText(artist);

        //changing image
        ImageView iCoverArt = findViewById(R.id.imgCoverArt);
        iCoverArt.setImageResource(drawable);

    }

    public void playSong(String songUrl) {
        try {
            player.reset();
            player.setDataSource(songUrl);
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.release();
    }

    public void playOrPauseMusic(View view) {
        Button btnPlayPause = findViewById(R.id.btnPlayPause);
        if (player.isPlaying()) {
            player.pause();

            btnPlayPause.setText("PLAY");
        } else if (!player.isPlaying()) {
            player.start();

            btnPlayPause.setText("PAUSE");
        }


    }
}