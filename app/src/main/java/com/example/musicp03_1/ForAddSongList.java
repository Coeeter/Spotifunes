package com.example.musicp03_1;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForAddSongList extends RecyclerView.ViewHolder{

    //creating variables to use the views
    public TextView songTitle;
    public TextView songArtist;
    public ImageView songCover;
    public ImageView addToPlayList;

    public ForAddSongList(@NonNull View itemView) {

        super(itemView);

        //making the variables able to access the views
        songTitle = itemView.findViewById(R.id.playlistName);
        songArtist = itemView.findViewById(R.id.numberSongs);
        songCover = itemView.findViewById(R.id.songCover);
        addToPlayList = itemView.findViewById(R.id.addPlaylist);

    }
}
