package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class PlaySongActivity extends AppCompatActivity {
    // The attributes to store Song object attributes
    private String title = "";
    private String artist = "";
    private String filelink = "";
    private int resid;
    private int drawable;

    //to play the song
    private MediaPlayer mp;
    private MediaPlayer player = new MediaPlayer();
    private SongCollection songCollection = new SongCollection();
    private int currentIndex = -1;
    private int playWhat;

    //for th buttons
    private Button btnPlayPause;

    //for the seekbar
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private TextView songEnd;
    private TextView songProgress;

    public void displaySongBasedOnIndex(int selectedIndex) {
        //saving and accessing the properties of the song being played
        Song song = songCollection.getCurrentSong(selectedIndex);
        title = song.getTitle();
        artist = song.getArtist();
        filelink = song.getFileLink();
        drawable = song.getDrawable();
        resid = song.getResid();

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

    public int playSong(String songUrl) {
        if (songUrl.equals("")) {
            mp = MediaPlayer.create(this, resid);
            mp.start();
            handrun();
            gracefullyStopWhenMusicEnds();
            return 1;
        } else {
            try {
                player.reset();
                player.setDataSource(songUrl);
                player.prepare();
                player.start();
                handrun();
                gracefullyStopWhenMusicEnds();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (playWhat == 1) {
            mp.stop();
        } else {
            player.stop();
        }
    }

    public void playOrPauseMusic(View view) {
        if (playWhat == 1) {
            if (mp.isPlaying()) {
                mp.pause();

                btnPlayPause.setText("PLAY");
            } else if (!mp.isPlaying()) {
                mp.start();

                btnPlayPause.setText("PAUSE");
            }
        } else {
            if (player.isPlaying()) {
                player.pause();

                btnPlayPause.setText("PLAY");
            } else if (!player.isPlaying()) {
                player.start();

                btnPlayPause.setText("PAUSE");
            }
        }
    }

    public void nextMusic(View view) {
        if (currentIndex == songCollection.getSongs().length - 1) {
            currentIndex -= currentIndex;
        } else {
            currentIndex++;
        }
        if (playWhat == 1) {
            mp.stop();
        } else {
            player.stop();
        }
        displaySongBasedOnIndex(currentIndex);
        playWhat = playSong(filelink);
    }

    public void prevMusic(View view) {
        if (currentIndex == 0){
            currentIndex += songCollection.getSongs().length -1;
        }else{
            currentIndex--;
        }
        if(playWhat == 1){
            mp.stop();
        }else{
            player.stop();
        }
        displaySongBasedOnIndex(currentIndex);
        playWhat = playSong(filelink);
    }

    public void gracefullyStopWhenMusicEnds(){
        if(playWhat == 1){
            whenMusicEnds(mp);
        }else{
            whenMusicEnds(player);
        }
    }

    public void whenMusicEnds(MediaPlayer mediaPlayer){
        mediaPlayer.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Toast.makeText(getBaseContext(), "The song has ended and the onCompleteListener is activated",Toast.LENGTH_LONG).show();
                        nextMusic(btnPlayPause);
                    }
                }
        );
    }


    Runnable run = new Runnable() {
        @Override
        public void run() {

            //to ensure we using the correct
            if (playWhat == 1 && mp.isPlaying() && mp != null) {
                //setting up the seekBar progress
                seekBar.setMax(mp.getDuration());
                seekBar.setProgress(mp.getCurrentPosition());

                //setting text on the end of the seekBar
                double songDuration = Math.round(mp.getDuration()/60000.00 * 100.0)/100.0;
                String endText = ("" + songDuration).length() <= 3?("" + songDuration + "0"):("" + songDuration);
                songEnd.setText(endText);

                //updating text on the start of the seekBar
                double soFarHowLong = Math.round(mp.getCurrentPosition()/60000.0 * 100.0)/100.0;
                String startText = ("" + soFarHowLong).length() <= 3?("" + soFarHowLong + "0"):("" + soFarHowLong);
                songProgress.setText(startText);
            }else if (playWhat == 0 && player.isPlaying() && player != null) {
                //setting up the seekBar progress
                seekBar.setMax(player.getDuration());
                seekBar.setProgress(player.getCurrentPosition());

                //setting text on the end of the seekBar
                double songDuration = Math.round(player.getDuration()/60000.00 * 100.0)/100.0;
                String endText = ("" + songDuration).length() <= 3?("" + songDuration + "0"):("" + songDuration);
                songEnd.setText(endText);

                //updating text on the start of the seekBar
                double soFarHowLong = Math.round(player.getCurrentPosition()/60000.0 * 100.0)/100.0;
                String startText = ("" + soFarHowLong).length() <= 3?("" + soFarHowLong + "0"):("" + soFarHowLong);
                songProgress.setText(startText);
            }
            handler.postDelayed(this,10);
        }
    };

    public void handrun() {
        handler.removeCallbacks(run);
        handler.postDelayed(run, 10);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        //getting the index in array
        Bundle songData = this.getIntent().getExtras();
        currentIndex = songData.getInt("index");
        Log.d("poly", "The index of the array is " + currentIndex);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        songProgress = findViewById(R.id.songProgress);
        songEnd = findViewById(R.id.songDuration);
        seekBar = findViewById(R.id.songSeekBar);
        displaySongBasedOnIndex(currentIndex);
        playWhat = playSong(filelink);
    }
}
