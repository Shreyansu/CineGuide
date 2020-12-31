package com.shreyansu.chillout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.shreyansu.chillout.R;
import com.shreyansu.chillout.adapters.ShowsDetailSmallAdapter;
import com.shreyansu.chillout.network.ApiClient;
import com.shreyansu.chillout.network.ApiInterface;
import com.shreyansu.chillout.network.tvshows.ShowAirtodayResponse;
import com.shreyansu.chillout.network.tvshows.ShowDetail;
import com.shreyansu.chillout.network.tvshows.ShowOnAirResponse;
import com.shreyansu.chillout.network.tvshows.ShowPopularResponse;
import com.shreyansu.chillout.network.tvshows.ShowTopRatedResponse;
import com.shreyansu.chillout.util.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllShowActivity extends AppCompatActivity
{
    private RecyclerView krecycView;
    private List<ShowDetail> kshows;
    private ShowsDetailSmallAdapter kshowsAdapter;


    private Call<ShowAirtodayResponse> kAiringTodayShowsCall;
    private Call<ShowOnAirResponse> kOnAirShowCall;
    private Call<ShowPopularResponse> kPopularShowCall;
    private Call<ShowTopRatedResponse> ktopRatedShowCall;



    private boolean buffering =true;
    private int pageNow=1;
    private int previousTotal=0;
    private int visibleThreshold=5;
    private boolean lastPage=false;

    private int kShowsType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_show);


//        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kShowsType=getIntent().getIntExtra(Constants.VIEW_ALL_SHOWS_TYPE,-1);

        if(kShowsType==-1)
            finish();
        switch(kShowsType)
        {
            case Constants.AIRING_TODAY_SHOWS_TYPE:
                setTitle("Airing Today TV Shows");
                break;

            case Constants.ON_THE_AIR_SHOWS_TYPE:
                setTitle("On Air TV Shows");
                break;

            case Constants.POPULAR_SHOWS_TYPE:
                setTitle("Popular TV Shows");
                break;

            case Constants.TOP_RATED_SHOWS_TYPE:
                setTitle("Top Rated Shows");
                break;
        }

        krecycView=(RecyclerView) findViewById(R.id.recyc_view_all_show);
        kshows=new ArrayList<>();
        kshowsAdapter=new ShowsDetailSmallAdapter(ViewAllShowActivity.this,kshows);
        krecycView.setAdapter(kshowsAdapter);

        final GridLayoutManager gridLayoutManager=new GridLayoutManager(ViewAllShowActivity.this,3);
        krecycView.setLayoutManager(gridLayoutManager);


        krecycView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visiblecount=gridLayoutManager.getChildCount();
                int totalItemCount=gridLayoutManager.getItemCount();
                int firstVisibleItem=gridLayoutManager.findFirstVisibleItemPosition();

                if(buffering)
                {
                    if(totalItemCount>previousTotal)
                    {
                        buffering=false;
                        previousTotal=totalItemCount;
                    }
                }
                if(!buffering && (totalItemCount-visiblecount)<=(firstVisibleItem+visibleThreshold))
                {
                    loadShows(kShowsType);
                    buffering=true;
                }

            }
        });
        loadShows(kShowsType);

    }



    @Override
    protected void onStart() {
        super.onStart();
        kshowsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(kAiringTodayShowsCall!=null)
            kAiringTodayShowsCall.cancel();
        if(kOnAirShowCall!=null)
            kOnAirShowCall.cancel();

        if(kPopularShowCall!=null)
            kPopularShowCall.cancel();
        if(ktopRatedShowCall!=null)
            ktopRatedShowCall.cancel();
    }
    private void loadShows(int kShowsType)
    {
        if(lastPage)
            return;
        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        switch (kShowsType)
        {
            case Constants.AIRING_TODAY_SHOWS_TYPE:
                kAiringTodayShowsCall=apiservice.getAiringTodayShows(getResources().getString(R.string.MOVIE_DB_API_KEY),pageNow);
                kAiringTodayShowsCall.enqueue(new Callback<ShowAirtodayResponse>() {
                    @Override
                    public void onResponse(Call<ShowAirtodayResponse> call, Response<ShowAirtodayResponse> response) {
                        if(!response.isSuccessful())
                        {
                            kAiringTodayShowsCall=call.clone();
                            kAiringTodayShowsCall.enqueue(this);
                            return;
                        }
                        if(response.body()==null)
                            return;
                        if(response.body().getResults()==null)
                            return;


                        for(ShowDetail showDetail: response.body().getResults())
                        {
                            if(showDetail!=null && showDetail.getName()!=null && showDetail.getPosterPath()!=null)
                                kshows.add(showDetail);
                        }
                        kshowsAdapter.notifyDataSetChanged();
                        if(response.body().getPage()==response.body().getTotalPages())
                            lastPage=true;
                        else
                            pageNow++;

                    }

                    @Override
                    public void onFailure(Call<ShowAirtodayResponse> call, Throwable t) {

                    }
                });
                break;

            case Constants.ON_THE_AIR_SHOWS_TYPE:
                kOnAirShowCall=apiservice.getOnAirShows(getResources().getString(R.string.MOVIE_DB_API_KEY),pageNow);
                kOnAirShowCall.enqueue(new Callback<ShowOnAirResponse>() {
                    @Override
                    public void onResponse(Call<ShowOnAirResponse> call, Response<ShowOnAirResponse> response) {

                        if(!response.isSuccessful())
                        {
                            kOnAirShowCall=call.clone();
                            kOnAirShowCall.enqueue(this);
                            return;
                        }
                        if(response.body()==null)
                            return;
                        if(response.body().getResults()==null)
                            return;


                        for(ShowDetail showDetail: response.body().getResults())
                        {
                            if(showDetail!=null && showDetail.getName()!=null && showDetail.getPosterPath()!=null)
                                kshows.add(showDetail);
                        }
                        kshowsAdapter.notifyDataSetChanged();
                        if(response.body().getPage()==response.body().getTotalPages())
                            lastPage=true;
                        else
                            pageNow++;
                    }

                    @Override
                    public void onFailure(Call<ShowOnAirResponse> call, Throwable t) {

                    }
                });
                break;
            case Constants.POPULAR_SHOWS_TYPE:
                kPopularShowCall=apiservice.getPopularShows(getResources().getString(R.string.MOVIE_DB_API_KEY),pageNow);
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


                        for(ShowDetail showDetail: response.body().getResults())
                        {
                            if(showDetail!=null && showDetail.getName()!=null && showDetail.getPosterPath()!=null)
                                kshows.add(showDetail);
                        }
                        kshowsAdapter.notifyDataSetChanged();
                        if(response.body().getPage()==response.body().getTotalPages())
                            lastPage=true;
                        else
                            pageNow++;
                    }

                    @Override
                    public void onFailure(Call<ShowPopularResponse> call, Throwable t) {

                    }
                });
                break;
            case Constants.TOP_RATED_SHOWS_TYPE:
                ktopRatedShowCall=apiservice.getTopRatedShows(getResources().getString(R.string.MOVIE_DB_API_KEY),pageNow);
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


                        for(ShowDetail showDetail: response.body().getResults())
                        {
                            if(showDetail!=null && showDetail.getName()!=null && showDetail.getPosterPath()!=null)
                                kshows.add(showDetail);
                        }
                        kshowsAdapter.notifyDataSetChanged();
                        if(response.body().getPage()==response.body().getTotalPages())
                            lastPage=true;
                        else
                            pageNow++;
                    }

                    @Override
                    public void onFailure(Call<ShowTopRatedResponse> call, Throwable t) {

                    }
                });
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}