package com.udacity.luisev96.popularmovies.remote.listeners;

public interface RemoteListener {
    void preExecute();
    void postExecute(Boolean isData);
}
