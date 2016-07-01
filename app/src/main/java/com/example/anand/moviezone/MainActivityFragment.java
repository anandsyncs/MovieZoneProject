package com.example.anand.moviezone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbFind;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MainActivityFragment extends Fragment{

    private String APIKey="YOUR TMDB API KEY";
    private MoviesDataAdapter moviesDataAdapter;
    private List<MovieDb> movie;
    List<MoviesData> moviesDataList= new ArrayList<>();
    public MainActivityFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.popular){
            new FetchMovies().execute("popular");
        }
        else if(id==R.id.latest){
            new FetchMovies().execute("latest");
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.activity_main_fragment,container,true);

        GridView gridView=(GridView)rootView.findViewById(R.id.movie_grid);


        new FetchMovies().execute("latest");
        moviesDataAdapter=new MoviesDataAdapter(getActivity(), moviesDataList);
        gridView.setAdapter(moviesDataAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieDb movieDetail=movie.get(i);

                String movieDescription=movieDetail.getOverview();
                String movieYear=movieDetail.getReleaseDate();
                String movieVotes= String.valueOf(movieDetail.getVoteAverage());
                String movieTitle=movieDetail.getTitle();
                String runTime= String.valueOf(movieDetail.getRuntime());
                MoviesData o=moviesDataList.get(i);
                Bitmap image=o.image;

                Intent intent=new Intent(getActivity(),DetailActivity.class);

                intent.putExtra("MovieDescription",movieDescription);
                intent.putExtra("MovieYear",movieYear);
                intent.putExtra("MovieVotes",movieVotes);
                intent.putExtra("MovieTitle",movieTitle);
                intent.putExtra("Runtime",runTime);
                intent.putExtra("Image",image);
//                Log.v("Baba2",runTime);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchMovies extends AsyncTask<String, Void, List<MoviesData>> {

        @Override
        protected List<MoviesData> doInBackground(String... id) {

            List<MoviesData> store=new ArrayList<>();
            TmdbMovies movies=new TmdbApi(APIKey).getMovies();
            TmdbFind movies1=new TmdbApi(APIKey).getFind();
//            movies1.find();
            movie=new ArrayList<>();


            MovieResultsPage movieResultsPage;

            if (id[0].equals("popular")){
                movieResultsPage=movies.getPopularMovies("en",0);
            }
            else {
                movieResultsPage=movies.getNowPlayingMovies("en",0);
            }
            movie=movieResultsPage.getResults();
            for (int i=0;i<10;i++){
                MovieDb d=movie.get(i);
                String Movie_tiltle = d.getOriginalTitle();
                String image_path="http://image.tmdb.org/t/p/w185/"+d.getPosterPath();
                Bitmap image = null;
                try {
                    image= Picasso.with(getActivity()).load(image_path).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("Baba ji ka ghanta", Movie_tiltle);

                MoviesData singleMovie=new MoviesData(Movie_tiltle,image);
                store.add(singleMovie);
            }

            return store;
        }



        @Override
        protected void onPostExecute(List<MoviesData> movieDbs) {
//            moviesDataList=new ArrayList<>();
            moviesDataList.clear();
            moviesDataList.addAll(movieDbs);
            moviesDataAdapter.notifyDataSetChanged();
        }
    }
}
