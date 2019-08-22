package com.udacity.luisev96.popularmovies.presentation.detail;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.squareup.picasso.Picasso;
import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.ActivityDetailBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.presentation.detail.reviews.ReviewsFragment;
import com.udacity.luisev96.popularmovies.presentation.detail.videos.VideosFragment;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding activityDetailBinding;
    public static final String MOVIE = "movie";
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w780";
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        setSupportActionBar(activityDetailBinding.toolbar);

        if (savedInstanceState != null) {
            movie = (Movie) savedInstanceState.getSerializable(MOVIE);
            setUpUI();
        } else {
            if (getIntent() != null) {
                if (getIntent().hasExtra(MOVIE)) {
                    movie = (Movie) getIntent().getSerializableExtra(MOVIE);
                    setUpUI();
                }
            }
        }
    }

    public void setUpUI() {
        assert movie != null;
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
}
