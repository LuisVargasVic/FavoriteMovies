package com.udacity.luisev96.popularmovies.presentation.movies;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.ActivityMoviesBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.remote.listeners.ConnectionListener;
import com.udacity.luisev96.popularmovies.remote.listeners.RemoteListener;
import com.udacity.luisev96.popularmovies.remote.receivers.MoviesReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MoviesActivity extends AppCompatActivity implements RemoteListener, ConnectionListener {

    private ActivityMoviesBinding activityMoviesBinding;
    private MoviesAdapter mAdapter;
    private MoviesViewModel viewModel;
    private BroadcastReceiver mReceiver;
    private String typeSelected;
    private List<Movie> mFavMovies = new ArrayList<>();
    private List<Movie> mMovies = new ArrayList<>();
    private boolean mConnection;
    private static ConnectionListener connectionListener;
    private static final String TAG = MoviesActivity.class.getSimpleName();
    public static final String TYPE_SELECTED = "type_selected";
    public static final String MOVIE = "movie";
    public static final String SORT_POPULAR = "popular";
    public static final String SORT_TOP = "top_rated";
    public static final String SORT_FAVORITE = "favorite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMoviesBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        setSupportActionBar(activityMoviesBinding.toolbar);
        mReceiver = new MoviesReceiver();
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        connectionListener = this;
        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.span_count));
        activityMoviesBinding.rvMoviesList.setLayoutManager(layoutManager);
        activityMoviesBinding.rvMoviesList.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(this);
        activityMoviesBinding.rvMoviesList.setAdapter(mAdapter);
        viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                mMovies = movies;
                if (!typeSelected.equals(SORT_FAVORITE)) sort();
            }
        });
        viewModel.getFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> favoriteMovies) {
                Log.d(TAG, "Updating list of favorite movies from LiveData in ViewModel");
                mFavMovies = favoriteMovies;
                if (typeSelected.equals(SORT_FAVORITE)) sortFavorite();
            }
        });
        if (savedInstanceState != null) {
            typeSelected = savedInstanceState.getString(TYPE_SELECTED);
            assert typeSelected != null;
            populateUI(typeSelected);
        } else populateUI(SORT_POPULAR);
    }

    public void populateUI(String sort) {
        typeSelected = sort;
        if (!typeSelected.equals(SORT_FAVORITE)) {
            connection(mConnection);
            viewModel.refresh(typeSelected, this);
        } else sortFavorite();

        if (sort.equals(SORT_POPULAR))
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.popular);
        else if (sort.equals(SORT_TOP))
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.top_rated);
        else Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.favorite);
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
        } else if (item.getItemId() == R.id.action_fav) {
            populateUI(SORT_FAVORITE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void preExecute() {
        activityMoviesBinding.pb.setVisibility(View.VISIBLE);
        activityMoviesBinding.message.setVisibility(View.GONE);
        activityMoviesBinding.rvMoviesList.setVisibility(View.GONE);
    }

    @Override
    public void postExecute(Boolean isData) {
        activityMoviesBinding.pb.setVisibility(View.GONE);
        activityMoviesBinding.rvMoviesList.setVisibility(View.VISIBLE);
        if (!mConnection) sort();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TYPE_SELECTED, typeSelected);
    }

    void sort() {
        if (mMovies.isEmpty()) {
            activityMoviesBinding.message.setVisibility(View.VISIBLE);
            activityMoviesBinding.rvMoviesList.setVisibility(View.GONE);
        } else {
            activityMoviesBinding.message.setVisibility(View.GONE);
            activityMoviesBinding.rvMoviesList.setVisibility(View.VISIBLE);
            if (typeSelected.equals(SORT_POPULAR)) {
                Collections.sort(mMovies, new Comparator<Movie>() {
                    @Override
                    public int compare(Movie c1, Movie c2) {
                        return Double.compare(c2.getPopularity(), c1.getPopularity());
                    }
                });
            } else if (typeSelected.equals(SORT_TOP)) {
                Collections.sort(mMovies, new Comparator<Movie>() {
                    @Override
                    public int compare(Movie c1, Movie c2) {
                        return Double.compare(c2.getVoteAverage(), c1.getVoteAverage());
                    }
                });
            }
            mAdapter.setMovies(mMovies);
        }
    }

    void sortFavorite() {
        if (mFavMovies.isEmpty()) {
            activityMoviesBinding.message.setText(R.string.favorite_empty);
            activityMoviesBinding.message.setVisibility(View.VISIBLE);
            activityMoviesBinding.rvMoviesList.setVisibility(View.GONE);
        } else {
            activityMoviesBinding.message.setVisibility(View.GONE);
            activityMoviesBinding.rvMoviesList.setVisibility(View.VISIBLE);
            mAdapter.setMovies(mFavMovies);
        }
    }

    public static void setMoviesConnection(Boolean connection) {
        connectionListener.connection(connection);
    }

    @Override
    public void connection(Boolean connection) {
        mConnection = connection;
        if (connection) {
            activityMoviesBinding.message.setText(R.string.movies_empty);
        } else {
            activityMoviesBinding.message.setText(R.string.movies_connection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
