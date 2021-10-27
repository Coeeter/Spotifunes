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
import java.util.Collection;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<ForSongList> implements Filterable {

    //creating the attributes
    private Context context;
    private List<Song> songList;
    private List<Song> filteredSongList;
    private String name;
    private String layoutStyle;

    //adding constructor to update attribute values
    public SongAdapter(List<Song> songs, String name, String layoutStyle) {
        this.songList = songs;
        this.filteredSongList = songs;
        this.name = name;
        this.layoutStyle = layoutStyle;
        /*songList.addAll(songs);
        filteredSongList.addAll(songs);*/

    }

    @NonNull
    @Override
    public ForSongList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //giving the variables value
        context = parent.getContext();

        //creating the layout for the recycleview
        LayoutInflater inflater = LayoutInflater.from(context);

        //checking which view to use
        View songView;
        if(layoutStyle.equals("popular")){

            songView = inflater.inflate(R.layout.item_layout_popular,parent,false);

        }else{

            songView = inflater.inflate(R.layout.item_layout,parent,false);

        }

        return new ForSongList(songView);

    }

    @Override
    public void onBindViewHolder(@NonNull ForSongList holder, int position) {

        //making sure we editing correct position with correct song
        Song song = filteredSongList.get(position);

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
                String listToBeSent = gson.toJson(filteredSongList);

                //creating intent
                Intent intent = new Intent(context, PlaySongActivity.class);

                //passing in some data to our next activity
                intent.putExtra("name",name);
                intent.putExtra("index",position);
                intent.putExtra("songList", listToBeSent);

                //starting the activity
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredSongList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                //converting our input to string
                String charString = constraint.toString();

                //checking the values of the input
                if(charString.isEmpty()){

                    //our default
                    filteredSongList = songList;

                }else{

                    //filtering our values
                    List<Song> filteredList = new ArrayList<Song>();
                    for (int i = 0; i < songList.size(); i++) {
                        if (songList.get(i).getTitle().toLowerCase().contains(charString.toLowerCase()) || songList.get(i).getArtist().toLowerCase().contains(charString.toLowerCase())){
                            //Log.d("poly", constraint.toString().toLowerCase());
                            filteredList.add(songList.get(i));
                        }
                    }

                    //saving our values to a var
                    filteredSongList = filteredList;

                }

/*
                for (int i = 0; i < filteredSongList.size(); i++) {
                    Log.d("poly", filteredSongList.get(i).getTitle());
                }
*/
                //ensure we can use the values in publishresults method
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSongList;

                //Log.d("poly",constraint.toString());

                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                //to show the results
                filteredSongList = (List<Song>) results.values;
                notifyDataSetChanged();

            }

        };

    }

}
