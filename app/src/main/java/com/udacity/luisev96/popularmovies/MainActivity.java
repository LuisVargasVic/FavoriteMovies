package com.udacity.luisev96.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.udacity.luisev96.popularmovies.databinding.ActivityMainBinding;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieClickListener {

    private ActivityMainBinding activityMainBinding;
    private String typeSelected;
    public static final String TYPE_SELECTED = "type_selected";
    public static final String MOVIE = "movie";
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "?api_key=YOUR_API_KEY";
    public static final String SORT_POPULAR = "popular";
    public static final String SORT_TOP = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        activityMainBinding.rvMoviesList.setLayoutManager(layoutManager);
        activityMainBinding.rvMoviesList.setHasFixedSize(true);

        if (savedInstanceState != null) {
            typeSelected = savedInstanceState.getString(TYPE_SELECTED);
            populateUI(typeSelected);
        } else {
            populateUI(SORT_POPULAR);
        }
    }

    public void populateUI(String sort) {
        typeSelected = sort;
        if (sort.equals(SORT_POPULAR)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.popular);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.top_rated);
        }
        URL url = null;
        try {
            url = new URL(BASE_URL + sort + API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new PopularMoviesTask(this).execute(url);
    }

    @Override
    public void movieClicked(Movie movie) {
        /*Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE, movie);
        startActivity(intent);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_popular) {
            populateUI(SORT_POPULAR);
            return true;
        } else if (item.getItemId() == R.id.action_top) {
            populateUI(SORT_TOP);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class PopularMoviesTask extends AsyncTask<URL, Void, String> {

        MoviesAdapter.MovieClickListener mMovieClickListener;

        public PopularMoviesTask(MoviesAdapter.MovieClickListener movieClickListener) {
            mMovieClickListener = movieClickListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activityMainBinding.pb.setVisibility(View.VISIBLE);
            activityMainBinding.network.setVisibility(View.GONE);
            activityMainBinding.rvMoviesList.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL url = params[0];
            String result = null;
            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    result = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            activityMainBinding.pb.setVisibility(View.GONE);
            if (result != null) {
                activityMainBinding.network.setVisibility(View.GONE);
                activityMainBinding.rvMoviesList.setVisibility(View.VISIBLE);

                MoviesAdapter mAdapter = new MoviesAdapter(JsonUtils.parseMovieJson(result), mMovieClickListener);
                activityMainBinding.rvMoviesList.setAdapter(mAdapter);

            } else {
                activityMainBinding.rvMoviesList.setVisibility(View.GONE);
                activityMainBinding.network.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TYPE_SELECTED, typeSelected);
    }
}
