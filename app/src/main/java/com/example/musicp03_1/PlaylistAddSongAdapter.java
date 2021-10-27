package com.example.musicp03_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAddSongAdapter extends RecyclerView.Adapter<ForAddSongList> {

    //creating the attributes
    private Context context;
    private List<Song> songList;
    public  ArrayList<Song> playlistArray = new ArrayList<Song>();
    //private boolean isAdded = true;

    //adding constructor to update attribute values
    public PlaylistAddSongAdapter(List<Song> songs) {
        this.songList = songs;

    }

    @NonNull
    @Override
    public ForAddSongList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //giving the variables value
        context = parent.getContext();

        //creating the layout for the recycleview
        LayoutInflater inflater = LayoutInflater.from(context);
        View songView = inflater.inflate(R.layout.add_playlist_item_layout,parent,false);
        return new ForAddSongList(songView);

    }

    @Override
    public void onBindViewHolder(@NonNull ForAddSongList holder, int position) {

        //to toggle on and off for the button
        final boolean[] isAdded = {true};

        //making sure we editing correct position with correct song
        Song song = songList.get(position);

        //changing the views 'settings'
        TextView songArtist = holder.songArtist;
        songArtist.setText(song.getArtist());

        TextView songTitle = holder.songTitle;
        songTitle.setText(song.getTitle());

        ImageView songCover = holder.songCover;
        Picasso.with(context).load(song.getImageLink()).into(songCover);

        //to reset the ui of the button
        holder.addToPlayList.setImageResource(R.drawable.ic_baseline_playlist_add_24);

        //giving fxn for the button
        holder.addToPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //to check which fxn to execute
                if(isAdded[0]) {

                    //changing the image
                    holder.addToPlayList.setImageResource(R.drawable.ic_baseline_playlist_add_check_24);

                    //adding to playlist
                    playlistArray.add(songList.get(position));

                }else{

                    //changing the image
                    holder.addToPlayList.setImageResource(R.drawable.ic_baseline_playlist_add_24);

                    //removing from the playlist
                    playlistArray.remove(songList.get(position));

                }

                //changing value so we can toggle the button
                isAdded[0] = !isAdded[0];

            }
        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

}
