package com.udacity.luisev96.popularmovies.data;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.udacity.luisev96.popularmovies.database.DatabaseMovie;
import com.udacity.luisev96.popularmovies.database.DatabaseVideo;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.domain.Video;
import com.udacity.luisev96.popularmovies.remote.MoviesTask;
import com.udacity.luisev96.popularmovies.remote.RemoteListener;
import com.udacity.luisev96.popularmovies.remote.VideosTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {

    private MoviesDatabase mMoviesDatabase;

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "?api_key=YOUR_API_KEY";
    private static final String VIDEOS = "/videos";

    private String typeSelected;

    public MoviesRepository(MoviesDatabase moviesDatabase) {
        mMoviesDatabase = moviesDatabase;
    }

    public void setTypeSelected(String typeSelected) {
        this.typeSelected = typeSelected;
    }

    public void refresh(RemoteListener remoteListener) {
        URL url = null;
        try {
            url = new URL(BASE_URL + typeSelected + API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final URL finalUrl = url;
        new MoviesTask(mMoviesDatabase, remoteListener).execute(finalUrl);
    }

    public LiveData<List<Movie>> getMovies() {
        return Transformations.map(mMoviesDatabase.moviesDao().getMovies(),
                new Function<List<DatabaseMovie>, List<Movie>>() {
                    @Override
                    public List<Movie> apply(List<DatabaseMovie> databaseMovies) {
                        List<Movie> movies = new ArrayList<>();

                        for (int i = 0; i < databaseMovies.size(); i++) {
                            DatabaseMovie databaseMovie = databaseMovies.get(i);
                            movies.add(new Movie(
                                            databaseMovie.getId(),
                                            databaseMovie.getVoteCount(),
                                            databaseMovie.isVideo(),
                                            databaseMovie.getVoteAverage(),
                                            databaseMovie.getTitle(),
                                            databaseMovie.getPopularity(),
                                            databaseMovie.getPosterPath(),
                                            databaseMovie.getOriginalLanguage(),
                                            databaseMovie.getOriginalTitle(),
                                            databaseMovie.getBackdropPath(),
                                            databaseMovie.isAdult(),
                                            databaseMovie.getOverview(),
                                            databaseMovie.getReleaseDate()
                                    )
                            );
                        }

                        return movies;
                    }
                });
    }

    public void videos(int movieId, RemoteListener remoteListener) {
        URL url = null;
        try {
            url = new URL(BASE_URL + movieId + VIDEOS + API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final URL finalUrl = url;
        new VideosTask(movieId, mMoviesDatabase, remoteListener).execute(finalUrl);
    }


    public LiveData<List<Video>> getVideos(final Integer movieId) {
        return Transformations.map(mMoviesDatabase.moviesDao().getVideos(movieId),
                new Function<List<DatabaseVideo>, List<Video>>() {
                    @Override
                    public List<Video> apply(List<DatabaseVideo> databaseVideos) {
                        List<Video> videos = new ArrayList<>();

                        for (int i = 0; i < databaseVideos.size(); i++) {
                            DatabaseVideo databaseVideo = databaseVideos.get(i);
                            videos.add(new Video(
                                            databaseVideo.getId(),
                                            databaseVideo.getKey(),
                                            databaseVideo.getName(),
                                            databaseVideo.getSite(),
                                            databaseVideo.getSize(),
                                            databaseVideo.getType()
                                    )
                            );
                        }

                        return videos;
                    }
                });
    }

}
