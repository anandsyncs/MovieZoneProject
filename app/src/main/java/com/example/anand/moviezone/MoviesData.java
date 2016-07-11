package com.example.anand.moviezone;

import android.graphics.Bitmap;
import android.media.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by anand on 06/06/16.
 */
public class MoviesData implements Serializable{

    private String posterBasePath="http://image.tmdb.org/t/p/w500/";
    private String movieName;
    private String posterPath;
    private String runTime;
    private String overview;
    private String releaseDate;
    private String voteAverage;
    private String imdbID;

    public MoviesData(){

    }

    public MoviesData(JSONObject singleMovie) throws JSONException {

        setMovieName(singleMovie.getString("title"));
        setPosterPath(singleMovie.getString("poster_path"));
        setRunTime(singleMovie.getString("original_language"));
        setOverview(singleMovie.getString("overview"));
        setReleaseDate(singleMovie.getString("release_date"));
        setVoteAverage(singleMovie.getString("vote_average"));
        setImdbID(singleMovie.getString("id"));


    }

    public void setMovieName(String movieName){
        this.movieName=movieName;
    }

    public void setPosterPath(String posterPath){
        this.posterPath=posterBasePath+posterPath;
    }

    public void setRunTime(String runTime){
        this.runTime=runTime.toUpperCase();
    }

    public void setOverview(String overview){
        this.overview=overview;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate=releaseDate;
    }

    public void setVoteAverage(String voteAverage){
        this.voteAverage=voteAverage+"/10";
    }

    public void setImdbID(String imdbID){
        this.imdbID=imdbID;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRunTime() {
        return runTime;
    }

    public String getVoteAverage() {
        return voteAverage;
    }



}
