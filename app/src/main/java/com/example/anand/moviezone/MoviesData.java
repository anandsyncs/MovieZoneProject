package com.example.anand.moviezone;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * Created by anand on 06/06/16.
 */
public class MoviesData {
    String movieName;
    Bitmap image;

    public MoviesData(String movieName, Bitmap image){
        this.movieName=movieName;
        this.image=image;
    }
}
