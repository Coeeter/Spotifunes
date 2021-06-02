 package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    SongCollection songCollection = new SongCollection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendDataToActivity(int index){
        Intent intent = new Intent(this,PlaySongActivity.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }

    public void handleSelection(View view) {
        /*String resourceId = getResources().getResourceEntryName(view.getId());
        Log.d("poly", "The id of the button is " + resourceId);
        int currentArrayIndex = songCollection.searchSongById(resourceId);
        Log.d("poly","The array index is " + currentArrayIndex);*/
        String resourceId = getResources().getResourceEntryName(view.getId());
        int currentArrayIndex = songCollection.searchSongById(resourceId);
        sendDataToActivity(currentArrayIndex);
    }
    
}