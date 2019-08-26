package com.udacity.luisev96.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM DatabaseMovie")
    LiveData<List<DatabaseMovie>> getMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(DatabaseMovie movie);

    @Query("SELECT * FROM DatabaseVideo WHERE typeId = :movieId")
    LiveData<List<DatabaseVideo>> getVideos(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(DatabaseVideo video);

    @Query("SELECT * FROM DatabaseReview WHERE typeId = :movieId")
    LiveData<List<DatabaseReview>> getReviews(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReview(DatabaseReview review);

    @Query("SELECT * FROM DatabaseFavMovie")
    LiveData<List<DatabaseFavMovie>> getFavMovies();

    @Query("SELECT * FROM DatabaseFavMovie WHERE id = :id")
    LiveData<DatabaseFavMovie> getFavMovie(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavMovie(DatabaseFavMovie movie);

    @Delete
    void deleteFavMovie(DatabaseFavMovie movie);
}
