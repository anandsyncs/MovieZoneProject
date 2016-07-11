package com.example.anand.moviezone;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivityFragment extends Fragment{
    private String DEBUG_TAG="BabaJi";
    private MoviesDataAdapter moviesDataAdapter;
    List<MoviesData> moviesDataList= new ArrayList<>();
    DisplayMetrics metrics = new DisplayMetrics();
//    ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
//            .getDefaultDisplay().getMetrics(metrics);
//     getWindowManager().getDefaultDisplay().getMetrics(met);
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
            new FetchMovies().execute(getString(R.string.PopulerMoviesURL));

        }
//        else if(id==R.id.latest){
//            new FetchMovies().execute(getString(R.string.LatestMoviesURL));
//        }
        else if (id==R.id.toprated) {
            new FetchMovies().execute(getString(R.string.TopRatedMoviesURL));
//            getActivity().getActionBar().setTitle("Top Rated Movies");

        }
        else if(id==R.id.upcoming){
            new FetchMovies().execute(getString(R.string.UpcomingMoviesURL));
//            getActivity().getActionBar().setTitle("Upcoming Movies");

        }
        else if(id==R.id.nowplaying){
            new FetchMovies().execute(getString(R.string.NowPlayingMoviesURL));
//            getActivity().getActionBar().setTitle("Now Playing");

        }
        else if(id==R.id.credits){
            startActivity(new Intent(getContext(),Credits.class));
//            getActivity().getActionBar().setTitle("Credits");

        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.activity_main_fragment,container,true);

        GridView gridView=(GridView)rootView.findViewById(R.id.movie_grid);


        new FetchMovies().execute(getString(R.string.PopulerMoviesURL));
        moviesDataAdapter=new MoviesDataAdapter(getActivity(), moviesDataList);
        gridView.setAdapter(moviesDataAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("MovieDetails",  moviesDataList.get(i));
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchMovies extends AsyncTask<String, Void, List<MoviesData>> {

        @Override
        protected List<MoviesData> doInBackground(String... id) {

            List<MoviesData> store = new ArrayList<>();
            try {
                store=getDataFromServer(id[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return store;
        }




        @Override
        protected void onPostExecute(List<MoviesData> movieDbs) {
            moviesDataList.clear();
            moviesDataList.addAll(movieDbs);
            moviesDataAdapter.notifyDataSetChanged();
        }
    }

    public ArrayList<MoviesData> getDataFromServer(String url) throws IOException {

        URL connect=new URL(url);
        InputStream stream = null;
        try {
            // Establish a connection
            HttpURLConnection conn = (HttpURLConnection) connect.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Accept", "application/json"); // Required to get TMDB to play nicely.
            conn.setDoInput(true);
            conn.connect();

            int responseCode = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response code is: " + responseCode + " " + conn.getResponseMessage());

            stream = conn.getInputStream();
            return JSONParser(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public ArrayList<MoviesData> JSONParser(InputStream stream) throws IOException {
        ArrayList<MoviesData> results=new ArrayList<>();
        BufferedReader in=new BufferedReader(new InputStreamReader(stream));
        String jsonData=in.readLine();

        try{

            JSONObject movieResults=new JSONObject(jsonData);
            JSONArray movieArray=movieResults.getJSONArray("results");
            for (int i=0;i<movieArray.length();i++){
                JSONObject singleMovie=movieArray.getJSONObject(i);
                Log.d(DEBUG_TAG, ""+i);

                MoviesData moviesData=new MoviesData(singleMovie);
                results.add(moviesData);

            }
        }
        catch (JSONException e){
            Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + e);
        }
        return results;
    }

}
