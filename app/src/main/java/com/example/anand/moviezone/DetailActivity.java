package com.example.anand.moviezone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity{
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView=inflater.inflate(R.layout.fragment_detail,container,false);
            ImageView moviePoster=(ImageView)rootView.findViewById(R.id.movieposter);
            TextView movieTitle=(TextView)rootView.findViewById(R.id.moviename);
            TextView movieYear=(TextView)rootView.findViewById(R.id.movieyear);
            TextView movieVotes=(TextView)rootView.findViewById(R.id.movievotes);
            TextView movieSynopsis=(TextView)rootView.findViewById(R.id.moviesynopsis);
            TextView movieRuntime=(TextView)rootView.findViewById(R.id.movieruntime);

            Intent intent=getActivity().getIntent();
            String MovieTitle=intent.getStringExtra("MovieTitle");

            String MovieYear=intent.getStringExtra("MovieYear");

            String MovieVotes=intent.getStringExtra("MovieVotes")+"/10";

            String MovieSynopsis=intent.getStringExtra("MovieDescription");

            String MovieRuntime=intent.getStringExtra("RunTime");

            moviePoster.setImageBitmap((Bitmap) intent.getParcelableExtra("Image"));
            movieTitle.setText(MovieTitle);
            movieYear.setText(MovieYear);
            movieVotes.setText(MovieVotes);
            movieSynopsis.setText(MovieSynopsis);
            movieRuntime.setText(MovieRuntime);

            Log.v("Baba",MovieTitle+" "+MovieRuntime+" "+MovieSynopsis+" "+MovieVotes+" "+MovieYear);
            return rootView;
        }
    }
}
