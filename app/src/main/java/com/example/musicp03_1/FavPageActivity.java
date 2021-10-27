package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class FavPageActivity extends AppCompatActivity {

    //the attribures
    private ArrayList<Song> favList;
    private RecyclerView mainList;
    private FavSongAdapter adapter;
    private SharedPreferences sharedPreferences;

    //methods to send data to activities
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
        intent.putExtra("playlist", "");
        startActivity(intent);

    }

    public void clearAll(View view){

        //ensuring we can clear the source of the array
        PlaySongActivity.favorites.clear();

        //show the change made
        adapter.notifyDataSetChanged();

        //converting and saving the favorites
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("favList",gson.toJson(PlaySongActivity.favorites));
        editor.apply();
    }

    public void settingLayout(){

        //getting our favorites
        sharedPreferences = getSharedPreferences("memory", MODE_PRIVATE);
        /*String favorites = sharedPreferences.getString("favList", "");
        if(favorites.equals("")){
            favList = new ArrayList<Song>();
        }else{
            Gson gson = new Gson();
            TypeToken<ArrayList<Song>> token = new TypeToken<ArrayList<Song>>(){};
            favList = gson.fromJson(favorites,token.getType());
        }*/
        favList = PlaySongActivity.favorites;

        //setting up the recyclerview
        mainList = findViewById(R.id.favList);
        adapter = new FavSongAdapter(favList);
        mainList.setAdapter(adapter);
        mainList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_page);

        //hiding the title
        getSupportActionBar().hide();

        //setting the layour
        settingLayout();

    }

}