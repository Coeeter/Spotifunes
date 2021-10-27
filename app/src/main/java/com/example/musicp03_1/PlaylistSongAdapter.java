package com.example.musicp03_1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistSongAdapter extends RecyclerView.Adapter<ForSongList> {

    //creating the attributes
    private Context context;
    private String layoutStyle;
    private final List<Playlist> listOfPlaylist;

    //adding constructor to update attribute values
    public PlaylistSongAdapter(List<Playlist> songs, String layoutStyle) {

        this.listOfPlaylist = songs;
        this.layoutStyle = layoutStyle;

    }

    @NonNull
    @Override
    public ForSongList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //giving the variables value
        context = parent.getContext();

        //creating the layout for the recycleview
        LayoutInflater inflater = LayoutInflater.from(context);
        View songView;

        //checking which layout to use
        if(layoutStyle.equals("home")){

            songView = inflater.inflate(R.layout.playlist_item_layout_home,parent,false);

        }else{

            songView = inflater.inflate(R.layout.playlist_item_layout,parent,false);

        }

        return new ForSongList(songView);

    }

    @Override
    public void onBindViewHolder(@NonNull ForSongList holder, int position) {

        //making sure we editing correct position with correct song
        Playlist playlist = listOfPlaylist.get(position);

        //changing the views 'settings'
        TextView songArtist = holder.songArtist;
        songArtist.setText(playlist.playlist.size() + " songs");

        ImageView songCover = holder.songCover;
        Picasso.with(context).load(playlist.imageLink).into(songCover);

        TextView songTitle = holder.songTitle;

        //ensuring our name is not too long
        if(playlist.name.length() > 7 && layoutStyle.equals("home")){

            songTitle.setText(playlist.name.substring(0,8) + "...");

        }else{

            songTitle.setText(playlist.name);

        }

        //method to execute when the image is clicked
        songCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creating intent
                Intent intent = new Intent(context, PlaylistActivity.class);
                Gson gson = new Gson();

                //passing in datat
                intent.putExtra("name", playlist.name);
                intent.putExtra("index", position);
                intent.putExtra("listOfSong", gson.toJson(listOfPlaylist));

                //starting activity
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfPlaylist.size();
    }

}
