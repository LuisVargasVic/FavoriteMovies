package com.udacity.luisev96.popularmovies.presentation;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.udacity.luisev96.popularmovies.data.MoviesRepository;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Movie;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private static final String TAG = MoviesViewModel.class.getSimpleName();
    MoviesRepository repository;
    private LiveData<List<Movie>> movies;

    public MoviesViewModel(Application application) {
        super(application);
        MoviesDatabase database = MoviesDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        repository = new MoviesRepository(database);
        movies = repository.getMovies();
    }

    public void refresh(String typeSelected, MoviesListener moviesListener) {
        repository.setTypeSelected(typeSelected);
        repository.refresh(moviesListener);
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
