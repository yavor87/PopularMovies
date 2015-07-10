package com.yavor.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_ARG = "movie_arg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        MovieInfo movie = (MovieInfo) intent.getSerializableExtra(MOVIE_ARG);

        if (findViewById(R.id.detailFragmentPlaceholder) != null) {
            if (savedInstanceState != null)
                return;

            MovieDetailsFragment fragment = MovieDetailsFragment.createInstance(movie);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailFragmentPlaceholder, fragment)
                    .commit();
        }
    }
}