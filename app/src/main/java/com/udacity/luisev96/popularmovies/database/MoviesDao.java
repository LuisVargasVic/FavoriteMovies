package com.udacity.luisev96.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM DatabaseMovie")
    LiveData<List<DatabaseMovie>> getMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DatabaseMovie movie);
}
