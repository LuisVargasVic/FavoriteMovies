package com.udacity.luisev96.popularmovies.presentation.detail;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;
import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.ActivityDetailBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.presentation.detail.reviews.ReviewsFragment;
import com.udacity.luisev96.popularmovies.presentation.detail.videos.VideosFragment;
import com.udacity.luisev96.popularmovies.remote.RemoteListener;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity implements RemoteListener {

    private ActivityDetailBinding activityDetailBinding;
    private DetailViewModel viewModel;
    private Menu mMenu;
    private String type = INSERT;
    public static final String MOVIE = "movie";
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static final String INSERT = "insert";
    private static final String DELETE = "delete";
    private static final String TAG = DetailActivity.class.getSimpleName();
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        setSupportActionBar(activityDetailBinding.toolbar);

        if (savedInstanceState != null) {
            movie = (Movie) savedInstanceState.getSerializable(MOVIE);
            viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
            populateUI();
        } else {
            if (getIntent() != null) {
                if (getIntent().hasExtra(MOVIE)) {
                    movie = (Movie) getIntent().getSerializableExtra(MOVIE);
                    viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
                    populateUI();
                }
            }
        }
    }

    public void populateUI() {
        assert movie != null;
        viewModel.movie(movie.getId());
        viewModel.getFavMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                Log.d(TAG, "Updating fav movie from LiveData in ViewModel");
                if (movie != null) {
                    type = DELETE;
                } else {
                    type = INSERT;
                }
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setTitle(movie.getTitle());
        Picasso
                .get()
                .load(Uri.parse(BASE_URL + movie.getBackdropPath()))
                .error(R.drawable.ic_panorama)
                .into(activityDetailBinding.ivDetailMovieBackdrop);
        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager(), movie);
        adapter.addFragment(new SynopsisFragment(), getString(R.string.synopsis));
        adapter.addFragment(new ReviewsFragment(), getString(R.string.reviews));
        adapter.addFragment(new VideosFragment(), getString(R.string.videos));
        activityDetailBinding.viewPager.setAdapter(adapter);
        activityDetailBinding.tabLayout.setupWithViewPager(activityDetailBinding.viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav, menu);
        mMenu = menu;
        if (type.equals(INSERT)) {
            menu.getItem(0).setIcon(R.drawable.ic_star_border);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_star);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_fav) {
            favMovie();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void favMovie() {
        viewModel.update(this, movie, type);
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Boolean isData) {
        if (isData) {
            mMenu.getItem(0).setIcon(R.drawable.ic_star);
        } else {
            mMenu.getItem(0).setIcon(R.drawable.ic_star_border);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MOVIE, movie);
    }
}
