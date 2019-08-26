package com.udacity.luisev96.popularmovies.presentation.detail.videos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.luisev96.popularmovies.databinding.FragmentVideosBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.domain.Video;
import com.udacity.luisev96.popularmovies.remote.RemoteListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends Fragment implements RemoteListener {

    private FragmentVideosBinding fragmentVideosBinding;
    private VideosViewModel viewModel;
    private VideosAdapter mAdapter;
    private Movie movie;
    private static final String MOVIE_KEY = "movie";
    private static final String TAG = VideosFragment.class.getSimpleName();

    public VideosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentVideosBinding = FragmentVideosBinding.inflate(inflater);
        return fragmentVideosBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        movie = (Movie) getArguments().getSerializable(MOVIE_KEY);
        assert movie != null;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentVideosBinding.rvVideos.setLayoutManager(layoutManager);
        fragmentVideosBinding.rvVideos.setHasFixedSize(true);
        mAdapter = new VideosAdapter(getContext());
        fragmentVideosBinding.rvVideos.setAdapter(mAdapter);
        viewModel = ViewModelProviders.of(this).get(VideosViewModel.class);
        populateUI();
    }

    private void populateUI() {
        viewModel.videos(this, movie.getId());
        viewModel.getVideos().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(@Nullable List<Video> videos) {
                Log.d(TAG, "Updating list of videos from LiveData in ViewModel");
                mAdapter.setVideos(videos);
            }
        });
    }

    @Override
    public void preExecute() {
        fragmentVideosBinding.pb.setVisibility(View.VISIBLE);
        fragmentVideosBinding.network.setVisibility(View.GONE);
        fragmentVideosBinding.rvVideos.setVisibility(View.GONE);
    }

    @Override
    public void postExecute(Boolean isData) {
        fragmentVideosBinding.pb.setVisibility(View.GONE);
        if (isData) {
            fragmentVideosBinding.network.setVisibility(View.GONE);
            fragmentVideosBinding.rvVideos.setVisibility(View.VISIBLE);
        } else {
            fragmentVideosBinding.rvVideos.setVisibility(View.GONE);
            fragmentVideosBinding.network.setVisibility(View.VISIBLE);
        }
    }
}
