package com.example.anand.moviezone;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        MoviesData MoviesData =getItem(position);

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.movie_item,parent,false);
        }

        ImageView movieImage=(ImageView)convertView.findViewById(R.id.movieImage);
        TextView movieText=(TextView)convertView.findViewById(R.id.movieText);

        movieImage.setImageBitmap(MoviesData.image);
        movieText.setText(MoviesData.movieName);

        return convertView;
    }
}
