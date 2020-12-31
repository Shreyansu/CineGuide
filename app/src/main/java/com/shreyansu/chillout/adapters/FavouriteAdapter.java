package com.shreyansu.chillout.adapters;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shreyansu.chillout.R;
import com.shreyansu.chillout.fragments.FavMoviesFragment;
import com.shreyansu.chillout.fragments.FavShowsFragment;

public class FavouriteAdapter extends FragmentPagerAdapter
{
    private Context kcontext;

    public FavouriteAdapter(@NonNull FragmentManager fm, Context kcontext) {
        super(fm);
        this.kcontext = kcontext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new FavMoviesFragment();
            case 1:
                return new FavShowsFragment();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return 2;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return kcontext.getResources().getString(R.string.movie);
            case 1:
                return kcontext.getResources().getString(R.string.TVshows);
        }
        return null;
    }
}
