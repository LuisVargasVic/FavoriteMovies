package com.udacity.luisev96.popularmovies.presentation.movies;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.udacity.luisev96.popularmovies.data.MoviesRepository;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.remote.RemoteListener;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private static final String TAG = MoviesViewModel.class.getSimpleName();
    MoviesRepository repository;
    private LiveData<List<Movie>> movies;
    private LiveData<List<Movie>> favMovies;

    public MoviesViewModel(Application application) {
        super(application);
        MoviesDatabase database = MoviesDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        repository = new MoviesRepository(database);
        movies = repository.getMovies();
        favMovies = repository.getFavMovies();
    }

    public void refresh(String typeSelected, RemoteListener remoteListener) {
        repository.setTypeSelected(typeSelected);
        repository.refresh(remoteListener);
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<List<Movie>> getFavMovies() {
        return favMovies;
    }
}
