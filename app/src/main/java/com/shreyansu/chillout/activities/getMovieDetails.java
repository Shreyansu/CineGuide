package com.shreyansu.chillout.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.shreyansu.chillout.BroadCastConnectivtiy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.adapters.MovieCastsAdapter;
import com.shreyansu.chillout.adapters.MovieDetailSmallAdapter;
import com.shreyansu.chillout.adapters.VideoAdapater;
import com.shreyansu.chillout.network.ApiClient;
import com.shreyansu.chillout.network.ApiInterface;
import com.shreyansu.chillout.network.movies.CreditMovieResponse;
import com.shreyansu.chillout.network.movies.Genre;
import com.shreyansu.chillout.network.movies.Movie;
import com.shreyansu.chillout.network.movies.MovieCastDetail;
import com.shreyansu.chillout.network.movies.MovieDetail;
import com.shreyansu.chillout.network.movies.SimilarMovieResponse;
import com.shreyansu.chillout.network.tvshows.CastPerson;
import com.shreyansu.chillout.network.videos.Video;
import com.shreyansu.chillout.network.videos.VideoResponse;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.Favourite;
import com.shreyansu.chillout.util.connectivityStatus;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        PosterHeight=(int)(PosterWidth/0.66);

        BackDropwidth=(int) (getResources().getDisplayMetrics().widthPixels);
        BackDropHeight=(int)(BackDropwidth/1.77);

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
    protected void onResume()
    {

        super.onResume();
        if(!isActivityLoaded && !connectivityStatus.isConnected(getMovieDetails.this))
        {
            snackbar=Snackbar.make(kTitletext,"No Internet Connection", BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.show();
            kConnectivityBroadCastReciever=new BroadCastConnectivtiy(new BroadCastConnectivtiy.connectivityRecieverListener() {
                @Override
                public void OnNetworkConnectionConnected() {
                        snackbar.dismiss();;
                        isActivityLoaded=true;
                        loadActivity();
                        isBroadCastRecieverRegistered=false;
                        unregisterReceiver(kConnectivityBroadCastReciever);
                }
            });
            IntentFilter intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            isBroadCastRecieverRegistered=true;
            registerReceiver(kConnectivityBroadCastReciever,intentFilter);

        }

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

    private void loadActivity()
    {
        ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);

        kPosterProgressBar.setVisibility(View.VISIBLE);
        kBackDropProgressBar.setVisibility(View.VISIBLE);

        kMovieDetailCall=apiService.getMovieDetails(kMovieid,getResources().getString(R.string.MOVIE_DB_API_KEY));
        kMovieDetailCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, final Response<Movie> response)
            {
                if(!response.isSuccessful())
                {
                    kMovieDetailCall=call.clone();
                    kMovieDetailCall.enqueue(this);
                    return;

                }
                if(response.body()==null)
                    return;
                kAppBarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if(appBarLayout.getTotalScrollRange() +verticalOffset==0)
                        {
                            if(response.body().getTitle()!=null)
                                kCollapsingToolbar.setTitle(response.body().getTitle());
                            else
                                kCollapsingToolbar.setTitle("");
                            kToolbar.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            kCollapsingToolbar.setTitle("");
                            kToolbar.setVisibility(View.INVISIBLE);
                        }

                    }
                });

                Glide.with(getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500+response.body().getPosterPath())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                kPosterProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                kPosterProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(kPosterImageView);


                Glide.with(getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500+response.body().getBackdropPath())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                kBackDropProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                kBackDropProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(kBackdropImageView);


                if(response.body().getTitle()==null)
                    kTitletext.setText(response.body().getTitle());
                else
                    kTitletext.setText("");



                SetGenres(response.body().getGenres());
                //todo
                SetYear(response.body().getReleaseDate());

                kFavImageButton.setVisibility(View.VISIBLE);
                kShareImageButton.setVisibility(View.VISIBLE);

                setImageButtons(response.body().getId(),response.body().getPosterPath(),response.body().getTitle(),response.body().getTagline(),response.body().getImdbId(),response.body().getHomepage());

                if(response.body().getVoteAverage()!=null && response.body().getVoteAverage()!=0)
                {
                    kratingLayout.setVisibility(View.VISIBLE);
                    kRatingText.setText(String.format("%.1f",response.body().getVoteAverage()));
                }

                if(response.body().getOverview() !=null && !response.body().getOverview().trim().isEmpty())
                {
                    kdescription.setVisibility(View.VISIBLE);
                    kdescription.setText(response.body().getOverview());
                    ReadMoreDescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            kdescription.setMaxLines(7);
                            kDetailsLayout.setVisibility(View.VISIBLE);
                            ReadMoreDescription.setVisibility(View.GONE);
                        }
                    });
                }
                else
                    kdescription.setText("");

                setDetails(response.body().getReleaseDate(),response.body().getRuntime());
                setTrailer();
                line.setVisibility(View.VISIBLE);
                setCasts();
                getSimilarMovies();

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    private void getSimilarMovies()
    {
        ApiInterface apiService=ApiClient.getClient().create(ApiInterface.class);
        kSimilarMovieCall=apiService.getSimilarMovies(kMovieid,getResources().getString(R.string.MOVIE_DB_API_KEY),1);
        kSimilarMovieCall.enqueue(new Callback<SimilarMovieResponse>() {
            @Override
            public void onResponse(Call<SimilarMovieResponse> call, Response<SimilarMovieResponse> response) {
                if(!response.isSuccessful())
                {
                    kSimilarMovieCall=call.clone();
                    kSimilarMovieCall.enqueue(this);
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;

                for(MovieDetail details:response.body().getResults())
                {
                    if(details!=null && details.getTitle()!=null && details.getPosterPath()!=null)
                        ksimilarmovies.add(details);

                }
                if(!ksimilarmovies.isEmpty())
                    simimlarMoviesText.setVisibility(View.VISIBLE);
                kSimilarMovieAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<SimilarMovieResponse> call, Throwable t) {

            }
        });
    }

    private void setCasts()
    {
        kCastDetail.setVisibility(View.VISIBLE);
        ApiInterface apiService=ApiClient.getClient().create(ApiInterface.class);
        kMovieCreditCall=apiService.getMovieCredits(kMovieid,getResources().getString(R.string.MOVIE_DB_API_KEY));
        kMovieCreditCall.enqueue(new Callback<CreditMovieResponse>() {
            @Override
            public void onResponse(Call<CreditMovieResponse> call, Response<CreditMovieResponse> response) {
                if(!response.isSuccessful())
                {
                    kMovieCreditCall=call.clone();
                    kMovieCreditCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getCasts()==null)
                    return;

                for (MovieCastDetail casts:response.body().getCasts())
                {
                    if(casts!=null && casts.getName()!=null)
                        kCasts.add(casts);

                }
//                if(!kCasts.isEmpty())
//                    kCastDetail.setVisibility(View.VISIBLE);
                kCastAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<CreditMovieResponse> call, Throwable t) {

            }
        });

    }

    private void setTrailer()
    {
        ApiInterface apiService=ApiClient.getClient().create(ApiInterface.class);
        kMovieTrailerCall=apiService.getMovieVideos(kMovieid,getResources().getString(R.string.MOVIE_DB_API_KEY));
        kMovieTrailerCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response)
            {
                if(!response.isSuccessful())
                {
                    kMovieTrailerCall=call.clone();
                    kMovieTrailerCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getVideos()==null)
                    return;
                for(Video video: response.body().getVideos())
                {
                    if(video!=null && video.getSite()!=null && video.getSite().equals("YouTube") && video.getType()!=null && video.getType().equals("Trailer"))
                    {
                        kTrailers.add(video);
                    }
                    if(!kTrailers.isEmpty())
                        kMovieTrailer.setVisibility(View.VISIBLE);
                    kTrailerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });
    }

    private void setDetails(String releaseDate, Integer runtime)
    {
        String details="";

        if(releaseDate !=null && !releaseDate.trim().isEmpty())
        {
            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
            try{
                Date releaseDay=sdf1.parse(releaseDate);
                details=sdf2.format(releaseDay)+"\n";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            details="-\n";
        }
        if(runtime!=null && runtime!=0)
        {
            if(runtime<60)
                details+=runtime+"min(s)";
            else
                details+=runtime/60+"hr"+runtime%60+"min(s)";
        }
        else
            details+="-";
        kDetailstext.setText(details);
    }

    private void setImageButtons(final Integer id,final String posterPath,final String title,final String tagline, final String imdbId,final String homepage)
    {
        if(id==null)
            return;
        if(Favourite.isFavMovie(getMovieDetails.this,id))
        {
            kFavImageButton.setTag(0);
            kFavImageButton.setImageResource(R.drawable.ic_facourite);
        }
        else
        {
            kFavImageButton.setTag(1);
            kFavImageButton.setImageResource(R.drawable.ic_favourite);
        }
        kShareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent shareIntent= new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String message="";
                if(title!=null)
                    message+=title+"\n";
                if(tagline!=null)
                    message+=tagline+"\n";
                if(imdbId!=null)
                    message+=Constants.IMDB_BASE_URL+imdbId+"\n";
                if(homepage!=null)
                    message+=homepage;
                shareIntent.putExtra(Intent.EXTRA_TEXT,message);
                startActivity(shareIntent);
            }
        });
        kFavImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(kFavImageButton.getTag().equals(0))
                {
                    Favourite.removeMoviefromfavourite(getMovieDetails.this,kMovieid);
                    kFavImageButton.setTag(1);
                    kFavImageButton.setImageResource(R.drawable.ic_favourite);
                }
                else
                {
                    Favourite.addMovieToFav(getMovieDetails.this,kMovieid,posterPath,title);
                    kFavImageButton.setTag(0);
                    kFavImageButton.setImageResource(R.drawable.ic_facourite);
                }

            }
        });

    }

    private void SetYear(String releaseDate)
    {
        if(releaseDate !=null && !releaseDate.trim().isEmpty())
        {
            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
            try{
                Date releaseDay=sdf1.parse(releaseDate);
                kYeartext.setText(sdf2.format(releaseDay));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            kYeartext.setText("");
        }
    }

    private void SetGenres(List<Genre> genresList)
    {
        String genre="";
        if(genresList!=null)
        {
            for(int i=0;i<genresList.size();i++)
            {
                if(genresList.get(i)==null)
                    continue;
                if(i==genresList.size()-1)
                    genre=genre.concat(genresList.get(i).getGenreName());
                else
                    genre=genre.concat(genresList.get(i).getGenreName()+", ");
            }
        }
        kGenretext.setText(genre);
    }
}
