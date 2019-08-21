package com.udacity.luisev96.popularmovies.data;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.udacity.luisev96.popularmovies.database.DatabaseMovie;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.presentation.MoviesListener;
import com.udacity.luisev96.popularmovies.remote.MoviesTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {

    private MoviesDatabase mMoviesDatabase;

    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "?api_key=YOUR_API_KEY";

    private String typeSelected;

    public MoviesRepository(MoviesDatabase moviesDatabase) {
        mMoviesDatabase = moviesDatabase;
    }

    public void setTypeSelected(String typeSelected) {
        this.typeSelected = typeSelected;
    }

    public void refresh(MoviesListener moviesListener) {
        URL url = null;
        try {
            url = new URL(BASE_URL + typeSelected + API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final URL finalUrl = url;
        new MoviesTask(mMoviesDatabase, moviesListener).execute(finalUrl);
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




}
