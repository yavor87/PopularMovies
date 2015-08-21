package com.yavor.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {

    private static final String DETAILFRAGMENT_TAG = "details_fragment";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movies_detail_fragment_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.movies_detail_fragment_container, new MovieDetailsFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void movieSelected(int movieId) {
        if (mTwoPane) {
            MovieDetailsFragment fragment = MovieDetailsFragment.createInstance(movieId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movies_detail_fragment_container, fragment, DETAILFRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            Intent showDetailsIntent = new Intent(this, MovieDetailsActivity.class);
            showDetailsIntent.putExtra(MovieDetailsActivity.MOVIE_ID_ARG, movieId);
            startActivity(showDetailsIntent);
        }
    }
}
