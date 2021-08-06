package com.example.musicp03_1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForSongList extends RecyclerView.ViewHolder{

    //creating variables to use the views
    public TextView songTitle;
    public TextView songArtist;
    public ImageView songCover;

    public ForSongList(@NonNull View itemView) {

        super(itemView);

        //making the variables able to access the views
        songTitle = itemView.findViewById(R.id.songTitle);
        songArtist = itemView.findViewById(R.id.songArtist);
        songCover = itemView.findViewById(R.id.songCover);

    }
}
