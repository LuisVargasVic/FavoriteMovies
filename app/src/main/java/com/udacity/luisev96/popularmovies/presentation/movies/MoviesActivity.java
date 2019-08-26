package com.udacity.luisev96.popularmovies.presentation.movies;

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
import com.udacity.luisev96.popularmovies.remote.RemoteListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MoviesActivity extends AppCompatActivity implements RemoteListener {

    private ActivityMoviesBinding activityMoviesBinding;
    MoviesAdapter mAdapter;
    MoviesViewModel viewModel;
    private String typeSelected;
    private List<Movie> mFavMovies = new ArrayList<>();
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
                assert movies != null;
                if (typeSelected.equals(SORT_POPULAR)) {
                    Collections.sort(movies, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie c1, Movie c2) {
                            return Double.compare(c2.getPopularity(), c1.getPopularity());
                        }
                    });
                    mAdapter.setMovies(movies);
                } else if (typeSelected.equals(SORT_TOP)) {
                    Collections.sort(movies, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie c1, Movie c2) {
                            return Double.compare(c2.getVoteAverage(), c1.getVoteAverage());
                        }
                    });
                    mAdapter.setMovies(movies);
                }
            }
        });
        viewModel.getFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> favMovies) {
                mFavMovies = favMovies;
                if (typeSelected.equals(SORT_FAVORITE)) mAdapter.setMovies(mFavMovies);
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
        if (!typeSelected.equals(SORT_FAVORITE)) viewModel.refresh(typeSelected, this);
        else mAdapter.setMovies(mFavMovies);

        if (sort.equals(SORT_POPULAR)) Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.popular);
        else if (sort.equals(SORT_TOP)) Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.top_rated);
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
