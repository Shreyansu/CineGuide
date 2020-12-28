package com.shreyansu.chillout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.shreyansu.chillout.R;
import com.shreyansu.chillout.adapters.MovieDetailSmallAdapter;
import com.shreyansu.chillout.network.ApiClient;
import com.shreyansu.chillout.network.ApiInterface;
import com.shreyansu.chillout.network.movies.Movie;
import com.shreyansu.chillout.network.movies.MovieDetail;
import com.shreyansu.chillout.network.movies.NowShowingMoviesResponse;
import com.shreyansu.chillout.network.movies.PopularMoviesResponse;
import com.shreyansu.chillout.network.movies.TopRatedMoviesResponse;
import com.shreyansu.chillout.network.movies.upcomingMoviesResponse;
import com.shreyansu.chillout.util.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllMoviesActivity extends AppCompatActivity
{
    private RecyclerView krecycView;
    private List<MovieDetail> kmovies;
    private MovieDetailSmallAdapter kmovieAdapter;

    private int kmovieType;

    private boolean pageOver=false;
    private int pagepresent=1;
    private boolean loading=true;
    private int prevTotal=0;
    private int VisibleThreshold=5;

    private Call<NowShowingMoviesResponse> knowShowingMovieCall;
    private Call<PopularMoviesResponse> kPopularMovieCall;
    private Call<upcomingMoviesResponse> kUpcomingMovieCall;
    private Call<TopRatedMoviesResponse> kTopRatedMovieCall;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_movies);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kmovieType=getIntent().getIntExtra(Constants.VIEW_ALL_MOVIES_TYPE,-1);

        if(kmovieType==-1)
            finish();
        switch (kmovieType)
        {
            case Constants.NOW_SHOWING_MOVIES_TYPE:
                setTitle("Now Showing Movies");
                break;

            case Constants.POPULAR_MOVIES_TYPE:
                setTitle("Popular Movies");
                break;
            case Constants.TOP_RATED_MOVIES_TYPE:
                setTitle("Top Rated Movies");
                break;
            case Constants.UPCOMING_MOVIES_TYPE:
                setTitle("Upcoming Movies");
                break;


        }

        krecycView=(RecyclerView) findViewById(R.id.recyc_view_all_content);
        kmovies=new ArrayList<>();
        kmovieAdapter=new MovieDetailSmallAdapter(ViewAllMoviesActivity.this,kmovies);
        krecycView.setAdapter(kmovieAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(ViewAllMoviesActivity.this,3);
        krecycView.setLayoutManager(gridLayoutManager);

        krecycView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount=gridLayoutManager.getChildCount();
                int totalItemCount=gridLayoutManager.getItemCount();
                int firstVisibleItem=gridLayoutManager.findFirstVisibleItemPosition();

                if(loading)
                {
                    if(totalItemCount>prevTotal)
                    {
                        prevTotal=totalItemCount;
                        loading=false;
                        prevTotal=totalItemCount;
                    }
                }
                if(!loading && (totalItemCount-visibleItemCount) <=(firstVisibleItem))
                {
                    loadMovies(kmovieType);
                    loading=true;
                }

            }
        });
        loadMovies(kmovieType);


    }

    @Override
    protected void onStart() {
        super.onStart();
        kmovieAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(knowShowingMovieCall!=null) knowShowingMovieCall.cancel();
        if(kPopularMovieCall!=null) kPopularMovieCall.cancel();
        if(kUpcomingMovieCall!=null) kUpcomingMovieCall.cancel();
        if(kTopRatedMovieCall !=null) kTopRatedMovieCall.cancel();
    }
    private void loadMovies(int movietype)
    {
        if(pageOver)
            return;
        ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        switch(movietype)
        {
            case Constants.NOW_SHOWING_MOVIES_TYPE:
                knowShowingMovieCall=apiservice.getNowShowing(getResources().getString(R.string.MOVIE_DB_API_KEY),pagepresent,"IN");
                knowShowingMovieCall.enqueue(new Callback<NowShowingMoviesResponse>() {
                    @Override
                    public void onResponse(Call<NowShowingMoviesResponse> call, Response<NowShowingMoviesResponse> response) {
                        if(!response.isSuccessful())
                        {
                            knowShowingMovieCall=call.clone();
                            knowShowingMovieCall.enqueue(this);
                            return;
                        }
                        if(response.body()==null)
                            return;
                        if(response.body().getResults()==null)
                            return;

                        for(MovieDetail movieDetail: response.body().getResults())
                        {
                            if(movieDetail!=null && movieDetail.getTitle()!=null)
                                kmovies.add(movieDetail);
                        }
                        kmovieAdapter.notifyDataSetChanged();
                        if(response.body().getPage()==response.body().getTotalPages())
                            pageOver=true;
                        else
                            pagepresent++;



                    }

                    @Override
                    public void onFailure(Call<NowShowingMoviesResponse> call, Throwable t) {

                    }
                });
                break;

            case Constants.POPULAR_MOVIES_TYPE:
                kPopularMovieCall=apiservice.getPopularMovie(getResources().getString(R.string.MOVIE_DB_API_KEY),pagepresent,"IN");
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
                        for(MovieDetail movieDetail: response.body().getResults())
                        {
                            if(movieDetail!=null && movieDetail.getTitle()!=null)
                                kmovies.add(movieDetail);
                        }
                        kmovieAdapter.notifyDataSetChanged();
                        if(response.body().getPage()==response.body().getTotalPages())
                            pageOver=true;
                        else
                            pagepresent++;
                    }

                    @Override
                    public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {

                    }
                });
                break;
            case Constants.UPCOMING_MOVIES_TYPE:
                kUpcomingMovieCall=apiservice.getUpcomingMovie(getResources().getString(R.string.MOVIE_DB_API_KEY),pagepresent,"IN");
                kUpcomingMovieCall.enqueue(new Callback<upcomingMoviesResponse>() {
                    @Override
                    public void onResponse(Call<upcomingMoviesResponse> call, Response<upcomingMoviesResponse> response) {
                        if(!response.isSuccessful())
                        {
                            kUpcomingMovieCall=call.clone();
                            kUpcomingMovieCall.enqueue(this);
                            return;
                        }
                        if(response.body()==null)
                            return;
                        if(response.body().getResults()==null)
                            return;
                        for(MovieDetail movieDetail: response.body().getResults())
                        {
                            if(movieDetail!=null && movieDetail.getTitle()!=null)
                                kmovies.add(movieDetail);
                        }
                        kmovieAdapter.notifyDataSetChanged();
                        if(response.body().getPage()==response.body().getTotalPages())
                            pageOver=true;
                        else
                            pagepresent++;

                    }

                    @Override
                    public void onFailure(Call<upcomingMoviesResponse> call, Throwable t) {

                    }
                });
                break;

                case Constants.TOP_RATED_MOVIES_TYPE:
                    kTopRatedMovieCall=apiservice.getTopRatedMovie(getResources().getString(R.string.MOVIE_DB_API_KEY),pagepresent,"IN");
                    kTopRatedMovieCall.enqueue(new Callback<TopRatedMoviesResponse>() {
                        @Override
                        public void onResponse(Call<TopRatedMoviesResponse> call, Response<TopRatedMoviesResponse> response) {
                            if(!response.isSuccessful())
                            {
                                kTopRatedMovieCall=call.clone();
                                kTopRatedMovieCall.enqueue(this);
                            }

                            if(response.body()==null)
                                return;
                            if(response.body().getResults()==null)
                                return;
                            for(MovieDetail movieDetail: response.body().getResults())
                            {
                                if(movieDetail!=null && movieDetail.getTitle()!=null)
                                    kmovies.add(movieDetail);
                            }
                            kmovieAdapter.notifyDataSetChanged();
                            if(response.body().getPage()==response.body().getTotalPages())
                                pageOver=true;
                            else
                                pagepresent++;
                        }

                        @Override
                        public void onFailure(Call<TopRatedMoviesResponse> call, Throwable t) {

                        }
                    });
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()== android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}