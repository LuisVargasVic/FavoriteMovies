package com.udacity.luisev96.popularmovies.presentation.detail.reviews;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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

import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.remote.receivers.ReviewsReceiver;
import com.udacity.luisev96.popularmovies.databinding.FragmentReviewsBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.domain.Review;
import com.udacity.luisev96.popularmovies.remote.listeners.ConnectionListener;
import com.udacity.luisev96.popularmovies.remote.listeners.RemoteListener;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment implements RemoteListener, ConnectionListener {

    private FragmentReviewsBinding fragmentReviewsBinding;
    private ReviewsViewModel viewModel;
    private ReviewsAdapter mAdapter;
    private BroadcastReceiver mReceiver;
    private static ConnectionListener connectionListener;
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
        connectionListener = this;
        assert getArguments() != null;
        Movie movie = (Movie) getArguments().getSerializable(MOVIE_KEY);
        assert movie != null;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentReviewsBinding.rvReviews.setLayoutManager(layoutManager);
        fragmentReviewsBinding.rvReviews.setHasFixedSize(true);
        mAdapter = new ReviewsAdapter();
        fragmentReviewsBinding.rvReviews.setAdapter(mAdapter);
        viewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        populateUI(movie.getId());
    }

    public void populateUI(int id) {
        mReceiver = new ReviewsReceiver();
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        viewModel.reviews(this, id);
        viewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                Log.d(TAG, "Updating list of reviews from LiveData in ViewModel");
                if (reviews == null || reviews.isEmpty()) {
                    fragmentReviewsBinding.message.setVisibility(View.VISIBLE);
                    fragmentReviewsBinding.rvReviews.setVisibility(View.GONE);
                } else {
                    fragmentReviewsBinding.message.setVisibility(View.GONE);
                    fragmentReviewsBinding.rvReviews.setVisibility(View.VISIBLE);
                    mAdapter.setReviews(reviews);
                }
            }
        });
    }

    @Override
    public void preExecute() {
        fragmentReviewsBinding.pb.setVisibility(View.VISIBLE);
        fragmentReviewsBinding.message.setVisibility(View.GONE);
        fragmentReviewsBinding.rvReviews.setVisibility(View.GONE);
    }

    @Override
    public void postExecute(Boolean isData) {
        fragmentReviewsBinding.pb.setVisibility(View.GONE);
        fragmentReviewsBinding.rvReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mReceiver);
    }

    public static void setReviewsConnection(Boolean connection) {
        connectionListener.connection(connection);
    }

    @Override
    public void connection(Boolean connection) {
        if (connection) {
            fragmentReviewsBinding.message.setText(R.string.reviews_empty);
        } else {
            fragmentReviewsBinding.message.setText(R.string.reviews_connection);
        }
    }
}
