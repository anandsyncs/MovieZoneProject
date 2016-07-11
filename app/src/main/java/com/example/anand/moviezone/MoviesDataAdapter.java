package com.example.anand.moviezone;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by anand on 06/06/16.
 */
public class MoviesDataAdapter extends ArrayAdapter<MoviesData>{


    public MoviesDataAdapter(Activity context, List<MoviesData> MoviesDatas) {

        super(context, 0, MoviesDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MoviesData moviesData =getItem(position);

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.movie_item,parent,false);
        }

        ImageView movieImage=(ImageView)convertView.findViewById(R.id.movieImage);
        movieImage.setAdjustViewBounds(true);
        Picasso.with(getContext()).load(moviesData.getPosterPath()).into(movieImage);


        return convertView;
    }
}
