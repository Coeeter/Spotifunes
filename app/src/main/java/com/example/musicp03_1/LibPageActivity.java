package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LibPageActivity extends AppCompatActivity {

    //attributes of our activity
    private RecyclerView playlists;
    private ArrayList<Playlist> listOfPlaylists = new ArrayList<Playlist>();
    private SharedPreferences sharedPreferences;

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

    public void sendDataToPlaylist(View view){

        Intent intent = new Intent(this, PlaylistLibPageActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_page);

        getSupportActionBar().hide();

        //to convert data types to string or vice versa
        Gson gson = new Gson();

        //getting data from sharedpref
        sharedPreferences = getSharedPreferences("memory",MODE_PRIVATE);

        //getting all our playlists created
        String list = sharedPreferences.getString("playlist","");
        TypeToken<ArrayList<Playlist>> token = new TypeToken<ArrayList<Playlist>>(){};

        if (list.isEmpty()){

            //default
            listOfPlaylists = new ArrayList<Playlist>();

        }else{

            //saving our list with values from sharedpref
            listOfPlaylists = gson.fromJson(list,token.getType());

        }

        //getting data from intent(when we delete playlist)
        Bundle playlistData = this.getIntent().getExtras();

        //ensure we execute this only if it is deleted
        if(!playlistData.getString("playlist").equals("")){

            //converting data type
            TypeToken<Playlist> typeToken = new TypeToken<Playlist>(){};
            listOfPlaylists.add(gson.fromJson(playlistData.getString("playlist"),typeToken.getType()));

            //saving our playlist so we can keep the changes
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("playlist", gson.toJson(listOfPlaylists));
            editor.apply();

        }

        //setting up our recycler view
        playlists = findViewById(R.id.playlist);
        PlaylistSongAdapter adapter = new PlaylistSongAdapter(listOfPlaylists,"playlist");
        playlists.setAdapter(adapter);
        playlists.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

    }
}