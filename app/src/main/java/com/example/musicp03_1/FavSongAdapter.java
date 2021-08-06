package com.example.musicp03_1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavSongAdapter extends RecyclerView.Adapter<ForSongList> {

    //creating the attributes
    private Context context;
    private List<Song> songList;

    //adding constructor to update attribute values
    public FavSongAdapter(List<Song> songs) {
        this.songList = songs;

    }

    @NonNull
    @Override
    public ForSongList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //giving the variables value
        context = parent.getContext();

        //creating the layout for the recycleview
        LayoutInflater inflater = LayoutInflater.from(context);
        View songView = inflater.inflate(R.layout.item_layout,parent,false);
        return new ForSongList(songView);

    }

    @Override
    public void onBindViewHolder(@NonNull ForSongList holder, int position) {

        //making sure we editing correct position with correct song
        Song song = songList.get(position);

        //changing the views 'settings'
        TextView songArtist = holder.songArtist;
        songArtist.setText(song.getArtist());

        TextView songTitle = holder.songTitle;
        songTitle.setText(song.getTitle());

        ImageView songCover = holder.songCover;
        Picasso.with(context).load(song.getImageLink()).into(songCover);

        //method to execute when the image is clicked
        songCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                String listToBeSent = gson.toJson(songList);

                //creating intent
                Intent intent = new Intent(context, PlaySongActivity.class);

                //passing in some data to our next activity
                intent.putExtra("index",position);
                intent.putExtra("songList", listToBeSent);

                //starting the activity
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

}
