package com.udacity.luisev96.popularmovies.presentation.detail.reviews;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.udacity.luisev96.popularmovies.data.MoviesRepository;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Review;
import com.udacity.luisev96.popularmovies.remote.RemoteListener;

import java.util.List;

public class ReviewsViewModel extends AndroidViewModel {

    private static final String TAG = ReviewsViewModel.class.getSimpleName();
    private MoviesRepository repository;
    private LiveData<List<Review>> reviews;

    public ReviewsViewModel(Application application) {
        super(application);
        MoviesDatabase database = MoviesDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the reviews from the DataBase");
        repository = new MoviesRepository(database);
    }

    void reviews(RemoteListener remoteListener, int movieId) {
        reviews = repository.getReviews(movieId);
        repository.reviews(movieId, remoteListener);
    }

    LiveData<List<Review>> getReviews() {
        return reviews;
    }
}
