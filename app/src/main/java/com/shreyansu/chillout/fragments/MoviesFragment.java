package com.shreyansu.chillout.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.activities.ViewAllMoviesActivity;
import com.shreyansu.chillout.adapters.MovieDetailLargeAdapter;
import com.shreyansu.chillout.adapters.MovieDetailSmallAdapter;
import com.shreyansu.chillout.network.movies.MovieDetail;
import com.shreyansu.chillout.network.movies.NowShowingMoviesResponse;
import com.shreyansu.chillout.network.movies.PopularMoviesResponse;
import com.shreyansu.chillout.network.movies.TopRatedMoviesResponse;
import com.shreyansu.chillout.network.movies.genreList;
import com.shreyansu.chillout.network.movies.upcomingMoviesResponse;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.MovieGenre;
import com.shreyansu.chillout.util.connectivityStatus;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class MoviesFragment extends Fragment
{
    private ProgressBar kprogressBar;

    private FrameLayout kNowshowinglayout;
    private FrameLayout kupcominglayout;
    private FrameLayout kpopularlayout;
    private FrameLayout ktopratedlayout;


    private TextView knowShowingAllText;
    private TextView kpopularAllText;
    private TextView kupcomingAllText;
    private TextView ktopratedALLText;


    private boolean knowshowingsectionload;
    private boolean kpopularmovieload;
    private boolean kupcomingmovieload;
    private boolean ktopratedmovieload;

    private RecyclerView knowshowingRecyView;
    private RecyclerView kpopularRecycView;
    private RecyclerView kupcomingRecycView;
    private RecyclerView ktopRatedRecycView;

    private List<MovieDetail> knowShowingmovies;
    private List<MovieDetail> kpopularmovies;
    private List<MovieDetail> kupcomingmovies;
    private List<MovieDetail> ktopratedmovies;


    private MovieDetailLargeAdapter knowshowingAdapter;
    private MovieDetailSmallAdapter kpopularAdapter;
    private MovieDetailLargeAdapter kupcomingAdapter;
    private MovieDetailSmallAdapter ktopRatedAdapter;


    private boolean isFragmentLoaded;
    private Snackbar snackbar;

    private Call<genreList> kgenreListCall;
    private Call<NowShowingMoviesResponse> kNowShowingMovieCall;
    private Call<PopularMoviesResponse> kPopularMovieCall;
    private Call<upcomingMoviesResponse> kupcomingMovieResponse;
    private Call<TopRatedMoviesResponse> ktopRatedMovieResponse;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movies, container, false);


        kprogressBar=(ProgressBar)view.findViewById(R.id.progress_bar);
        kprogressBar.setVisibility(view.GONE);

        knowshowingsectionload=false;
        kpopularmovieload=false;
        kupcomingmovieload=false;
        ktopratedmovieload=false;

        kNowshowinglayout=(FrameLayout)view.findViewById(R.id.now_showing);
        kpopularlayout=(FrameLayout)view.findViewById(R.id.popular);
        kupcominglayout=(FrameLayout)view.findViewById(R.id.upcoming);
        ktopratedlayout=(FrameLayout)view.findViewById(R.id.top_rated);

        knowShowingAllText=(TextView)view.findViewById(R.id.all_showing_now);
        kupcomingAllText=(TextView)view.findViewById(R.id.text_view_all_upcoming);
        kpopularAllText=(TextView)view.findViewById(R.id.text_view_all_popular);
        ktopratedALLText=(TextView)view.findViewById(R.id.all_top_rated);


        knowshowingRecyView=(RecyclerView)view.findViewById(R.id.recycler_view_now_showing);
        (new LinearSnapHelper()).attachToRecyclerView(knowshowingRecyView);

        kpopularRecycView=(RecyclerView)view.findViewById(R.id.recycler_view_popular);
        kupcomingRecycView=(RecyclerView)view.findViewById(R.id.recycler_view_upcoming);
        (new LinearSnapHelper()).attachToRecyclerView(kupcomingRecycView);

        ktopRatedRecycView=(RecyclerView)view.findViewById(R.id.recycler_view_top_rated);

        knowShowingmovies=new ArrayList<>();
        kpopularmovies=new ArrayList<>();
        kupcomingmovies=new ArrayList<>();
        ktopratedmovies=new ArrayList<>();


        knowshowingAdapter=new MovieDetailLargeAdapter(getContext(),knowShowingmovies);;
        kpopularAdapter=new MovieDetailSmallAdapter(getContext(),kpopularmovies);
        kupcomingAdapter=new MovieDetailLargeAdapter(getContext(),kupcomingmovies);
        ktopRatedAdapter=new MovieDetailSmallAdapter(getContext(),ktopratedmovies);


        knowshowingRecyView.setAdapter(knowshowingAdapter);
        knowshowingRecyView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        kpopularRecycView.setAdapter(kpopularAdapter);
        kpopularRecycView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        kupcomingRecycView.setAdapter(kupcomingAdapter);
        kupcomingRecycView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        ktopRatedRecycView.setAdapter(kupcomingAdapter);
        ktopRatedRecycView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


        knowShowingAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!connectivityStatus.isConnected(getContext()))
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE,Constants.NOW_SHOWING_MOVIES_TYPE);
                startActivity(intent);

            }
        });

        kupcomingAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!connectivityStatus.isConnected(getContext()))
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE,Constants.UPCOMING_MOVIES_TYPE);
                startActivity(intent);

            }
        });

        kpopularAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!connectivityStatus.isConnected(getContext()))
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE,Constants.POPULAR_MOVIES_TYPE);
                startActivity(intent);

            }
        });

        ktopratedALLText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!connectivityStatus.isConnected(getContext()))
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), ViewAllMoviesActivity.class);
                intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE,Constants.TOP_RATED_MOVIES_TYPE);
                startActivity(intent);

            }
        });
        if(connectivityStatus.isConnected(getContext()))
        {
            isFragmentLoaded=true;
            loadFragment();
        }

        return view;
    }

    @Override
    public void onStart()
    {
        knowshowingAdapter.notifyDataSetChanged();
        kupcomingAdapter.notifyDataSetChanged();
        kpopularAdapter.notifyDataSetChanged();
        ktopRatedAdapter.notifyDataSetChanged();

        super.onStart();
    }

    @Override
    public void onResume()
    {
        //TODO BROADCASTRECIEVER
        super.onResume();
        if(!isFragmentLoaded && !connectivityStatus.isConnected(getContext()))
        {
            snackbar=Snackbar.make(getActivity().findViewById(R.id.main_activity_fragment),"No Internet Connection", BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.show();
        }



    }
    @Override
    public void onPause()
    {
        super.onPause();
        snackbar.dismiss();
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if(kgenreListCall!=null)
            kgenreListCall.cancel();
        if(kNowShowingMovieCall!=null)
            kNowShowingMovieCall.cancel();
        if(kupcomingMovieResponse!=null)
            kupcomingMovieResponse.cancel();
        if(ktopRatedMovieResponse!=null)
            ktopRatedMovieResponse.cancel();
        if (kPopularMovieCall!=null)
            kPopularMovieCall.cancel();
    }

    public void loadFragment()
    {
        if(MovieGenre.isgenreLoaded())
        {
            loadNowShowingMovies();
            loadPopularMovies();
            loadUpcomingMovies();
            loadTopRatedMovies();
        }
        else
        {
            //TODO 16-12


        }
    }
}