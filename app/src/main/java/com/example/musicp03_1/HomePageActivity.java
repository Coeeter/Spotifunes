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
    private SongAdapter songAdapter;
    private String listToBeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        //hiding the title
        getSupportActionBar().hide();

        //accessing the recycler view
        homepageList = findViewById(R.id.homepageList);

        //getting the api info
        String url = "https://songs-e2f4.restdb.io/rest/database-for-songs?apikey=8cbf8ffd3a4984bcc88a11ae957cfdfd40cf0";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                //method to be activated when got response from the url
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //so we can send to search activity
                        listToBeSent = response;

                        //ensure we can change the json to arraylist
                        Gson gson = new Gson();
                        TypeToken<ArrayList<Song>> token = new TypeToken<ArrayList<Song>>(){};
                        songList = gson.fromJson(response, token.getType());

                        //setting up the recycler view
                        songAdapter = new SongAdapter(songList);
                        homepageList.setAdapter(songAdapter);
                        homepageList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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

        for (int i = 0; i < PlaySongActivity.favorites.size(); i++) {
            Log.d("poly",PlaySongActivity.favorites.get(i).getTitle());
        }

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

        Intent intent = new Intent(this,LibPageActivity.class);
        startActivity(intent);

    }

}