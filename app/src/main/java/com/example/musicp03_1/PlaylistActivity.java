package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    //attributes of our activity
    private ImageView playlistCover;
    private TextView playlistName;
    private TextView playlistNum;
    private RecyclerView list;
    private SongAdapter adapter;
    private Playlist playlist;
    private ArrayList<Playlist> listOfPlaylist;
    private int index;
    private SharedPreferences sharedPreferences;

    public void playAll(View view){

        //converting the data type to string
        Gson gson = new Gson();
        String listToBeSent = gson.toJson(playlist.playlist);

        //creating intent
        Intent intent = new Intent(this, PlaySongActivity.class);

        //passing in some data to our next activity
        intent.putExtra("name",playlist.name);
        intent.putExtra("index",0);
        intent.putExtra("songList", listToBeSent);

        //starting the activity
        startActivity(intent);

    }

    public void back(View view){

        //so we can go to prev activity
        onBackPressed();

    }

    public void deletePlaylist(View view){

        //so we can delete the playlist
        listOfPlaylist.remove(playlist);
        sendDataToLib(view);

    }

    //methods to send data
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

    public void sendDataToLib(View view){

        //passing datat to library activity
        Intent intent = new Intent(this,LibPageActivity.class);

        //saving to sharedpref
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String listToBeSent = gson.toJson(listOfPlaylist);
        editor.putString("playlist",listToBeSent);
        editor.apply();

        //starting the activity and giving the default putExtra
        intent.putExtra("playlist","");
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        getSupportActionBar().hide();

        //giving value to sharedpref var
        sharedPreferences = getSharedPreferences("memory",MODE_PRIVATE);

        //getting data from intent
        Bundle songData = this.getIntent().getExtras();

        //to convert from string
        Gson gson = new Gson();
        TypeToken<ArrayList<Playlist>> token = new TypeToken<ArrayList<Playlist>>(){};

        //giving some attributes values
        listOfPlaylist = gson.fromJson(songData.getString("listOfSong"),token.getType());
        index = songData.getInt("index");

        //getting a handle on our Views
        playlist = listOfPlaylist.get(index);
        playlistCover = findViewById(R.id.playlistCover);
        playlistName = findViewById(R.id.nameOfPlaylist);
        playlistNum = findViewById(R.id.numberOfSongs);
        list = findViewById(R.id.playlistList);

        //setting up the recycler view
        adapter = new SongAdapter(playlist.playlist, playlist.name, "notPopular");
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        //setting up the image and the texts to be shown
        Picasso.with(this).load(playlist.imageLink).into(playlistCover);
        playlistNum.setText(playlist.playlist.size() + " Songs");
        playlistName.setText(playlist.name);

    }
}