 package com.example.musicp03_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleSelection(View view) {
        int buttonId = view.getId();
        String resourceId = getResources().getResourceEntryName(buttonId);
        Log.d("poly", "The id of the button is " + resourceId);
    }
    
}