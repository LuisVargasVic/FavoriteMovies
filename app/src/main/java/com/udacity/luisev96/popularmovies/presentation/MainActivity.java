package com.udacity.luisev96.popularmovies.presentation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.ActivityMainBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieClickListener {

    private ActivityMainBinding activityMainBinding;
    MoviesAdapter mAdapter;
    MoviesViewModel viewModel;
    private String typeSelected;
    MoviesAdapter.MovieClickListener mMovieClickListener;
    private static final String TAG = MainActivity.class.getSimpleName();
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
        mMovieClickListener = this;
        mAdapter = new MoviesAdapter(mMovieClickListener);
        activityMainBinding.rvMoviesList.setAdapter(mAdapter);
        viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapter.setMovies(movies);
            }
        });
        if (savedInstanceState != null) {
            typeSelected = savedInstanceState.getString(TYPE_SELECTED);
            populateUI(typeSelected);
        } else {
            populateUI(SORT_POPULAR);
        }
    }

    public void populateUI(String sort) {
        typeSelected = sort;
        viewModel.refresh(typeSelected);
        if (sort.equals(SORT_POPULAR)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.popular);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.top_rated);
        }
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
