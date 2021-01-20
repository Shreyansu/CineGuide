package com.shreyansu.chillout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shreyansu.chillout.R;
import com.shreyansu.chillout.adapters.MovieDetailSmallAdapter;
import com.shreyansu.chillout.adapters.ShowsDetailSmallAdapter;
import com.shreyansu.chillout.network.movies.MovieDetail;
import com.shreyansu.chillout.network.tvshows.ShowDetail;
import com.shreyansu.chillout.util.Favourite;

import java.util.ArrayList;
import java.util.List;


public class FavShowsFragment extends Fragment
{

    private RecyclerView kShowsFavRecView;
    private List<ShowDetail> kFavShows;
    private ShowsDetailSmallAdapter kFavShowsAdapter;

    private LinearLayout klayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fav_shows, container, false);
        kShowsFavRecView=(RecyclerView)view.findViewById(R.id.recyc_view_fav_shows);
        kFavShows=new ArrayList<>();
        kFavShowsAdapter=new ShowsDetailSmallAdapter(getContext(),kFavShows);
        kShowsFavRecView.setAdapter(kFavShowsAdapter);
        kShowsFavRecView.setLayoutManager(new GridLayoutManager(getContext(),3));
        klayout=(LinearLayout) view.findViewById(R.id.linear_layout_rec_view_fav_shows);
        loadFavShows();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        kFavShowsAdapter.notifyDataSetChanged();
    }

    private void loadFavShows()
    {
        List<ShowDetail> favShowDetails=Favourite.getFavTVShowDetails(getContext());
        if(favShowDetails.isEmpty())
        {
            klayout.setVisibility(View.VISIBLE);
            return;
        }
        for(ShowDetail showDetail: favShowDetails)
            kFavShows.add(showDetail);

        kFavShowsAdapter.notifyDataSetChanged();
    }
}