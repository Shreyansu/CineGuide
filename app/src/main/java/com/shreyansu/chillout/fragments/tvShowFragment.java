package com.shreyansu.chillout.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.shreyansu.chillout.BroadCastConnectivtiy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.activities.ViewAllShowActivity;
import com.shreyansu.chillout.adapters.ShowDetailLargeAdapter;
import com.shreyansu.chillout.adapters.ShowsDetailSmallAdapter;
import com.shreyansu.chillout.network.ApiClient;
import com.shreyansu.chillout.network.ApiInterface;
import com.shreyansu.chillout.network.tvshows.ShowAirtodayResponse;
import com.shreyansu.chillout.network.tvshows.ShowDetail;
import com.shreyansu.chillout.network.tvshows.ShowOnAirResponse;
import com.shreyansu.chillout.network.tvshows.ShowPopularResponse;
import com.shreyansu.chillout.network.tvshows.ShowTopRatedResponse;
import com.shreyansu.chillout.network.tvshows.*;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.ShowGenre;
import com.shreyansu.chillout.util.connectivityStatus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class tvShowFragment extends Fragment
{
    private ProgressBar kprogressBar;

    private FrameLayout konAirlayout;
    private FrameLayout kAiringtodaylayout;
    private FrameLayout kpopularlayout;
    private FrameLayout ktopratedlayout;


    private TextView konAirAllText;
    private TextView kpopularAllText;
    private TextView kAiringTodayAllText;
    private TextView ktopratedAllText;


    private boolean kAiringTodayload;
    private boolean kpopularShowload;
    private boolean konAirShowload;
    private boolean ktopratedShowload;

    private RecyclerView kAiringTodayRecyView;
    private RecyclerView kpopularRecycView;
    private RecyclerView konAirRecycView;
    private RecyclerView ktopRatedRecycView;

    private List<ShowDetail> kAiringTodayShows;
    private List<ShowDetail> kpopularShows;
    private List<ShowDetail> konAirShows;
    private List<ShowDetail> ktopratedShows;


    private ShowDetailLargeAdapter kAiringAdapter;
    private ShowsDetailSmallAdapter kpopularAdapter;
    private ShowDetailLargeAdapter konAirAdapter;
    private ShowsDetailSmallAdapter ktopRatedAdapter;


    private boolean isFragmentLoaded;
    private Snackbar snackbar;
    private BroadCastConnectivtiy kBroadCastConnectivity;
    private boolean isBroadcastRecieverRegistered=false;

//    private Call<genre> kgenreListCall;
//    private Call<genreList> kgenreListCall;
    private Call<genreList> kgenreListCall;
    private Call<ShowAirtodayResponse> kAiringTodayShowCall;
    private Call<ShowPopularResponse> kPopularShowCall;
    private Call<ShowOnAirResponse> konAirShowcall;
    private Call<ShowTopRatedResponse> ktopRatedShowCall;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);
        kAiringTodayload=false;
        kpopularShowload=false;
        konAirShowload=false;
        ktopratedShowload=false;


        konAirlayout=(FrameLayout)view.findViewById(R.id.on_air_layout);
        kpopularlayout=(FrameLayout)view.findViewById(R.id.popular_layout);
        kAiringtodaylayout=(FrameLayout)view.findViewById(R.id.airing_today_layout);
        ktopratedlayout=(FrameLayout)view.findViewById(R.id.top_rated_layout);


        kAiringTodayAllText=(TextView)view.findViewById(R.id.all_airing_today_text);
        konAirAllText=(TextView)view.findViewById(R.id.all_on_air_text);
        kpopularAllText=(TextView)view.findViewById(R.id.all_popular_text);
        ktopratedAllText=(TextView)view.findViewById(R.id.all_top_rated_text);

        kprogressBar=(ProgressBar)view.findViewById(R.id.progress_bar);
        kprogressBar.setVisibility(view.GONE);


        kAiringTodayRecyView=(RecyclerView)view.findViewById(R.id.rec_view_airing_today);
        (new LinearSnapHelper()).attachToRecyclerView(kAiringTodayRecyView);

        kpopularRecycView=(RecyclerView)view.findViewById(R.id.rec_view_popular);
        konAirRecycView=(RecyclerView)view.findViewById(R.id.rec_view_on_the_air);
        (new LinearSnapHelper()).attachToRecyclerView(konAirRecycView);

        ktopRatedRecycView=(RecyclerView)view.findViewById(R.id.rec_view_top_rated);

        kAiringTodayShows=new ArrayList<>();
        kpopularShows=new ArrayList<>();
        konAirShows=new ArrayList<>();
        ktopratedShows=new ArrayList<>();

        kAiringAdapter= new ShowDetailLargeAdapter(getContext(),kAiringTodayShows);
        kpopularAdapter =new ShowsDetailSmallAdapter(getContext(),kpopularShows);
        konAirAdapter=new ShowDetailLargeAdapter(getContext(),konAirShows);
        ktopRatedAdapter=new ShowsDetailSmallAdapter(getContext(),ktopratedShows);

        kAiringTodayRecyView.setAdapter(kAiringAdapter);
        kAiringTodayRecyView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        kpopularRecycView.setAdapter(kpopularAdapter);
        kpopularRecycView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        konAirRecycView.setAdapter(konAirAdapter);
        konAirRecycView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        ktopRatedRecycView.setAdapter(ktopRatedAdapter);
        ktopRatedRecycView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


        kAiringTodayAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!connectivityStatus.isConnected(getContext()))
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent =new Intent(getContext(), ViewAllShowActivity.class);
                intent.putExtra(Constants.VIEW_ALL_SHOWS_TYPE,Constants.AIRING_TODAY_SHOWS_TYPE);
                startActivity(intent);

            }
        });
        konAirAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!connectivityStatus.isConnected(getContext()))
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent =new Intent(getContext(), ViewAllShowActivity.class);
                intent.putExtra(Constants.VIEW_ALL_SHOWS_TYPE,Constants.ON_THE_AIR_SHOWS_TYPE);
                startActivity(intent);
            }
        });
        kpopularAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if(!connectivityStatus.isConnected(getContext()))
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent =new Intent(getContext(), ViewAllShowActivity.class);
                intent.putExtra(Constants.VIEW_ALL_SHOWS_TYPE,Constants.POPULAR_SHOWS_TYPE);
                startActivity(intent);
            }
        });
        ktopratedAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!connectivityStatus.isConnected(getContext()))
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent =new Intent(getContext(), ViewAllShowActivity.class);
                intent.putExtra(Constants.VIEW_ALL_SHOWS_TYPE,Constants.TOP_RATED_SHOWS_TYPE);
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
    public void onStart() {
        super.onStart();
        kAiringAdapter.notifyDataSetChanged();
        konAirAdapter.notifyDataSetChanged();
        kpopularAdapter.notifyDataSetChanged();
        ktopRatedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(!isFragmentLoaded && !connectivityStatus.isConnected(getContext()))
        {
            snackbar=Snackbar.make(getActivity().findViewById(R.id.main_activity_fragment),"No Internet Connection", BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.show();
            kBroadCastConnectivity=new BroadCastConnectivtiy(new BroadCastConnectivtiy.connectivityRecieverListener() {
                @Override
                public void OnNetworkConnectionConnected()
                {
                    snackbar.dismiss();
                    isFragmentLoaded=true;
                    loadFragment();
                    isBroadcastRecieverRegistered=false;
                    getActivity().unregisterReceiver(kBroadCastConnectivity);

                }
            });
            IntentFilter intentFilter =new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            isBroadcastRecieverRegistered=true;
            getActivity().registerReceiver(kBroadCastConnectivity,intentFilter);
        }
        else if(!isFragmentLoaded && connectivityStatus.isConnected(getContext()))
        {
            isFragmentLoaded=true;
            loadFragment();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isBroadcastRecieverRegistered)
        {
            isBroadcastRecieverRegistered=false;
            snackbar.dismiss();
            getActivity().unregisterReceiver(kBroadCastConnectivity);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if(kgenreListCall!=null)kgenreListCall.cancel();
        if(konAirShowcall!=null)konAirShowcall.cancel();
        if(ktopRatedShowCall!=null)ktopRatedShowCall.cancel();
        if(kPopularShowCall!=null)kPopularShowCall.cancel();
        if(kAiringTodayShowCall!=null) kAiringTodayShowCall.cancel();

    }
    private void loadFragment()
    {
        if(ShowGenre.isGenreListLoaded())
        {
            loadAiringTodayShows();
            loadOnAirShows();
            loadPopularShows();
            loadTopRatedShows();
        }
        else
        {
            ApiInterface  apiService= ApiClient.getClient().create(ApiInterface.class);
            kprogressBar.setVisibility(View.VISIBLE);
            kgenreListCall=apiService.getShowsList(getResources().getString(R.string.MOVIE_DB_API_KEY));
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

                    ShowGenre.loadGenre(response.body().getGenres());
                    loadAiringTodayShows();
                    loadOnAirShows();
                    loadPopularShows();
                    loadTopRatedShows();



                }

                @Override
                public void onFailure(Call<genreList> call, Throwable t) {

                }
            });

        }

    }

    private void loadTopRatedShows()
    {
        ApiInterface apiservice=ApiClient.getClient().create(ApiInterface.class);
        kprogressBar.setVisibility(View.VISIBLE);
        ktopRatedShowCall=apiservice.getTopRatedShows(getResources().getString(R.string.MOVIE_DB_API_KEY),1);
        ktopRatedShowCall.enqueue(new Callback<ShowTopRatedResponse>() {
            @Override
            public void onResponse(Call<ShowTopRatedResponse> call, Response<ShowTopRatedResponse> response) {
                if(!response.isSuccessful())
                {
                    ktopRatedShowCall=call.clone();
                    ktopRatedShowCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;
                ktopratedShowload=true;
                checkAllDataLoaded();
                for(ShowDetail detail : response.body().getResults())
                {
                    if(detail!=null && detail.getPosterPath()!=null)
                        ktopratedShows.add(detail);
                }
                ktopRatedAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ShowTopRatedResponse> call, Throwable t) {

            }
        });
    }

    private void loadPopularShows()
    {
        ApiInterface apiservice=ApiClient.getClient().create(ApiInterface.class);
        kprogressBar.setVisibility(View.VISIBLE);
        kPopularShowCall=apiservice.getPopularShows(getResources().getString(R.string.MOVIE_DB_API_KEY),1);
        kPopularShowCall.enqueue(new Callback<ShowPopularResponse>() {
            @Override
            public void onResponse(Call<ShowPopularResponse> call, Response<ShowPopularResponse> response) {
                if(!response.isSuccessful())
                {
                    kPopularShowCall=call.clone();
                    kPopularShowCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;
                kpopularShowload=true;
                checkAllDataLoaded();
                for(ShowDetail detail: response.body().getResults())
                {
                    if(detail!=null && detail.getBackdropPath()!=null)
                        konAirShows.add(detail);
                }
                konAirAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ShowPopularResponse> call, Throwable t) {

            }
        });
    }


    private void loadOnAirShows()
    {
        ApiInterface apiservice=ApiClient.getClient().create(ApiInterface.class);
        kprogressBar.setVisibility(View.VISIBLE);
        konAirShowcall=apiservice.getOnAirShows(getResources().getString(R.string.MOVIE_DB_API_KEY),1);
        konAirShowcall.enqueue(new Callback<ShowOnAirResponse>() {
            @Override
            public void onResponse(Call<ShowOnAirResponse> call, Response<ShowOnAirResponse> response) {
                if(!response.isSuccessful())
                {
                    konAirShowcall=call.clone();
                    konAirShowcall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;
                konAirShowload=true;
                checkAllDataLoaded();
                for(ShowDetail detail:response.body().getResults())
                {
                    if(detail!=null && detail.getPosterPath()!=null)
                        konAirShows.add(detail);
                }
                konAirAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ShowOnAirResponse> call, Throwable t) {

            }
        });
    }

    private void loadAiringTodayShows()
    {
        ApiInterface apiservice=ApiClient.getClient().create(ApiInterface.class);
        kprogressBar.setVisibility(View.VISIBLE);
        kAiringTodayShowCall=apiservice.getAiringTodayShows(getResources().getString(R.string.MOVIE_DB_API_KEY),1);
        kAiringTodayShowCall.enqueue(new Callback<ShowAirtodayResponse>() {
            @Override
            public void onResponse(Call<ShowAirtodayResponse> call, Response<ShowAirtodayResponse> response) {

                if(!response.isSuccessful())
                {
                    kAiringTodayShowCall=call.clone();
                    kAiringTodayShowCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;
                kAiringTodayload=true;
                checkAllDataLoaded();
                for(ShowDetail detail :response.body().getResults())
                {
                    if(detail!=null && detail.getBackdropPath()!=null)
                        kAiringTodayShows.add(detail);
                }
                kAiringAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ShowAirtodayResponse> call, Throwable t) {

            }
        });

    }
    private void checkAllDataLoaded()
    {
        if(kAiringTodayload && konAirShowload && ktopratedShowload && kpopularShowload)
        {
            kprogressBar.setVisibility(View.GONE);
            kAiringtodaylayout.setVisibility(View.VISIBLE);
            kAiringTodayRecyView.setVisibility(View.VISIBLE);
            konAirlayout.setVisibility(View.VISIBLE);
            konAirRecycView.setVisibility(View.VISIBLE);
            kpopularlayout.setVisibility(View.VISIBLE);
            kpopularRecycView.setVisibility(View.VISIBLE);
            ktopratedlayout.setVisibility(View.VISIBLE);
            ktopRatedRecycView.setVisibility(View.VISIBLE);
        }
    }



}