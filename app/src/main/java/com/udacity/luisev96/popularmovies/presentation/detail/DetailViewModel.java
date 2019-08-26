package com.udacity.luisev96.popularmovies.presentation.detail;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.udacity.luisev96.popularmovies.data.MoviesRepository;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.remote.RemoteListener;

/**
 * Created by Luis Vargas on 2019-08-25.
 */

public class DetailViewModel extends AndroidViewModel {

    private static final String TAG = DetailViewModel.class.getSimpleName();
    MoviesRepository repository;
    private LiveData<Movie> movie;

    public DetailViewModel(Application application) {
        super(application);
        MoviesDatabase database = MoviesDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        repository = new MoviesRepository(database);
    }

    void movie(int movieId) {
        movie = repository.getFavMovie(movieId);
    }

    void update(RemoteListener remoteListener, Movie movie, String type) {
        repository.favMovie(remoteListener, movie, type);
    }

    LiveData<Movie> getFavMovie() {
        return movie;
    }
}
