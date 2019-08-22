package com.udacity.luisev96.popularmovies.remote;

public interface RemoteListener {
    void preExecute();
    void postExecute(Boolean isData);
}
