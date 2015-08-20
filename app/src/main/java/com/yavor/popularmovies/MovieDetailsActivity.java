package com.yavor.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_ID_ARG = "movie_id_arg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        int movieId = intent.getIntExtra(MOVIE_ID_ARG, -1);

        if (findViewById(R.id.detailFragmentPlaceholder) != null) {
            if (savedInstanceState != null)
                return;

            MovieDetailsFragment fragment = MovieDetailsFragment.createInstance(movieId);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailFragmentPlaceholder, fragment)
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
