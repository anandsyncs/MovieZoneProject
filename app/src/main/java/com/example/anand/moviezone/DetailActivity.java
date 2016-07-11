package com.example.anand.moviezone;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public class DetailActivity extends AppCompatActivity{
    private static String DEBUG_TAG="Baba Detail";
    private static String[] trailers=new String[2];
    private static String trailer1Url="";
    private static String trailer2Url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.container,new DetailFragment()).commit();
        }
    }

    public static class DetailFragment extends Fragment{
        public DetailFragment(){

        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView=inflater.inflate(R.layout.fragment_detail,container,false);
            ImageView moviePoster=(ImageView)rootView.findViewById(R.id.movieposter);
            TextView movieTitle=(TextView)rootView.findViewById(R.id.moviename);
            TextView movieYear=(TextView)rootView.findViewById(R.id.movieyear);
            TextView movieVotes=(TextView)rootView.findViewById(R.id.movievotes);
            TextView movieSynopsis=(TextView)rootView.findViewById(R.id.moviesynopsis);
            TextView movieRuntime=(TextView)rootView.findViewById(R.id.movieruntime);
            final TextView trailer1=(TextView)rootView.findViewById(R.id.trailer1);
            final TextView trailer2=(TextView)rootView.findViewById(R.id.trailer2);


            Intent intent=getActivity().getIntent();
            final MoviesData singleMovie= (MoviesData) intent.getSerializableExtra("MovieDetails");
            Picasso.with(getContext()).load(singleMovie.getPosterPath()).into(moviePoster);
            movieTitle.setText(singleMovie.getMovieName());
            movieYear.setText(singleMovie.getReleaseDate());
            movieVotes.setText(singleMovie.getVoteAverage());
            movieSynopsis.setText(singleMovie.getOverview());
            movieRuntime.setText(singleMovie.getRunTime());


            new fetchTrailers().execute("http://api.themoviedb.org/3/movie/"+singleMovie.getImdbID()+"/videos?api_key="+getString(R.string.APIKey));
            trailer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trailer1Url="https://www.youtube.com/watch?v="+trailers[0];
                    Intent intent1=new Intent(Intent.ACTION_VIEW, Uri.parse(trailer1Url));
                    Log.d(DEBUG_TAG,trailer1Url);
                    startActivity(intent1);
                }
            });

            trailer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trailer2Url="https://www.youtube.com/watch?v="+trailers[1];
                    Intent intent2=new Intent(Intent.ACTION_VIEW, Uri.parse(trailer2Url));
                    startActivity(intent2);
                }
            });

            Log.v("Baba",singleMovie.toString());

            return rootView;


        }
    }

    public static class fetchTrailers extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Log.d(DEBUG_TAG,strings[0]);
                getDataFromServer(strings[0]);
                Log.d(DEBUG_TAG,"Async Task Successful");
            } catch (IOException e) {
                Log.d(DEBUG_TAG,e.toString());

            }
            return null;
        }


    }

    public static void getDataFromServer(String url) throws IOException {

        URL connect=new URL(url);
        InputStream stream = null;
        Log.d(DEBUG_TAG,"get data from server");
        try {
            // Establish a connection
            Log.d(DEBUG_TAG,"network start");
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
            trailers= JSONParser(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static String[] JSONParser(InputStream stream) throws IOException {
        String[] results=new String[2];
        BufferedReader in=new BufferedReader(new InputStreamReader(stream));
        String jsonData=in.readLine();

        try{

            JSONObject movieResults=new JSONObject(jsonData);
            JSONArray movieArray=movieResults.getJSONArray("results");
            int i=0;
            while (i<movieArray.length() && !(movieArray.getJSONObject(i).getString("type").equals("Trailer"))){

                i++;
            }

            JSONObject singleMovie=movieArray.getJSONObject(i);
            Log.d(DEBUG_TAG, "Successful. String was: " + i);
            results[0]=singleMovie.getString("key");
            while (i<movieArray.length()-1 && !(movieArray.getJSONObject(i).getString("type").equals("Trailer"))){

                i++;
            }
            singleMovie=movieArray.getJSONObject(i);
            results[1]=singleMovie.getString("key");
        }
        catch (JSONException e){
            Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + e);
        }
        return results;
    }
}
