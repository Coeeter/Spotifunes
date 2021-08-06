package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class SearchPageActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ArrayList<Song> songList = new ArrayList<Song>();
    private RecyclerView searchList;
    private SongAdapter adapter;
    private SearchView searchView;

    public void sendDataToHome(View view){

        Intent intent = new Intent(this,HomePageActivity.class);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        //hiding the title
        getSupportActionBar().hide();

        //getting values of songList
        songList.addAll(HomePageActivity.songList);

        //setting up recycleview
        searchList = findViewById(R.id.searchList);
        adapter = new SongAdapter(songList);
        searchList.setAdapter(adapter);
        searchList.setLayoutManager(new LinearLayoutManager(this));

        //setting up the search
        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }
}