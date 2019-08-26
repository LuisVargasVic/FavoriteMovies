package com.udacity.luisev96.popularmovies.presentation.detail;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.FragmentSynopsisBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;

/**
 * A simple {@link Fragment} subclass.
 */
public class SynopsisFragment extends Fragment {

    private FragmentSynopsisBinding fragmentSynopsisBinding;
    private Movie movie;
    private static final String MOVIE_KEY = "movie";
    private static final String BASE_URL_SMALL = "https://image.tmdb.org/t/p/w342";

    public SynopsisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSynopsisBinding = FragmentSynopsisBinding.inflate(inflater);
        return fragmentSynopsisBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        movie = (Movie) getArguments().getSerializable(MOVIE_KEY);
        assert movie != null;
        Picasso
                .get()
                .load(Uri.parse(BASE_URL_SMALL + movie.getPosterPath()))
                .error(R.drawable.ic_photo)
                .into(fragmentSynopsisBinding.ivDetailMovieImage);
        fragmentSynopsisBinding.tvDetailMovieVoteAverage.setText((String.valueOf(movie.getVoteAverage())));
        fragmentSynopsisBinding.tvDetailMovieReleaseDate.setText(movie.getReleaseDate());
        fragmentSynopsisBinding.tvDetailMovieVoteCount.setText(String.valueOf(movie.getVoteCount()));
        fragmentSynopsisBinding.tvDetailMoviePopularity.setText(String.valueOf(movie.getPopularity()));
        fragmentSynopsisBinding.tvDetailMovieOriginalLanguage.setText(String.valueOf(movie.getOriginalLanguage()));
        fragmentSynopsisBinding.tvDetailMovieOverview.setText(movie.getOverview());
    }
}
