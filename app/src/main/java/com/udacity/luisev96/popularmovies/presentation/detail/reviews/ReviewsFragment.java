package com.udacity.luisev96.popularmovies.presentation.detail.reviews;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.luisev96.popularmovies.databinding.FragmentReviewsBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.domain.Review;
import com.udacity.luisev96.popularmovies.remote.RemoteListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment implements RemoteListener {

    private FragmentReviewsBinding fragmentReviewsBinding;
    private ReviewsViewModel viewModel;
    private ReviewsAdapter mAdapter;
    private Movie movie;
    private static final String MOVIE_KEY = "movie";
    private static final String TAG = ReviewsFragment.class.getSimpleName();

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentReviewsBinding = FragmentReviewsBinding.inflate(inflater);
        return fragmentReviewsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            movie = (Movie) savedInstanceState.getSerializable(MOVIE_KEY);
        } else {
            assert getArguments() != null;
            movie = (Movie) getArguments().getSerializable(MOVIE_KEY);
        }
        assert movie != null;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentReviewsBinding.rvReviews.setLayoutManager(layoutManager);
        fragmentReviewsBinding.rvReviews.setHasFixedSize(true);
        mAdapter = new ReviewsAdapter();
        fragmentReviewsBinding.rvReviews.setAdapter(mAdapter);
        viewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        populateUI();
    }

    private void populateUI() {
        viewModel.reviews(this, movie.getId());
        viewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapter.setReviews(reviews);
            }
        });
    }


    @Override
    public void preExecute() {
        fragmentReviewsBinding.pb.setVisibility(View.VISIBLE);
        fragmentReviewsBinding.network.setVisibility(View.GONE);
        fragmentReviewsBinding.rvReviews.setVisibility(View.GONE);
    }

    @Override
    public void postExecute(Boolean isData) {
        fragmentReviewsBinding.pb.setVisibility(View.GONE);
        if (isData) {
            fragmentReviewsBinding.network.setVisibility(View.GONE);
            fragmentReviewsBinding.rvReviews.setVisibility(View.VISIBLE);
        } else {
            fragmentReviewsBinding.rvReviews.setVisibility(View.GONE);
            fragmentReviewsBinding.network.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MOVIE_KEY, movie);
    }
}
