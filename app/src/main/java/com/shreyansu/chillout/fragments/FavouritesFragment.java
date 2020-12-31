package com.shreyansu.chillout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.adapters.FavouriteAdapter;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FavouritesFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FavouritesFragment extends Fragment {

    private TabLayout ktabLayout;
    private ViewPager kViewPager;

//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favourites, container, false);

        ktabLayout=(TabLayout) view.findViewById(R.id.tab_view_fav);
        kViewPager=(ViewPager) view.findViewById(R.id.view_pager_fav);
        kViewPager.setAdapter(new FavouriteAdapter(getChildFragmentManager(),getContext()));
        ktabLayout.setupWithViewPager(kViewPager);

        return view;
    }
}