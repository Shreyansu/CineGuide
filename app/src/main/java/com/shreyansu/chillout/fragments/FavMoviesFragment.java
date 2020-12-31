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
import com.shreyansu.chillout.network.movies.MovieDetail;
import com.shreyansu.chillout.util.Favourite;

import java.util.ArrayList;
import java.util.List;


public class FavMoviesFragment extends Fragment
{
    private RecyclerView kMovieFavRecView;
    private List<MovieDetail> kFavMovies;
    private MovieDetailSmallAdapter kFavMovieAdapter;

    private LinearLayout klayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fav_movies, container, false);

        kMovieFavRecView=(RecyclerView)view.findViewById(R.id.recyc_view_fav_movies);
        kFavMovies=new ArrayList<>();
        kFavMovieAdapter=new MovieDetailSmallAdapter(getContext(),kFavMovies);
        kMovieFavRecView.setAdapter(kFavMovieAdapter);
        kMovieFavRecView.setLayoutManager(new GridLayoutManager(getContext(),3));

        klayout=(LinearLayout) view.findViewById(R.id.linear_layout_rec_view_fav_movies);

        loadFavMovies();


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        kFavMovieAdapter.notifyDataSetChanged();
    }

    private void loadFavMovies()
    {
        List<MovieDetail> favMovieDetails= Favourite.getFavMovieDetails(getContext());
        if(favMovieDetails.isEmpty())
        {
            klayout.setVisibility(View.VISIBLE);

            return;
        }
        for(MovieDetail movieDetail: favMovieDetails)
            kFavMovies.add(movieDetail);
        kFavMovieAdapter.notifyDataSetChanged();
    }
}