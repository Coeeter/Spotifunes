package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;

public class PlaylistLibPageActivity extends AppCompatActivity {

    private RecyclerView list;
    private PlaylistAddSongAdapter playlistAddSongAdapter;
    private EditText nameOfPlaylist;
    private static int i = 1;
    private SharedPreferences sharedPreferences;

    //methods to send our data
    public void sendDataToHome(View view){

        Intent intent = new Intent(this,HomePageActivity.class);
        startActivity(intent);

    }

    public void sendDataToSearch(View view){

        Intent intent = new Intent(this,SearchPageActivity.class);
        startActivity(intent);

    }

    public void sendDataToFav(View view){

        Intent intent = new Intent(this,FavPageActivity.class);
        startActivity(intent);

    }

    public void back(View view){

        //ensure we can go prev activity
        onBackPressed();

    }

    public void createPlaylist(View view){

        //creating playlist name
        String name;

        //checking if got user input
        if(nameOfPlaylist.getText().toString().isEmpty()){

            //default naming style
            name = "My playlist " + i;
            i++;

            //saving int to sharedpref so can rmbr the number
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("integar", i);
            editor.apply();

        }else{

            //setting name to user input
            name = nameOfPlaylist.getText().toString();

        }

        //creating the playlist
        Playlist playlist = new Playlist(name, playlistAddSongAdapter.playlistArray);

        //creating intent
        Intent intent = new Intent(this, LibPageActivity.class);

        //converting to string
        Gson gson = new Gson();
        String playlistToString = gson.toJson(playlist);

        //passing in data and starting the activity
        intent.putExtra("playlist", playlistToString);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_lib_page);
        getSupportActionBar().hide();

        // setting up our recycler view
        list = findViewById(R.id.songList);
        playlistAddSongAdapter = new PlaylistAddSongAdapter(HomePageActivity.songList);
        list.setAdapter(playlistAddSongAdapter);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //getting handle of our ui
        nameOfPlaylist = findViewById(R.id.PersonName);

        //gettin data saved in shared pref
        sharedPreferences = getSharedPreferences("memory", MODE_PRIVATE);
        sharedPreferences.getInt("integar", 1);

    }

}