package com.udacity.luisev96.popularmovies.presentation.movies;

import android.content.Intent;
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
import com.udacity.luisev96.popularmovies.presentation.detail.DetailActivity;
import com.udacity.luisev96.popularmovies.remote.RemoteListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.MovieClickListener, RemoteListener {

    private ActivityMoviesBinding activityMoviesBinding;
    MoviesAdapter mAdapter;
    MoviesViewModel viewModel;
    private String typeSelected;
    MoviesAdapter.MovieClickListener mMovieClickListener;
    private static final String TAG = MoviesActivity.class.getSimpleName();
    public static final String TYPE_SELECTED = "type_selected";
    public static final String MOVIE = "movie";
    public static final String SORT_POPULAR = "popular";
    public static final String SORT_TOP = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMoviesBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        setSupportActionBar(activityMoviesBinding.toolbar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        activityMoviesBinding.rvMoviesList.setLayoutManager(layoutManager);
        activityMoviesBinding.rvMoviesList.setHasFixedSize(true);
        mMovieClickListener = this;
        mAdapter = new MoviesAdapter(mMovieClickListener);
        activityMoviesBinding.rvMoviesList.setAdapter(mAdapter);
        viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                assert movies != null;
                if (typeSelected.equals(SORT_POPULAR)) {
                    Collections.sort(movies, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie c1, Movie c2) {
                            return Double.compare(c2.getPopularity(), c1.getPopularity());
                        }
                    });
                } else {
                    Collections.sort(movies, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie c1, Movie c2) {
                            return Double.compare(c2.getVoteAverage(), c1.getVoteAverage());
                        }
                    });
                }
                mAdapter.setMovies(movies);
            }
        });
        if (savedInstanceState != null) {
            typeSelected = savedInstanceState.getString(TYPE_SELECTED);
            assert typeSelected != null;
            populateUI(typeSelected);
        } else {
            populateUI(SORT_POPULAR);
        }
    }

    public void populateUI(String sort) {
        typeSelected = sort;
        viewModel.refresh(typeSelected, this);
        if (sort.equals(SORT_POPULAR)) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.popular);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.top_rated);
        }
    }

    @Override
    public void movieClicked(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE, movie);
        startActivity(intent);
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

    @Override
    public void preExecute() {
        activityMoviesBinding.pb.setVisibility(View.VISIBLE);
        activityMoviesBinding.network.setVisibility(View.GONE);
        activityMoviesBinding.rvMoviesList.setVisibility(View.GONE);
    }

    @Override
    public void postExecute(Boolean isData) {
        activityMoviesBinding.pb.setVisibility(View.GONE);
        if (isData) {
            activityMoviesBinding.network.setVisibility(View.GONE);
            activityMoviesBinding.rvMoviesList.setVisibility(View.VISIBLE);
        } else {
            activityMoviesBinding.rvMoviesList.setVisibility(View.GONE);
            activityMoviesBinding.network.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TYPE_SELECTED, typeSelected);
    }
}
