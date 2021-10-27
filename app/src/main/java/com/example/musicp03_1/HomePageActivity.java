package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    //creating the attributes
    public static ArrayList<Song> songList = new ArrayList<Song>();
    private RecyclerView homepageList;
    private RecyclerView playlistView;
    private RecyclerView popular;
    private SongAdapter songAdapter;
    private PlaylistSongAdapter playlistSongAdapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<Playlist> listOfPlaylists;
    private ArrayList<Song> popularList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        //hiding the title
        getSupportActionBar().hide();

        //accessing the recycler views
        homepageList = findViewById(R.id.homepageList);
        popular = findViewById(R.id.popularList);
        playlistView = findViewById(R.id.playlistView);

        //getting our data from sharedpref
        sharedPreferences = getSharedPreferences("memory", MODE_PRIVATE);
        String favList = sharedPreferences.getString("favList", "");

        //getting ready objects to convert data types
        Gson gson = new Gson();
        TypeToken<ArrayList<Song>> token = new TypeToken<ArrayList<Song>>(){};

        //making sure our favorites get the correct data type
        if(favList.equals("")){

            //our default val
            PlaySongActivity.favorites = new ArrayList<Song>();

        }else{

            //setting the value saved in sharedpref
            PlaySongActivity.favorites = gson.fromJson(favList,token.getType());

        }

        //set up our playlist list
        String list = sharedPreferences.getString("playlist","");
        TypeToken<ArrayList<Playlist>> tokenForPlaylist = new TypeToken<ArrayList<Playlist>>(){};
        if (list.isEmpty()){

            //default
            listOfPlaylists = new ArrayList<Playlist>();

        }else{

            //if we have saved in shared pref
            listOfPlaylists = gson.fromJson(list,tokenForPlaylist.getType());

        }

        //setting our recycler view
        playlistSongAdapter = new PlaylistSongAdapter(listOfPlaylists,"home");
        playlistView.setAdapter(playlistSongAdapter);
        playlistView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        //getting our songs and updating our popular songs list
        getApi();

    }

    //sending data methods
    public void sendDataToSearch(View view){

        Intent intent = new Intent(this,SearchPageActivity.class);
        startActivity(intent);

    }

    public void sendDataToFav(View view){

        Intent intent = new Intent(this,FavPageActivity.class);
        startActivity(intent);

    }

    public void sendDataToLib(View view){

        Intent intent = new Intent(this,LibPageActivity.class);
        intent.putExtra("playlist", "");
        startActivity(intent);

    }

    //method to get api
    public void getApi(){

        //getting the api info
        String url = "https://songs-e2f4.restdb.io/rest/database-for-songs?apikey=8cbf8ffd3a4984bcc88a11ae957cfdfd40cf0";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                //method to be activated when got response from the url
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //ensure we can change the json to arraylist
                        Gson gson = new Gson();
                        TypeToken<ArrayList<Song>> token = new TypeToken<ArrayList<Song>>(){};

                        String listOfSong = sharedPreferences.getString("songList", "");

                        if(listOfSong.equals("")){
                            songList = gson.fromJson(response, token.getType());
                        }else{
                            songList = gson.fromJson(listOfSong,token.getType());
                        }

                        //setting up the recycler view
                        songAdapter = new SongAdapter(songList, "Recently Played","notPopular");
                        homepageList.setAdapter(songAdapter);
                        homepageList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        //giving val to our popular list
                        popularList = new ArrayList<Song>();
                        for (int i = 0; i < 5; i++) {

                            Song song = songList.get((int) (Math.random() * 5));
                            if(!popularList.contains(song)){

                                popularList.add(song);

                            }else{

                                i--;

                            }

                        }

                        //setting up popular recycler view
                        SongAdapter songAdapter = new SongAdapter(popularList, "Popular Songs", "popular");
                        popular.setAdapter(songAdapter);
                        popular.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

                    }
                },

                //method when kena error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //logging the error
                        Log.d("poly", error.toString());

                    }
                }
        );

        //creating the method to get the data
        RequestQueue queue = Volley.newRequestQueue(this);

        //executing the stringrequest methods
        queue.add(stringRequest);

    }

}