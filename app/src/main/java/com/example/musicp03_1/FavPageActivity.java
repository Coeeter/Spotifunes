package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class FavPageActivity extends AppCompatActivity {
    ArrayList<Song> favList = new ArrayList<Song>();
    RecyclerView mainList;
    FavSongAdapter adapter;

    public void sendDataToHome(View view){

        Intent intent = new Intent(this,HomePageActivity.class);
        startActivity(intent);

    }

    public void sendDataToSearch(View view){

        Intent intent = new Intent(this,SearchPageActivity.class);
        startActivity(intent);

    }

    public void sendDataToLib(View view){

        Intent intent = new Intent(this,LibPageActivity.class);
        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_page);

        getSupportActionBar().hide();

        favList.addAll(PlaySongActivity.favorites);
        if(favList.size() != 0) {
            mainList = findViewById(R.id.favList);
            adapter = new FavSongAdapter(favList);
            mainList.setAdapter(adapter);
            mainList.setLayoutManager(new LinearLayoutManager(this));
        }

    }
}