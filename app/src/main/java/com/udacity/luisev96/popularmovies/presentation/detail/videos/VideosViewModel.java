package com.udacity.luisev96.popularmovies.presentation.detail.videos;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.udacity.luisev96.popularmovies.data.MoviesRepository;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Video;
import com.udacity.luisev96.popularmovies.remote.RemoteListener;

import java.util.List;

public class VideosViewModel extends AndroidViewModel {

    private static final String TAG = VideosViewModel.class.getSimpleName();
    private MoviesRepository repository;
    private LiveData<List<Video>> videos;

    public VideosViewModel(Application application) {
        super(application);
        MoviesDatabase database = MoviesDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the videos from the DataBase");
        repository = new MoviesRepository(database);
    }

    void videos(RemoteListener remoteListener, int movieId) {
        videos = repository.getVideos(movieId);
        repository.videos(movieId, remoteListener);
    }

    LiveData<List<Video>> getVideos() {
        return videos;
    }
}
