package com.shreyansu.chillout.fragments;

import android.content.Intent;
import android.content.IntentFilter;
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
import com.shreyansu.chillout.BroadCastConnectivtiy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.activities.ViewAllMoviesActivity;
import com.shreyansu.chillout.adapters.MovieDetailLargeAdapter;
import com.shreyansu.chillout.adapters.MovieDetailSmallAdapter;
import com.shreyansu.chillout.network.ApiClient;
import com.shreyansu.chillout.network.ApiInterface;
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
import retrofit2.Callback;
import retrofit2.Response;


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
    private Call<upcomingMoviesResponse> kupcomingMoviecall;
    private Call<TopRatedMoviesResponse> ktopRatedMovieCall;
    private BroadCastConnectivtiy kConnectivityReciever;
    private boolean isBroadCastRegistered;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movies, container, false);




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

        kprogressBar=(ProgressBar)view.findViewById(R.id.progress_bar);
        kprogressBar.setVisibility(view.GONE);

        knowshowingRecyView=(RecyclerView)view.findViewById(R.id.recycler_view_now_showing);
//        (new LinearSnapHelper()).attachToRecyclerView(knowshowingRecyView);

        kpopularRecycView=(RecyclerView)view.findViewById(R.id.recycler_view_popular);
        kupcomingRecycView=(RecyclerView)view.findViewById(R.id.recycler_view_upcoming);
//        (new LinearSnapHelper()).attachToRecyclerView(kupcomingRecycView);


        ktopRatedRecycView=(RecyclerView)view.findViewById(R.id.recycler_view_top_rated);

        knowShowingmovies=new ArrayList<>();
        kpopularmovies=new ArrayList<>();
        kupcomingmovies=new ArrayList<>();
        ktopratedmovies=new ArrayList<>();


        knowshowingAdapter=new MovieDetailLargeAdapter(getContext(),knowShowingmovies);
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
        super.onStart();

        knowshowingAdapter.notifyDataSetChanged();
        kupcomingAdapter.notifyDataSetChanged();
        kpopularAdapter.notifyDataSetChanged();
        ktopRatedAdapter.notifyDataSetChanged();


    }

    @Override
    public void onResume()
    {
        //TODO BROADCASTRECIEVER
        super.onResume();
        if(!isFragmentLoaded && !connectivityStatus.isConnected(getContext()))
        {
            snackbar=Snackbar.make(getActivity().findViewById(R.id.main_activity_fragment),"No Internet Connection",
                    BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.show();
            kConnectivityReciever=new BroadCastConnectivtiy(new BroadCastConnectivtiy.connectivityRecieverListener() {
                @Override
                public void OnNetworkConnectionConnected()
                {
                    snackbar.dismiss();
                    isFragmentLoaded=true;
                    loadFragment();
                    getActivity().unregisterReceiver(kConnectivityReciever);

                }
            });
            IntentFilter intentFilter =new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            isBroadCastRegistered=true;
            getActivity().registerReceiver(kConnectivityReciever,intentFilter);

        }
        else if(!isFragmentLoaded && connectivityStatus.isConnected(getContext()))
        {
            isFragmentLoaded=true;
            loadFragment();
        }



    }
    @Override
    public void onPause()
    {
        super.onPause();
        if(isBroadCastRegistered)
        {
            snackbar.dismiss();
            isBroadCastRegistered=false;
            getActivity().unregisterReceiver(kConnectivityReciever);
        }
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if(kgenreListCall!=null)
            kgenreListCall.cancel();
        if(kNowShowingMovieCall!=null)
            kNowShowingMovieCall.cancel();
        if(kupcomingMoviecall!=null)
            kupcomingMoviecall.cancel();
        if(ktopRatedMovieCall!=null)
            ktopRatedMovieCall.cancel();
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
            ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);
            kprogressBar.setVisibility(View.VISIBLE);
            kgenreListCall=apiService.getMovieGenreList(getResources().getString(R.string.MOVIE_DB_API_KEY));
            kgenreListCall.enqueue(new Callback<genreList>() {
                @Override
                public void onResponse(Call<genreList> call, Response<genreList> response)
                {
                    if(!response.isSuccessful())
                    {
                        kgenreListCall=call.clone();
                        kgenreListCall.enqueue(this);
                        return;
                    }
                    if(response.body()==null)
                        return;
                    if(response.body().getGenres()==null)
                        return;

                    MovieGenre.loadGenreList(response.body().getGenres());
                    loadNowShowingMovies();
                    loadPopularMovies();
                    loadUpcomingMovies();
                    loadTopRatedMovies();

                }

                @Override
                public void onFailure(Call<genreList> call, Throwable t) {

                }
            });

        }
    }



    private void loadNowShowingMovies()
    {
        ApiInterface apiService =ApiClient.getClient().create(ApiInterface.class);
        kprogressBar.setVisibility(View.VISIBLE);
        kNowShowingMovieCall=apiService.getNowShowing(getResources().getString(R.string.MOVIE_DB_API_KEY),1,"IN");
        kNowShowingMovieCall.enqueue(new Callback<NowShowingMoviesResponse>()
        {

            @Override
            public void onResponse(Call<NowShowingMoviesResponse> call, Response<NowShowingMoviesResponse> response) {
                if(!response.isSuccessful())
                {
                    kNowShowingMovieCall=call.clone();
                    kNowShowingMovieCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;
                knowshowingsectionload=true;
                checkAllDataLoaded();
                for(MovieDetail movieDetail : response.body().getResults())
                {
                    if(movieDetail!=null && movieDetail.getBackdropPath()!=null)
                        knowShowingmovies.add(movieDetail);
                }
                knowshowingAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NowShowingMoviesResponse> call, Throwable t)
            {


            }
        });

    }

    private void loadPopularMovies()
    {
        ApiInterface apiService =ApiClient.getClient().create(ApiInterface.class);
        kprogressBar.setVisibility(View.VISIBLE);
        kPopularMovieCall=apiService.getPopularMovie(getResources().getString(R.string.MOVIE_DB_API_KEY),1,"IN");
        kPopularMovieCall.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                if(!response.isSuccessful())
                {
                    kPopularMovieCall=call.clone();
                    kPopularMovieCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;
                kpopularmovieload=true;
                checkAllDataLoaded();
                for(MovieDetail movieDetail : response.body().getResults())
                {
                    if(movieDetail!=null && movieDetail.getPosterPath()!=null)
                        kpopularmovies.add(movieDetail);
                }
                kpopularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {

            }
        });
    }




    private void loadUpcomingMovies()
    {
        ApiInterface apiService =ApiClient.getClient().create(ApiInterface.class);
        kprogressBar.setVisibility(View.VISIBLE);
        kupcomingMoviecall=apiService.getUpcomingMovie(getResources().getString(R.string.MOVIE_DB_API_KEY),1,"IN");
        kupcomingMoviecall.enqueue(new Callback<upcomingMoviesResponse>() {
            @Override
            public void onResponse(Call<upcomingMoviesResponse> call, Response<upcomingMoviesResponse> response) {
                if(!response.isSuccessful())
                {
                    kupcomingMoviecall = call.clone();
                    kupcomingMoviecall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;
                kupcomingmovieload=true;
                checkAllDataLoaded();
                for(MovieDetail movieDetail : response.body().getResults())
                {
                    if(movieDetail!=null && movieDetail.getBackdropPath()!=null)
                        kupcomingmovies.add(movieDetail);
                }
                kupcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<upcomingMoviesResponse> call, Throwable t) {

            }
        });


    }

    private void loadTopRatedMovies()
    {
        ApiInterface apiService =ApiClient.getClient().create(ApiInterface.class);
        kprogressBar.setVisibility(View.VISIBLE);
        ktopRatedMovieCall =apiService.getTopRatedMovie(getResources().getString(R.string.MOVIE_DB_API_KEY),1,"IN");
        ktopRatedMovieCall.enqueue(new Callback<TopRatedMoviesResponse>() {
            @Override
            public void onResponse(Call<TopRatedMoviesResponse> call, Response<TopRatedMoviesResponse> response) {
                if(!response.isSuccessful())
                {
                    ktopRatedMovieCall=call.clone();
                    ktopRatedMovieCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;
                ktopratedmovieload=true;
                checkAllDataLoaded();
                for(MovieDetail movieDetail: response.body().getResults())
                {
                    if(movieDetail !=null && movieDetail.getPosterPath()!=null)
                        ktopratedmovies.add(movieDetail);
                }
                ktopRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TopRatedMoviesResponse> call, Throwable t) {

            }
        });

    }
    private void checkAllDataLoaded()
    {
        if(knowshowingsectionload && kupcomingmovieload && ktopratedmovieload && kpopularmovieload)
        {
            kprogressBar.setVisibility(View.GONE);
            kNowshowinglayout.setVisibility(View.VISIBLE);
            kupcominglayout.setVisibility(View.VISIBLE);
            ktopratedlayout.setVisibility(View.VISIBLE);
            kpopularlayout.setVisibility(View.VISIBLE);
            knowshowingRecyView.setVisibility(View.VISIBLE);
            ktopRatedRecycView.setVisibility(View.VISIBLE);
            kpopularRecycView.setVisibility(View.VISIBLE);
            kupcomingRecycView.setVisibility(View.VISIBLE);
        }
    }

}