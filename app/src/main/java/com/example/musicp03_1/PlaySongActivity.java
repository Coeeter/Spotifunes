package com.example.musicp03_1;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.VolumeProvider;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class PlaySongActivity extends AppCompatActivity {

    // The attributes to store Song object attributes
    private String title = "";
    private String artist = "";
    private String filelink = "";
    private int resid;
    private int drawable;
    private String id;

    //to play the song
    private MediaPlayer player;
    private SongCollection songCollection = new SongCollection();
    private int currentIndex = -1;


    //for th buttons
    private ImageButton btnPlayPause;
    private ImageButton btnShuffle;
    private ImageButton btnLoop;

    //for the loop
    private boolean shouldLoop = true;

    //for the shuffle
    private boolean shouldShuffle = true;

    //for the seekbar
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private TextView songEnd;
    private TextView songProgress;

    //for the volume seekbar
    private SeekBar volSeekBar;
    private int audioChange = 0;

    //for the mute function
    private boolean isMute = false;
    private int currentPos;

    public void displaySongBasedOnIndex(int selectedIndex) {

        //saving and accessing the properties of the song being played
        Song song = songCollection.getCurrentSong(selectedIndex);
        title = song.getTitle();
        artist = song.getArtist();
        filelink = song.getFileLink();
        drawable = song.getDrawable();
        resid = song.getResid();
        id = song.getId();

        //changing title of the song by using id of the song
        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);

        //changing artist
        TextView txtArtist = findViewById(R.id.txtArtist);
        txtArtist.setText(artist);

        //changing cover image of the song
        ImageView iCoverArt = findViewById(R.id.imgCoverArt);
        iCoverArt.setImageResource(drawable);

    }

    public void playSong(String songUrl) {

        //to decide which player to use
        if (songUrl.equals("")) {

            //creating player to play .wav files
            player = MediaPlayer.create(this, resid);
            player.start();

            //methods to improve the experience
            updateSeekBar(sBar);
            gracefullyStopWhenMusicEnds();

        } else {

            try {
                //creating player to play from online source
                player = new MediaPlayer();
                player.reset();
                player.setDataSource(songUrl);
                player.prepare();
                player.start();

                //methods to improve the experience
                updateSeekBar(sBar);
                gracefullyStopWhenMusicEnds();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        setTitle(title);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        //to ensure the player stops and resets everything to ensure no app crash
        handler.removeCallbacks(sBar);
        player.stop();

        //to remember the audio settings
        currentPos = volSeekBar.getProgress();

    }

    public void backButton(View view){
        onBackPressed();
    }

    public void mute(View view){
        if (!isMute) {
            currentPos = volSeekBar.getProgress();
            volSeekBar.setProgress(0);
        }else{
            volSeekBar.setProgress(currentPos);
        }
        isMute = !isMute;
    }

    public void playOrPauseMusic(View view) {

        //to check if player is playing
        if (player.isPlaying()) {

            player.pause();

            //changing text on the button
            btnPlayPause.setImageResource(R.drawable.play_button);

        } else if (!player.isPlaying()) {

            player.start();

            //changing text on the button
            btnPlayPause.setImageResource(R.drawable.pause_button);

        }
    }

    public void nextMusic(View view) {

        //checking index to ensure change is within the array indexes
        if (currentIndex == songCollection.getSongs().length - 1) {

            player.seekTo(player.getDuration());
            seekBar.setProgress(player.getDuration());
            btnPlayPause.setImageResource(R.drawable.play_button);
            onBackPressed();

        } else {

            currentIndex++;

        }

        //playing the next song with the new index and updating the button
        player.stop();
        btnPlayPause.setImageResource(R.drawable.pause_button);
        displaySongBasedOnIndex(currentIndex);
        playSong(filelink);

    }

    public void prevMusic(View view) {

        if(player.getCurrentPosition() > 5000){

            //resetting the song playing
            player.seekTo(0);

        }else{

            //checking index to ensure change is within array indexes
            if (!(currentIndex == 0)){

                currentIndex--;

            }

            //playing the previous song with the new index and updating the button
            player.stop();
            btnPlayPause.setImageResource(R.drawable.pause_button);
            displaySongBasedOnIndex(currentIndex);
            playSong(filelink);

        }

    }

    public void gracefullyStopWhenMusicEnds(){

        //checking whether player is over
        player.setOnCompletionListener(

                new MediaPlayer.OnCompletionListener() {

                    //what is to happen when song is over
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        if(!shouldLoop){

                            //to make the song loop
                            playOrPauseMusic(btnPlayPause);

                        }else{

                            if (currentIndex == songCollection.getSongs().length - 1) {

                                //set the button to original
                                btnPlayPause.setImageResource(R.drawable.play_button);

                                //back to home screen
                                onBackPressed();

                            }else{

                                //playing next music when song is over
                                nextMusic(btnPlayPause);

                            }

                        }

                    }

                }

        );

    }

    public void shuffleSong(View view){
        //Creating list to shuffle it
        List<Song> shuffleList = new ArrayList<>(Arrays.asList(songCollection.songs));

        if(shouldShuffle){

            //changing the button to show change
            btnShuffle.setImageResource(R.drawable.shuffle_on);

            //making the list all shuffled up
            Collections.shuffle(shuffleList);

            //finding index of the current song
            int firstValIndex = shuffleList.indexOf(songCollection.getCurrentSong(currentIndex));

            //finding first element
            Song songInFirstIndex = shuffleList.get(0);

            //making the current song to be the first element
            shuffleList.set(0, shuffleList.get(firstValIndex));

            //making originally first element take over old pos of current song
            shuffleList.set(firstValIndex, songInFirstIndex);

            //making list into array and updating the currentIndex
            shuffleList.toArray(songCollection.songs);
            currentIndex = 0;

        }else{

            //changing the button imagej
            btnShuffle.setImageResource(R.drawable.shuffle_off);


            //making the array the same again
            songCollection = new SongCollection();

        }

        //ensure we can change modes
        shouldShuffle = !shouldShuffle;

    }

    public void loopSong(View view){

        //checking if we should loop or not
        if (shouldLoop) {

            //changing button
            btnLoop.setImageResource(R.drawable.loop_on);

        }else{

            //changing button to original
            btnLoop.setImageResource(R.drawable.loop_off);

        }

        //ensure we can loop again
        shouldLoop = !shouldLoop;
    }

    public void updateSeekBar(Runnable runnable) {

        //to reset the handler
        handler.removeCallbacks(runnable);

        //to ensure the run method is called 10 ms later
        handler.postDelayed(runnable, 10);

    }

    private Runnable sBar = new Runnable() {

        //updating the run() method to suit the app
        @Override
        public void run() {

            //ensure we only use the method when player is playing
            if (player.isPlaying() && player != null) {

                //setting up the seekBar progress
                seekBar.setMax(player.getDuration());
                seekBar.setProgress(player.getCurrentPosition());

                //setting text on the end of the seekBar
                songEnd.setText(timeFormat(player.getDuration()));

                //updating text on the start of the seekBar
                songProgress.setText(timeFormat(player.getCurrentPosition()));

            }

            //ensure this method is always being called on with a delay of 10 ms
            handler.postDelayed(this,10);

        }

    };

    public String timeFormat(int length){

        //getting the minute and second at a particular time
        int minute = length / 60000;
        int second = (length / 1000) % 60;

        //putting minute in time var
        String time = minute + ":";

        //checking if second is in correct format
        if(second < 10){

            //making the time be like = (time):0(second)
            time += "0" + second;

        }else{

            //making the time be like = (tiem):(second)
            time += second;

        }

        return time;
    }

    public void audioSeekBar(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                isMute = false;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(audioChange == 0){
            volSeekBar.setProgress(volSeekBar.getMax());
            audioChange++;
        }else{
            volSeekBar.setProgress(currentPos);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        getSupportActionBar().hide();

        //getting the index in array from main activity
        Bundle songData = this.getIntent().getExtras();
        currentIndex = songData.getInt("index");
        Log.d("poly", "The index of the array is " + currentIndex);

        //updating the variables with the widget they are supposed to be
        btnPlayPause = findViewById(R.id.btnPlayPause);
        songProgress = findViewById(R.id.songProgress);
        songEnd = findViewById(R.id.songDuration);
        seekBar = findViewById(R.id.songSeekBar);
        btnShuffle = findViewById(R.id.btnShuffle);
        btnLoop = findViewById(R.id.btnLoop);
        volSeekBar = findViewById(R.id.volSeekBar);

        //activating the methods to play the song and update the display
        displaySongBasedOnIndex(currentIndex);
        playSong(filelink);

        //creating method to be able to drag the volume seekbar
        audioSeekBar();
        
        //creating method to be able to drag the seekbar
        seekBar.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        //checking player is active
                        if (player != null) {

                            //setting the change in progress to the player
                            player.seekTo(seekBar.getProgress());

                        }

                    }

                }

        );


    }

}
