package com.shreyansu.chillout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.shreyansu.chillout.BroadCastConnectivtiy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.adapters.MovieCastsAdapter;
import com.shreyansu.chillout.adapters.MovieDetailSmallAdapter;
import com.shreyansu.chillout.adapters.VideoAdapater;
import com.shreyansu.chillout.network.movies.CreditMovieResponse;
import com.shreyansu.chillout.network.movies.Movie;
import com.shreyansu.chillout.network.movies.MovieCastDetail;
import com.shreyansu.chillout.network.movies.MovieDetail;
import com.shreyansu.chillout.network.movies.SimilarMovieResponse;
import com.shreyansu.chillout.network.tvshows.CastPerson;
import com.shreyansu.chillout.network.videos.Video;
import com.shreyansu.chillout.network.videos.VideoResponse;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.connectivityStatus;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class getMovieDetails extends AppCompatActivity
{
    private AppBarLayout kAppBarlayout;
    private CollapsingToolbarLayout kCollapsingToolbar;
    private Toolbar kToolbar;


    private ConstraintLayout kMovieLayout;
    private ImageView kBackdropImageView;
    private int BackDropHeight;
    private int BackDropwidth;
    private AVLoadingIndicatorView kBackDropProgressBar;
    private ImageView kPosterImageView;
    private int PosterHeight;
    private int PosterWidth;
    private AVLoadingIndicatorView kPosterProgressBar;

    private TextView kTitletext;
    private TextView kGenretext;
    private TextView kYeartext;

    private ImageButton kFavImageButton;
    private ImageButton kShareImageButton;
    private ImageButton kBackImageButton;



    private LinearLayout kratingLayout;
    private TextView kRatingText;

    private TextView kdescription;
    private TextView ReadMoreDescription;

    private LinearLayout kDetailsLayout;
    private TextView kDetailstext;

    private TextView kMovieTrailer;
    private RecyclerView kTrailerRecyclerView;
    private List<Video> kTrailers;
    private VideoAdapater kTrailerAdapter;


    private TextView kCastDetail;
    private RecyclerView kCastRecylerview;
    private List<MovieCastDetail> kCasts;
    private MovieCastsAdapter kCastAdapter;

    private TextView simimlarMoviesText;
    private RecyclerView kSimilarRecyclerView;
    private List<MovieDetail> ksimilarmovies;
    private MovieDetailSmallAdapter kSimilarMovieAdapter;

    private Call<Movie> kMovieDetailCall;
    private Call<VideoResponse> kMovieTrailerCall;
    private Call<CreditMovieResponse> kMovieCreditCall;
    private Call<SimilarMovieResponse> kSimilarMovieCall;

    private Snackbar snackbar;
    private BroadCastConnectivtiy kConnectivityBroadCastReciever;
    private Boolean isBroadCastRecieverRegistered;
    private boolean isActivityLoaded;

    private int kMovieid;
    private View line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_movie_details);

        kToolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(kToolbar);
        setTitle("");

        kMovieid=getIntent().getIntExtra(Constants.MOVIE_ID,-1);
        if(kMovieid==-1)
            finish();

        kCollapsingToolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
        kAppBarlayout=(AppBarLayout)findViewById(R.id.app_bar_layout);


        PosterWidth=(int) (getResources().getDisplayMetrics().widthPixels*0.25);
        PosterHeight=(int)(PosterWidth*0.66);

        BackDropwidth=(int) (getResources().getDisplayMetrics().widthPixels);
        BackDropHeight=(int)(BackDropwidth*1.77);

        kMovieLayout=(ConstraintLayout)findViewById(R.id.layout_collapse_bar_layout);
        kMovieLayout.getLayoutParams().height=BackDropHeight + (int)(PosterHeight*0.9);

        kBackdropImageView=(ImageView)findViewById(R.id.movie_poster_backdrop);
        kBackdropImageView.getLayoutParams().height=BackDropHeight;
        kBackdropImageView.getLayoutParams().width=BackDropwidth;
        kBackDropProgressBar=(AVLoadingIndicatorView) findViewById(R.id.loading_backdrop);
        kBackDropProgressBar.setVisibility(View.GONE);

        kPosterImageView=(ImageView) findViewById(R.id.image_view_poster);
        kPosterImageView.getLayoutParams().height=PosterHeight;
        kPosterImageView.getLayoutParams().width=PosterWidth;
        kPosterProgressBar=(AVLoadingIndicatorView)findViewById(R.id.loading_poster);
        kPosterProgressBar.setVisibility(View.GONE);

        kTitletext=(TextView)findViewById(R.id.title_movie_detail);
        kYeartext=(TextView)findViewById(R.id.year_movie_detail);
        kGenretext=(TextView)findViewById(R.id.genre_movie_detail);


        kFavImageButton=(ImageButton)findViewById(R.id.fav_movie_detail);
        kShareImageButton=(ImageButton) findViewById(R.id.share_movie_detail);
        kBackImageButton=(ImageButton)findViewById(R.id.back_button_movie_detail);

        kBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        kratingLayout=(LinearLayout) findViewById(R.id.rating_detail);
        kRatingText=(TextView) findViewById(R.id.rating_movie_text);

        kdescription=(TextView)findViewById(R.id.movie_description);
        ReadMoreDescription=(TextView)findViewById(R.id.read_more);

        kDetailsLayout=(LinearLayout)findViewById(R.id.release_runtime_detail);
        kDetailstext=(TextView)findViewById(R.id.text_view_detail_movie_detail);

        kMovieTrailer=(TextView)findViewById(R.id.trailer_text);
        kTrailerRecyclerView=(RecyclerView)findViewById(R.id.trailer_recyc_view);
        kTrailers=new ArrayList<>();
        kTrailerAdapter=new VideoAdapater(kTrailers,getMovieDetails.this);
        kTrailerRecyclerView.setAdapter(kTrailerAdapter);
        kTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(getMovieDetails.this,LinearLayoutManager.HORIZONTAL,false));

        line=(View)findViewById(R.id.horizontal_line);

        kCastDetail=(TextView)findViewById(R.id.cast_detail_text);
        kCastRecylerview=(RecyclerView)findViewById(R.id.cast_movie_detail_rec_view);
        kCasts=new ArrayList<>();
        kCastAdapter=new MovieCastsAdapter(getMovieDetails.this,kCasts);
        kCastRecylerview.setAdapter(kCastAdapter);
        kCastRecylerview.setLayoutManager(new LinearLayoutManager(getMovieDetails.this,LinearLayoutManager.HORIZONTAL,false));


        simimlarMoviesText=(TextView) findViewById(R.id.similar_movie_text);
        kSimilarRecyclerView=(RecyclerView)findViewById(R.id.similar_movie_reecyc_view);
        ksimilarmovies=new ArrayList<>();
        kSimilarMovieAdapter=new MovieDetailSmallAdapter(getMovieDetails.this,ksimilarmovies);
        kSimilarRecyclerView.setAdapter(kSimilarMovieAdapter);
        kSimilarRecyclerView.setLayoutManager(new LinearLayoutManager(getMovieDetails.this,LinearLayoutManager.HORIZONTAL,false));



        if(connectivityStatus.isConnected(getMovieDetails.this))
        {
            isActivityLoaded=true;
            loadActivity();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        kSimilarMovieAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(kMovieDetailCall!=null)
            kMovieDetailCall.cancel();
        if(kSimilarMovieCall!=null)
            kSimilarMovieCall.cancel();
        if(kMovieCreditCall!=null)
            kMovieCreditCall.cancel();
        if(kMovieTrailerCall!=null)
            kMovieTrailerCall.cancel();
    }

    private void loadActivity() {
    }
}