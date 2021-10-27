package com.example.musicp03_1;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PlaySongActivity extends AppCompatActivity {

    // The attributes to store Song object attributes
    private String title = "";
    private String artist = "";
    private String filelink = "";
    private String imageLink;

    //to play the song
    private MediaPlayer player;
    private Song song;
    public final ArrayList<Song> songList = new ArrayList<Song>();
    private final ArrayList<Song> queue = new ArrayList<Song>();
    private final ArrayList<Song> history = new ArrayList<Song>();
    private int currentIndex = -1;
    private TextView nameOfList;


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
    private final Handler handler = new Handler();
    private TextView songEnd;
    private TextView songProgress;

    //for the volume seekbar
    private SeekBar volSeekBar;
    private int currentPos;
    private AudioManager audioManager;

    //for the mute function
    private boolean isMute;
    private int posBeforeMute;
    private ImageView volButton;

    //for fav
    public static ArrayList<Song> favorites = new ArrayList<Song>();
    private boolean addfav;
    private ImageView favButton;

    //to save in device
    private SharedPreferences sharedPreferences;

    public void displaySongBasedOnIndex(int selectedIndex) {

        //saving and accessing the properties of the song being played
        song = songList.get(selectedIndex);
        title = song.getTitle();
        artist = song.getArtist();
        filelink = song.getFileLink();
        imageLink = song.getImageLink();


        //changing title of the song by using id of the song
        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);

        //changing artist
        TextView txtArtist = findViewById(R.id.txtArtist);
        txtArtist.setText(artist);

        //changing cover image of the song
        ImageView iCoverArt = findViewById(R.id.imgCoverArt);
        Picasso.with(this).load(imageLink).into(iCoverArt);

        //setting our ui
        ImageView nextSong = findViewById(R.id.nextimgCoverArt);
        ImageView prevSong = findViewById(R.id.previmgCoverArt);

        //adding songs to be played to queue
        for (int i = selectedIndex + 1; i < songList.size(); i++) {

            queue.add(songList.get(i));

        }

        //adding songs to history(songs before song selected)
        for (int i = 0; i <= selectedIndex - 1; i++) {

            history.add(songList.get(i));

        }

        //checking if it should be visible
        if (history.size() == 0){

            CardView cardView = findViewById(R.id.prevcardView);
            cardView.setVisibility(View.INVISIBLE);

        }else{

            Picasso.with(this).load(songList.get(selectedIndex - 1).getImageLink()).into(prevSong);

        }

        if(queue.size() == 0){

            CardView cardView = findViewById(R.id.nextcardView);
            cardView.setVisibility(View.INVISIBLE);

        }else{

            Picasso.with(this).load(songList.get(selectedIndex + 1).getImageLink()).into(nextSong);

        }

        if(song.isFav()){

            favButton.setImageResource(R.drawable.ic_baseline_favorite_24);
            addfav = true;

        }else{

            favButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            addfav = false;

        }

    }

    public void displaySongBasedOnSong(Song songToBePlayed) {

        //saving and accessing the properties of the song being played
        song = songToBePlayed;
        title = song.getTitle();
        artist = song.getArtist();
        filelink = song.getFileLink();
        imageLink = song.getImageLink();


        //changing title of the song by using id of the song
        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);

        //changing artist
        TextView txtArtist = findViewById(R.id.txtArtist);
        txtArtist.setText(artist);

        //changing cover image of the song
        ImageView iCoverArt = findViewById(R.id.imgCoverArt);
        Picasso.with(this).load(imageLink).into(iCoverArt);

        ImageView nextSong = findViewById(R.id.nextimgCoverArt);

        ImageView prevSong = findViewById(R.id.previmgCoverArt);
        CardView prevCardView = findViewById(R.id.prevcardView);
        CardView nextCardView = findViewById(R.id.nextcardView);

        if (history.size() == 0){

            prevCardView.setVisibility(View.INVISIBLE);

        }else{

            prevCardView.setVisibility(View.VISIBLE);
            Picasso.with(this).load(history.get(history.size()-1).getImageLink()).into(prevSong);

        }

        if(queue.size() == 0){

            nextCardView.setVisibility(View.INVISIBLE);

        }else{

            nextCardView.setVisibility(View.VISIBLE);
            Picasso.with(this).load(queue.get(0).getImageLink()).into(nextSong);

        }

        if(song.isFav()){

            favButton.setImageResource(R.drawable.ic_baseline_favorite_24);
            addfav = true;

        }else{

            favButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            addfav = false;

        }

    }

    public void playSong(String songUrl) {

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

    public void gracefullyStopWhenMusicEnds(){

        //checking whether player is over
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            //what is to happen when song is over
            @Override
            public void onCompletion(MediaPlayer mp) {

                if(!shouldLoop){

                    //to make the song loop
                    playOrPauseMusic(btnPlayPause);

                }else{

                    if (currentIndex == songList.size() - 1) {

                        //set the button to original
                        btnPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                        //back to home screen
                        onBackPressed();

                    }else{

                        //playing next music when song is over
                        nextMusic(btnPlayPause);

                    }

                }

            }

        });

    }

    public void nextMusic(View view) {

        //checking index to ensure change is within the array indexes
        if (queue.size() == 0) {

            player.stop();
            seekBar.setProgress(seekBar.getMax());
            btnPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            onBackPressed();

        } else {

            //stoping the player and changing button to pause
            player.stop();
            btnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);

            //adding song which was playing just now to history
            history.add(song);

            //displaying and playing song while removing the first song of queue
            displaySongBasedOnSong(queue.remove(0));
            playSong(filelink);

            /*Log.d("poly","queue: " + queue.size() + "");
            Log.d("poly","history: " + history.size() + "");*/

        }

    }

    public void prevImgMusic(View view){

        //displaying and playing song while removing the first song of queue
        player.stop();
        btnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);
        queue.add(0,song);
        displaySongBasedOnSong(history.remove(history.size() - 1));
        playSong(filelink);

    }

    public void prevMusic(View view) {

        if(player.getCurrentPosition() > 5000 || history.size() == 0){

            //resetting the song playing
            player.seekTo(0);

        }else{

            //displaying and playing song while removing the first song of queue
            player.stop();
            btnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);
            queue.add(0,song);
            displaySongBasedOnSong(history.remove(history.size() - 1));
            playSong(filelink);

            /*Log.d("poly","queue: " + queue.size() + "");
            Log.d("poly","history: " + history.size() + "");*/



        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();


        //to ensure the player stops and resets everything to ensure no app crash
        handler.removeCallbacks(sBar);
        player.stop();

    }

    public void backButton(View view){

        //we can use onBackPressed as it it the same method
        onBackPressed();

    }

    public void playOrPauseMusic(View view) {

        //to check if player is playing
        if (player.isPlaying()) {

            player.pause();

            //changing text on the button
            btnPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);

        } else if (!player.isPlaying()) {

            player.start();

            //changing text on the button
            btnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);

        }
    }

    public void shuffleSong(View view){

        //creating arraylist to be shuffled
        ArrayList<Song> shuffleList = new ArrayList<Song>();
        shuffleList.addAll(songList);
        shuffleList.remove(song);

        if(shouldShuffle){

            //changing the button to show change
            btnShuffle.setImageResource(R.drawable.ic_baseline_shuffle_on_24);

            //making the list all shuffled up
            for (int i = 0; i < shuffleList.size(); i++) {

                //making random index
                int index = (int) (Math.random() * shuffleList.size());

                //holding the song going to be replaced in a temporary variable
                Song tempHolder = shuffleList.get(i);

                //replacing the song in index to song in i'
                shuffleList.set(i,shuffleList.get(index));

                //replacing the duplicate song with the song which was replaced
                shuffleList.set(index, tempHolder);

            }

            //clearing queue and history
            queue.clear();
            history.clear();

            //adding the elements of shuffleList to queue
            queue.addAll(shuffleList);

            //to change the display for next and prev song
            displaySongBasedOnSong(song);

            /*for (int i = 0; i < queue.size(); i++) {
                Log.d("poly", queue.get(i).getTitle());
            }

            Log.d("poly","queue: " + queue.size() + "");*/

        }else{

            //changing the button imagej
            btnShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);

            //changing the queue to normal
            queue.clear();
            for (int i = songList.indexOf(song) + 1; i < songList.size(); i++) {
                queue.add(songList.get(i));
            }

            //changing the history to normal
            history.clear();
            for (int i = 0; i < songList.indexOf(song); i++) {
                history.add(songList.get(i));
            }

            //to change the display for next and prev song
            displaySongBasedOnSong(song);

            /*for (int i = 0; i < queue.size(); i++) {
                Log.d("poly", queue.get(i).getTitle());
            }

            Log.d("poly","queue: " + queue.size() + "");*/

        }

        //ensure we can change modes
        shouldShuffle = !shouldShuffle;

    }

    public void loopSong(View view){

        //checking if we should loop or not
        if (shouldLoop) {

            //changing button
            btnLoop.setImageResource(R.drawable.ic_baseline_repeat_on_24);

        }else{

            //changing button to original
            btnLoop.setImageResource(R.drawable.ic_baseline_repeat_24);

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

    private final Runnable sBar = new Runnable() {

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

    public void songSeekBar(){

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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

        });

    }

    public void audioSeekBar(){

        //creating audiomanager object which takes in audio service as a parameter
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //setting up the seekbar
        volSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

        volSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                isMute = false;
                volButton.setImageResource(R.drawable.ic_baseline_volume_mute_24);

                //changing audio with respect to change in seekbar
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

                //saving the audio using sharedpreferences
                currentPos = progress;
                saveToSharedPreferences(currentPos,"audio");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        //making sure we can have a default seekbar position
        if(currentPos != -1){

            //making sure we set the audio as what we changed to before
            volSeekBar.setProgress(currentPos);

        }else{

            //setting our default position
            volSeekBar.setProgress(volSeekBar.getMax());

        }
    }

    public void mute(View view){
        //checking which toggle function to use
        if (!isMute) {

            //setting our image
            volButton.setImageResource(R.drawable.ic_baseline_volume_off_24);

            //saving our progress
            posBeforeMute = volSeekBar.getProgress();
            /*volSeekBar.setProgress(0);*/

            //muting our audio
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);

        }else{

            //changing our image
            volButton.setImageResource(R.drawable.ic_baseline_volume_mute_24);

            //making our audio not mute
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volSeekBar.getProgress(),0);

        }

        //ensure we can toggle again
        isMute = !isMute;

        //saving to sharedpref
        saveToSharedPreferences(posBeforeMute, "audioBefMute");
        saveToSharedPreferences(isMute,"toMute");

    }

    public void addToFav(View view) {

        //ensuring we can toggle
        if (!addfav){

            favButton.setImageResource(R.drawable.ic_baseline_favorite_24);
            int index = songList.indexOf(song);
            songList.get(index).setFav(true);
            favorites.add(songList.get(index));

        }else{

            favButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            int index = songList.indexOf(song);
            songList.get(index).setFav(false);
            favorites.remove(songList.get(index));

        }

        addfav = !addfav;
        Gson gson = new Gson();
        String songListToSend = gson.toJson(HomePageActivity.songList);
        String dataToBeSent = gson.toJson(favorites);

        //saving to shared pref
        saveToSharedPreferences(dataToBeSent,"favList");
        saveToSharedPreferences(songListToSend,"songList");

    }

    public void saveToSharedPreferences(int toBeSaved, String key){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,toBeSaved);
        editor.apply();

    }

    public void saveToSharedPreferences(boolean toBeSaved, String key){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,toBeSaved);
        editor.apply();

    }

    public void saveToSharedPreferences(String toBeSaved, String key){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,toBeSaved);
        editor.apply();

    }

    public void sendDataToHome(View view){

        //to ensure the player stops and resets everything to ensure no app crash
        handler.removeCallbacks(sBar);
        player.stop();
        Intent intent = new Intent(this,HomePageActivity.class);
        startActivity(intent);

    }

    public void sendDataToSearch(View view){

        handler.removeCallbacks(sBar);
        player.stop();
        Intent intent = new Intent(this,SearchPageActivity.class);
        startActivity(intent);

    }

    public void sendDataToFav(View view){

        handler.removeCallbacks(sBar);
        player.stop();
        Intent intent = new Intent(this,FavPageActivity.class);
        startActivity(intent);

    }

    public void sendDataToLib(View view){

        handler.removeCallbacks(sBar);
        player.stop();
        Intent intent = new Intent(this,LibPageActivity.class);
        intent.putExtra("playlist", "");
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        getSupportActionBar().hide();


        //getting the index in array from home page activity
        Bundle songData = this.getIntent().getExtras();
        currentIndex = songData.getInt("index");

        String name = songData.getString("name");

        //getting the songList
        Gson gson = new Gson();
        TypeToken<ArrayList<Song>> token = new TypeToken<ArrayList<Song>>(){};
        songList.addAll(gson.fromJson(songData.getString("songList"),token.getType()));
        for (int i = 0; i < songList.size(); i++) {
            Log.d("poly", songList.get(i).getTitle());
        }
        //songList.addAll(HomePageActivity.songList);

        //setting sharedpreferences settings
        sharedPreferences = getSharedPreferences("memory", MODE_PRIVATE);

        //getting data from sharedPreferences
        currentPos = sharedPreferences.getInt("audio", -1);
        posBeforeMute = sharedPreferences.getInt("audioBefMute",-1);
        isMute = sharedPreferences.getBoolean("toMute",false);

        //updating the variables with the widget they are supposed to be
        btnPlayPause = findViewById(R.id.btnPlayPause);
        songProgress = findViewById(R.id.songProgress);
        songEnd = findViewById(R.id.songDuration);
        seekBar = findViewById(R.id.songSeekBar);
        btnShuffle = findViewById(R.id.btnShuffle);
        btnLoop = findViewById(R.id.btnLoop);
        volSeekBar = findViewById(R.id.volSeekBar);
        volButton = findViewById(R.id.muter);
        favButton = findViewById(R.id.heart);
        nameOfList = findViewById(R.id.nameOfList);

        //activating the methods to play the song and update the display
        displaySongBasedOnIndex(currentIndex);
        playSong(filelink);
        nameOfList.setText(name);

        //executing method to be able to drag the volume seekbar
        audioSeekBar();
        
        //creating method to be able to drag the seekbar
        songSeekBar();

    }

}
