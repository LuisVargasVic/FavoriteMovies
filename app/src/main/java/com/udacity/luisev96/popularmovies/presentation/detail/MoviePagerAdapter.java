package com.udacity.luisev96.popularmovies.presentation.detail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.udacity.luisev96.popularmovies.domain.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mFragmentsTitle = new ArrayList<>();
    private Movie mMovie;
    private static final String MOVIE_KEY = "movie";

    MoviePagerAdapter(FragmentManager fragmentManager, Movie movie) {
        super(fragmentManager);
        mMovie = movie;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragments.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVIE_KEY, mMovie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentsTitle.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentsTitle.add(title);
    }

    Fragment getFragment(int position) {
        return mFragments.get(position);
    }
}
