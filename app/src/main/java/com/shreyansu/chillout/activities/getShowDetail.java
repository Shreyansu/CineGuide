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
import com.shreyansu.chillout.adapters.ShowCastsAdapte;
import com.shreyansu.chillout.adapters.ShowsDetailSmallAdapter;
import com.shreyansu.chillout.adapters.VideoAdapater;
import com.shreyansu.chillout.network.ApiClient;
import com.shreyansu.chillout.network.ApiInterface;
import com.shreyansu.chillout.network.movies.CreditMovieResponse;
import com.shreyansu.chillout.network.movies.Movie;
import com.shreyansu.chillout.network.movies.MovieCastDetail;
import com.shreyansu.chillout.network.movies.MovieDetail;
import com.shreyansu.chillout.network.movies.SimilarMovieResponse;
import com.shreyansu.chillout.network.tvshows.CastDetail;
import com.shreyansu.chillout.network.tvshows.Genre;
import com.shreyansu.chillout.network.tvshows.Network;
import com.shreyansu.chillout.network.tvshows.ShowCreditResponse;
import com.shreyansu.chillout.network.tvshows.ShowDetail;
import com.shreyansu.chillout.network.tvshows.Shows;
import com.shreyansu.chillout.network.tvshows.SimilarShowResponse;
import com.shreyansu.chillout.network.videos.Video;
import com.shreyansu.chillout.network.videos.VideoResponse;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.Favourite;
import com.shreyansu.chillout.util.connectivityStatus;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class getShowDetail extends AppCompatActivity
{

    private AppBarLayout kAppBarlayout;
    private CollapsingToolbarLayout kCollapsingToolbar;
    private Toolbar kToolbar;


    private ConstraintLayout kShowLayout;
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

    private ImageButton kFavShowImageButton;
    private ImageButton kShareShowImageButton;
    private ImageButton kBackShowImageButton;



    private LinearLayout kratingLayout;
    private TextView kRatingText;

    private TextView kdescription;
    private TextView ReadMoreDescription;

    private LinearLayout kDetailsLayout;
    private TextView kDetailstext;

    private TextView kShowTrailer;
    private RecyclerView kShowRecyclerView;
    private List<Video> kTrailers;
    private VideoAdapater kTrailerAdapter;


    private TextView kCastDetail;
    private RecyclerView kCastRecylerview;
    private List<CastDetail> kCasts;
    private ShowCastsAdapte kCastAdapter;

    private TextView simimlarShowsText;
    private RecyclerView kSimilarRecyclerView;
    private List<ShowDetail> ksimilarShow;
    private ShowsDetailSmallAdapter kSimilarShowAdapter;

    private Call<Shows> kShowDetailCall;
    private Call<VideoResponse> kShowTrailerCall;
    private Call<ShowCreditResponse> kShowCreditCall;
    private Call<SimilarShowResponse> kSimilarShowCall;

    private Snackbar snackbar;
    private BroadCastConnectivtiy kConnectivityBroadCastReciever;
    private Boolean isBroadCastRecieverRegistered;
    private boolean isActivityLoaded;

    private int kShowid;
    private View line;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_show_detail);


        kToolbar=(Toolbar) findViewById(R.id.toolbar_shows);
        setSupportActionBar(kToolbar);
        setTitle("");

        kShowid=getIntent().getIntExtra(Constants.TV_SHOW_ID,-1);
        if(kShowid==-1)
            finish();

        kCollapsingToolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout_shows);
        kAppBarlayout=(AppBarLayout)findViewById(R.id.app_bar_layout_shows);


        PosterWidth=(int) (getResources().getDisplayMetrics().widthPixels*0.25);
        PosterHeight=(int)(PosterWidth/0.66);

        BackDropwidth=(int) (getResources().getDisplayMetrics().widthPixels);
        BackDropHeight=(int)(BackDropwidth/1.77);

        kShowLayout=(ConstraintLayout)findViewById(R.id.layout_collapse_bar_shows);
        kShowLayout.getLayoutParams().height=BackDropHeight + (int)(PosterHeight*0.9);

        kBackdropImageView=(ImageView)findViewById(R.id.show_poster_backdrop);
        kBackdropImageView.getLayoutParams().height=BackDropHeight;
        kBackdropImageView.getLayoutParams().width=BackDropwidth;
        kBackDropProgressBar=(AVLoadingIndicatorView) findViewById(R.id.loading_backdrop_show);
        kBackDropProgressBar.setVisibility(View.GONE);

        kPosterImageView=(ImageView) findViewById(R.id.image_view_poster_show);
        kPosterImageView.getLayoutParams().height=PosterHeight;
        kPosterImageView.getLayoutParams().width=PosterWidth;
        kPosterProgressBar=(AVLoadingIndicatorView)findViewById(R.id.loading_poster_show);
        kPosterProgressBar.setVisibility(View.GONE);

        kTitletext=(TextView)findViewById(R.id.title_show_detail);
        kYeartext=(TextView)findViewById(R.id.year_show_detail);
        kGenretext=(TextView)findViewById(R.id.genre_show_detail);


        kFavShowImageButton=(ImageButton)findViewById(R.id.fav_show_detail);
        kShareShowImageButton=(ImageButton) findViewById(R.id.share_show_detail);
        kBackShowImageButton=(ImageButton)findViewById(R.id.back_button_show_detail);

//        kBackShowImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        kratingLayout=(LinearLayout) findViewById(R.id.rating_detail_shows);
        kRatingText=(TextView) findViewById(R.id.rating_show_text);

        kdescription=(TextView)findViewById(R.id.show_description);
        ReadMoreDescription=(TextView)findViewById(R.id.read_more_shows);

        kDetailsLayout=(LinearLayout)findViewById(R.id.release_runtime_detail_shows);
        kDetailstext=(TextView)findViewById(R.id.text_view_detail_show_detail);

        kShowTrailer=(TextView)findViewById(R.id.trailer_text_shows);
        kShowRecyclerView=(RecyclerView)findViewById(R.id.trailer_recyc_view_shows);
        kTrailers=new ArrayList<>();
        kTrailerAdapter=new VideoAdapater(kTrailers,getShowDetail.this);
        kShowRecyclerView.setAdapter(kTrailerAdapter);
        kShowRecyclerView.setLayoutManager(new LinearLayoutManager(getShowDetail.this,LinearLayoutManager.HORIZONTAL,false));

        line=(View)findViewById(R.id.horizontal_line_shows);

        kCastDetail=(TextView)findViewById(R.id.cast_detail_text_shows);
        kCastRecylerview=(RecyclerView)findViewById(R.id.cast_show_detail_rec_view);
        kCasts=new ArrayList<>();
        kCastAdapter=new ShowCastsAdapte(getShowDetail.this,kCasts);
        kCastRecylerview.setAdapter(kCastAdapter);
        kCastRecylerview.setLayoutManager(new LinearLayoutManager(getShowDetail.this,LinearLayoutManager.HORIZONTAL,false));


        simimlarShowsText=(TextView) findViewById(R.id.similar_shows_text);
        kSimilarRecyclerView=(RecyclerView)findViewById(R.id.similar_shows_recyc_view);
        ksimilarShow=new ArrayList<>();
        kSimilarShowAdapter=new ShowsDetailSmallAdapter(getShowDetail.this,ksimilarShow);
        kSimilarRecyclerView.setAdapter(kSimilarShowAdapter);
        kSimilarRecyclerView.setLayoutManager(new LinearLayoutManager(getShowDetail.this,LinearLayoutManager.HORIZONTAL,false));


        if(connectivityStatus.isConnected(getShowDetail.this))
        {
            isActivityLoaded=true;
            loadActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        kSimilarShowAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume()
    {

        super.onResume();
        if(!isActivityLoaded && !connectivityStatus.isConnected(getShowDetail.this))
        {
            snackbar=snackbar.make(kTitletext,"No Internet Connection", BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.show();
            kConnectivityBroadCastReciever=new BroadCastConnectivtiy(new BroadCastConnectivtiy.connectivityRecieverListener() {
                @Override
                public void OnNetworkConnectionConnected()
                {
                    snackbar.dismiss();
                    isActivityLoaded=true;
                    loadActivity();
                    isBroadCastRecieverRegistered=false;
                    unregisterReceiver(kConnectivityBroadCastReciever);

                }
            });
            IntentFilter intentFilter =new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            isBroadCastRecieverRegistered=true;
            registerReceiver(kConnectivityBroadCastReciever,intentFilter);

        }
        else if(!isActivityLoaded && connectivityStatus.isConnected(getShowDetail.this))
        {
            isActivityLoaded=true;
            loadActivity();
        }

    }

    @Override
    protected void onDestroy()
    {

        super.onDestroy();
        if(kShowCreditCall!=null)
            kShowCreditCall.cancel();
        if(kShowDetailCall!=null)
            kShowDetailCall.cancel();
        if(kShowTrailerCall!=null)
            kShowTrailerCall.cancel();
        if(kSimilarShowCall!=null)
            kSimilarShowCall.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isBroadCastRecieverRegistered)
        {
            isBroadCastRecieverRegistered=false;
            unregisterReceiver(kConnectivityBroadCastReciever);
        }
    }

    private void loadActivity()
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        kPosterProgressBar.setVisibility(View.VISIBLE);
        kBackDropProgressBar.setVisibility(View.VISIBLE);

        kShowDetailCall=apiService.getShowsDetails(kShowid,getResources().getString(R.string.MOVIE_DB_API_KEY));
        kShowDetailCall.enqueue(new Callback<Shows>() {
            @Override
            public void onResponse(Call<Shows> call, final Response<Shows> response) {

                if(!response.isSuccessful())
                {
                    kShowDetailCall=call.clone();
                    kShowDetailCall.enqueue(this);
                }
                if(response.body()==null)
                    return;
                kAppBarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                        if(appBarLayout.getTotalScrollRange() +verticalOffset==0)
                        {
                            if(response.body().getName()!=null)
                                kCollapsingToolbar.setTitle(response.body().getName());
                            else
                                kCollapsingToolbar.setTitle("");
                            kToolbar.setVisibility(View.VISIBLE);
                        }
                        else
                            kCollapsingToolbar.setTitle("");
                            kToolbar.setVisibility(View.INVISIBLE);
                    }
                });

                Glide.with(getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500 + response.body().getPosterPath())
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

                if(response.body().getName()!=null)
                    kTitletext.setText(response.body().getName());
                else
                    kTitletext.setText("");

                setGenres(response.body().getGenres());
                setYear(response.body().getFirstAirDate());

                kBackShowImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
                kFavShowImageButton.setVisibility(View.VISIBLE);
                kShareShowImageButton.setVisibility(View.VISIBLE);



                setImageButton(response.body().getId(),response.body().getPosterPath(),response.body().getName(),response.body().getHomepage());
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

                setDetails(response.body().getFirstAirDate(),response.body().getEpisodeRunTime(),response.body().getStatus(),response.body().getOriginCountries(),response.body().getNetworks());


                setTrailers();

                line.setVisibility(View.VISIBLE);
                setCasts();

                setSimilarShows();




            }


            @Override
            public void onFailure(Call<Shows> call, Throwable t) {

            }
        });

    }

    private void setSimilarShows()
    {
        ApiInterface apiService=ApiClient.getClient().create(ApiInterface.class);
        kSimilarShowCall=apiService.getSimilarShows(kShowid,getResources().getString(R.string.MOVIE_DB_API_KEY),1);
        kSimilarShowCall.enqueue(new Callback<SimilarShowResponse>() {
            @Override
            public void onResponse(Call<SimilarShowResponse> call, Response<SimilarShowResponse> response) {


                if(!response.isSuccessful())
                {
                    kSimilarShowCall=call.clone();
                    kSimilarShowCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getResults()==null)
                    return;

                for(ShowDetail detail : response.body().getResults())
                {
                    if(detail !=null && detail.getName() !=null && detail.getPosterPath() !=null)
                        ksimilarShow.add(detail);
                }
                if(!ksimilarShow.isEmpty())
                    simimlarShowsText.setVisibility(View.VISIBLE);
                kSimilarShowAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SimilarShowResponse> call, Throwable t) {

            }
        });
    }

    private void setCasts()
    {

        ApiInterface apiService =ApiClient.getClient().create(ApiInterface.class);
        kShowCreditCall=apiService.getShowCredits(kShowid,getResources().getString(R.string.MOVIE_DB_API_KEY));
        kShowCreditCall.enqueue(new Callback<ShowCreditResponse>() {
            @Override
            public void onResponse(Call<ShowCreditResponse> call, Response<ShowCreditResponse> response)
            {
                if(!response.isSuccessful())
                {
                    kShowCreditCall=call.clone();
                    kShowCreditCall.enqueue(this);
                    return;

                }
                if(response.body()==null)
                    return;
                if(response.body().getCasts()==null)
                    return;
                for(CastDetail detail:response.body().getCasts())
                {
                    if(detail !=null && detail.getName()!=null)
                        kCasts.add(detail);

                }
                if(!kCasts.isEmpty())
                    kCastDetail.setVisibility(View.VISIBLE);
                kCastAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<ShowCreditResponse> call, Throwable t) {

            }
        });
    }

    private void setTrailers()
    {
        ApiInterface apiService =ApiClient.getClient().create(ApiInterface.class);
        kShowTrailerCall=apiService.getShowsVideos(kShowid,getResources().getString(R.string.MOVIE_DB_API_KEY));
        kShowTrailerCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {

                if(!response.isSuccessful())
                {
                    kShowTrailerCall=call.clone();
                    kShowTrailerCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getVideos()==null)
                    return;

                for(Video video :response.body().getVideos())
                {
                    if(video!=null && video.getSite()!=null && video.getSite().equals("YouTube") && video.getType()!=null && video.getType().equals("Trailer"))
                        kTrailers.add(video);

                    if(!kTrailers.isEmpty())
                        kShowTrailer.setVisibility(View.VISIBLE);
                    kTrailerAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });

    }

    private void setDetails(String releaseDate, List<Integer> runtime, String status, List<String> originCountries, List<Network> networks) {
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
        if(runtime!=null && !runtime.isEmpty() && runtime.get(0)!=0)
        {
            if(runtime.get(0)<60)
                details+=runtime+"min(s)" +"\n";
            else
                details+=runtime.get(0)/60+"hr"+runtime.get(0)%60+"min(s)";
        }
        else
            details+="-\n";

        if(status !=null && !status.isEmpty())
        {
            details+=status+"\n";
        }
        else
            details="-\n";

        String OriginCountry="";
        if(originCountries!=null && !originCountries.isEmpty())
        {

            for(String country : originCountries)
            {
                if(country!=null && !country.trim().isEmpty())
                    OriginCountry+=country +", ";
            }
            if(!OriginCountry.isEmpty())
                details+=OriginCountry.substring(0,OriginCountry.length()-2);
            else
                details+="-\n";
        }
        else
            details+="-\n";
        String StringNetwork="";

        if(networks!=null && !networks.isEmpty())
        {

            for(Network net : networks)
            {
                if(net==null || net.getName()==null || net.getName().isEmpty())
                    continue;
                StringNetwork+=net.getName()+", ";
            }
            if(!StringNetwork.isEmpty())
                details+=StringNetwork.substring(0,StringNetwork.length()-2);
            else
                details+="-\n";
        }
        else
            details+="-\n";

        kDetailstext.setText(details);


    }

    private void setImageButton(final Integer id, final String posterPath, final String name, final String homepage)
    {
        if(id==null)
            return;

        if(Favourite.isTVShowFav(getShowDetail.this,id))
        {
            kFavShowImageButton.setTag(1);
            kFavShowImageButton.setImageResource(R.drawable.ic_facourite);
        }
        else
        {
            kFavShowImageButton.setTag(0);
            kFavShowImageButton.setImageResource(R.drawable.ic_favourite);
        }

        kFavShowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(kFavShowImageButton.getTag().equals(1))
                {
                    Favourite.removeTVShowsfromfavourite(getShowDetail.this,id);
                    kFavShowImageButton.setTag(0);
                    kFavShowImageButton.setImageResource(R.drawable.ic_favourite);
                }
                else
                {
                    Favourite.addTVShowsToFav(getShowDetail.this,id,posterPath,name);
                    kFavShowImageButton.setTag(1);
                    kFavShowImageButton.setImageResource(R.drawable.ic_facourite);
                }

            }
        });
        kShareShowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent shareIntent= new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String message="";
                if(name!=null)
                    message+=name+"\n";
                if(homepage!=null)
                    message+=homepage;
                shareIntent.putExtra(Intent.EXTRA_TEXT,message);
                startActivity(shareIntent);

            }
        });
    }

    private void setYear(String firstAirDate)
    {
        if (firstAirDate != null && !firstAirDate.trim().isEmpty()) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date AirDate = sdf1.parse(firstAirDate);
                kYeartext.setText(sdf2.format(AirDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            kYeartext.setText("");
        }
    }

    private void setGenres(List<Genre> genres)
    {
        String genre="";
        if(genres!=null)
        {
            for(int i=0;i<genres.size();i++)
            {
                if(genres.get(i)==null)
                    continue;
                if(i==genres.size()-1)
                    genre=genre.concat(genres.get(i).getGenreName());
                else
                    genre=genre.concat(genres.get(i).getGenreName()+", ");
            }
        }
        kGenretext.setText(genre);
    }
}