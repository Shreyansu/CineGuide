package com.shreyansu.chillout.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.shreyansu.chillout.BroadCastConnectivtiy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.adapters.MovieCastsOfPersonAdapter;
import com.shreyansu.chillout.adapters.ShowCastOfPersonAdapter;
import com.shreyansu.chillout.network.Actor;
import com.shreyansu.chillout.network.ApiClient;
import com.shreyansu.chillout.network.ApiInterface;
import com.shreyansu.chillout.network.movies.MovieCastPersonResponse;
import com.shreyansu.chillout.network.movies.MoviePersonCast;
import com.shreyansu.chillout.network.tvshows.CastDetail;
import com.shreyansu.chillout.network.tvshows.CastPerson;
import com.shreyansu.chillout.network.tvshows.CastPersonResponse;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.connectivityStatus;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CastDetailActivity extends AppCompatActivity
{
    private AppBarLayout kAppbarlayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar ktoolbar;


    private CardView kCastCardView;
    private ImageView kcatImageView;
    private AVLoadingIndicatorView kcastImageLoading;

    private ImageButton kback;
    private TextView  kCastViewName;
    private TextView kCastAgeText;
    private TextView kCastBirthplace;


    private Call<Actor> kCastpersonDetail;

    private TextView kDesciptionHeader;
    private TextView kDescriptiontext;
    private TextView kDescriptionReadMore;


    private TextView kMovieCastTextView;
    private RecyclerView kMovieCastRecycView;
    private List<MoviePersonCast> kMovieCastPerson;
    private Call<MovieCastPersonResponse> kMovieCastPersonCall;
    private MovieCastsOfPersonAdapter kMovieCastAdapter;

    private TextView kShowCastTextView;
    private RecyclerView kShowRecycView;
    private List<CastPerson> kShowCastPerson;
    private Call<CastPersonResponse> kShowCastPersonCall;
    private ShowCastOfPersonAdapter kShowCastAdapter;



    private Snackbar snackbar;
    private BroadCastConnectivtiy kConnectivtyReciever;
    private boolean isRecieverRegister;
    private boolean isActivityLoaded;
    private  int kCastPeronId;
    private int kCastImageSize;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_detail);
        ktoolbar=(Toolbar)findViewById(R.id.toolbar_shows);
        setSupportActionBar(ktoolbar);
        setTitle("");

        kCastPeronId=getIntent().getIntExtra(Constants.TV_SHOW_ID,-1);


        if(kCastPeronId==-1)
            return ;

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout_shows);
        kAppbarlayout=(AppBarLayout)findViewById(R.id.app_bar_layout_shows);
        kCastImageSize=(int) (getResources().getDisplayMetrics().widthPixels*0.35);

        kCastCardView=(CardView) findViewById(R.id.cast_card_view);
        kcatImageView=(ImageView) findViewById(R.id.cast_image);
        kCastCardView.getLayoutParams().height=kCastImageSize;
        kCastCardView.getLayoutParams().width=kCastImageSize;

        kCastCardView.setRadius(kCastImageSize/2);
        kcastImageLoading=(AVLoadingIndicatorView)findViewById(R.id.cast_image_loading);
        kcastImageLoading.setVisibility(View.GONE);
        kCastViewName=(TextView)findViewById(R.id.cast_name_text);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) kCastViewName.getLayoutParams();
        params.setMargins(params.leftMargin, kCastImageSize / 2, params.rightMargin, params.bottomMargin);

        kCastAgeText=(TextView)findViewById(R.id.age_cast);

        kCastBirthplace=(TextView)findViewById(R.id.birthplace_text_detail);
        kback=(ImageButton) findViewById(R.id.back_cast);
        kback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        kDesciptionHeader=(TextView)findViewById(R.id.cast_bio_text);
        kDescriptiontext=(TextView)findViewById(R.id.cast_bio_details);
        kDescriptionReadMore=(TextView)findViewById(R.id.read_more_shows);

        kMovieCastTextView=(TextView)findViewById(R.id.movie_cast_text);
        kMovieCastRecycView=(RecyclerView)findViewById(R.id.movie_cast_recyc_view);
        kMovieCastPerson=new ArrayList<>();
        kMovieCastAdapter=new MovieCastsOfPersonAdapter(this,kMovieCastPerson);
        kMovieCastRecycView.setAdapter(kMovieCastAdapter);
        kMovieCastRecycView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        kShowCastTextView=(TextView)findViewById(R.id.show_cast_text);
        kShowRecycView=(RecyclerView)findViewById(R.id.show_cast_recyc_view);
        kShowCastPerson=new ArrayList<>();
        kShowCastAdapter=new ShowCastOfPersonAdapter(this,kShowCastPerson);
        kShowRecycView.setAdapter(kShowCastAdapter);
        kShowRecycView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


        if(connectivityStatus.isConnected(CastDetailActivity.this))
        {
            isActivityLoaded=true;
            loadActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isActivityLoaded && !connectivityStatus.isConnected(CastDetailActivity.this))
        {
            snackbar=snackbar.make(kCastViewName,"No Internet Connection", BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.show();
            kConnectivtyReciever=new  BroadCastConnectivtiy(new BroadCastConnectivtiy.connectivityRecieverListener() {
                @Override
                public void OnNetworkConnectionConnected()
                {
                    snackbar.dismiss();
                    isActivityLoaded=true;
                    loadActivity();
                    isRecieverRegister=false;
                    unregisterReceiver(kConnectivtyReciever);

                }
            });
            IntentFilter intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            isRecieverRegister=true;
            registerReceiver(kConnectivtyReciever,intentFilter);

        }
        else if(!isActivityLoaded && connectivityStatus.isConnected(CastDetailActivity.this))
        {
            isActivityLoaded=true;
            loadActivity();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isRecieverRegister)
        {
            isRecieverRegister=false;
            unregisterReceiver(kConnectivtyReciever);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(kCastpersonDetail!=null)
            kCastpersonDetail.cancel();
        if(kMovieCastPersonCall!=null)
            kMovieCastPersonCall.cancel();
        if(kShowCastPersonCall!=null)
            kShowCastPersonCall.cancel();
    }

    private void loadActivity()
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        kcastImageLoading.setVisibility(View.VISIBLE);
        kCastpersonDetail=apiService.getActorDetail(kCastPeronId,getResources().getString(R.string.MOVIE_DB_API_KEY));

        kCastpersonDetail.enqueue(new Callback<Actor>() {
            @Override
            public void onResponse(Call<Actor> call, final Response<Actor> response)
            {
                if(!response.isSuccessful())
                {
                    kCastpersonDetail=call.clone();
                    kCastpersonDetail.clone();
                    return;
                }
                 if(response.body()==null)
                     return;

                 kAppbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                     @Override
                     public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                         if(appBarLayout.getTotalScrollRange()+verticalOffset==0)
                         {
                             if(response.body().getName()!=null)
                                 collapsingToolbarLayout.setTitle(response.body().getName());
                             else
                                 collapsingToolbarLayout.setTitle("");
                             ktoolbar.setVisibility(View.VISIBLE);
                         }
                         else
                         {
                             collapsingToolbarLayout.setTitle("");
                             ktoolbar.setVisibility(View.INVISIBLE);
                         }

                     }

                 });

                Glide.with(getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500+ response.body().getProfilePath())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                kcastImageLoading.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                kcastImageLoading.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(kcatImageView);

                if(response.body().getName()!=null)
                    kCastViewName.setText(response.body().getName());
                else
                    kCastViewName.setText("");
                setAge(response.body().getDateOfBirth());

                if(response.body().getPlaceOfBirth()!=null && !response.body().getPlaceOfBirth().trim().isEmpty())
                    kCastBirthplace.setText(response.body().getPlaceOfBirth());

                if(response.body().getBiography()!=null && !response.body().getBiography().trim().isEmpty())
                {
                    kDesciptionHeader.setVisibility(View.VISIBLE);
                    kDescriptiontext.setText(response.body().getBiography());
                    kDescriptionReadMore.setVisibility(View.VISIBLE);
                    kDescriptionReadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            kDescriptiontext.setMaxLines(7);
                            kDescriptionReadMore.setVisibility(View.GONE);
                        }
                    });
                }

                setCastMovies(response.body().getId());
                setCastShows(response.body().getId());




            }

            @Override
            public void onFailure(Call<Actor> call, Throwable t) {

            }
        });
    }

    private void setCastMovies(Integer id)
    {
        ApiInterface apiService=ApiClient.getClient().create(ApiInterface.class);
        kMovieCastPersonCall=apiService.getMovieCastPerson(id,getResources().getString(R.string.MOVIE_DB_API_KEY));
        kMovieCastPersonCall.enqueue(new Callback<MovieCastPersonResponse>() {
            @Override
            public void onResponse(Call<MovieCastPersonResponse> call, Response<MovieCastPersonResponse> response) {
                if(!response.isSuccessful())
                {
                    kMovieCastPersonCall=call.clone();
                    kMovieCastPersonCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;
                if(response.body().getCasts()==null)
                    return;

                for(MoviePersonCast moviePersonCast : response.body().getCasts())
                {
                    if(moviePersonCast==null)
                       return;
                    if(moviePersonCast.getTitle()!=null && moviePersonCast.getPosterPath()!=null)
                    {
                        kMovieCastTextView.setVisibility(View.VISIBLE);
                        kMovieCastPerson.add(moviePersonCast);
                    }
                }
                    kMovieCastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieCastPersonResponse> call, Throwable t) {

            }
        });
    }

    private void setCastShows(Integer id)
    {
        ApiInterface apiService=ApiClient.getClient().create(ApiInterface.class);
        kShowCastPersonCall=apiService.getShowsCastpersonResponse(id,getResources().getString(R.string.MOVIE_DB_API_KEY));
        kShowCastPersonCall.enqueue(new Callback<CastPersonResponse>() {
            @Override
            public void onResponse(Call<CastPersonResponse> call, Response<CastPersonResponse> response) {

                if(!response.isSuccessful())
                {
                    kShowCastPersonCall=call.clone();
                    kShowCastPersonCall.enqueue(this);
                    return;
                }
                if(response.body()==null)
                    return;

                if(response.body().getCasts()==null)
                    return;
                for(CastPerson castPerson :response.body().getCasts())
                {
                    if(castPerson==null)
                        return;
                    if(castPerson.getName()!=null && castPerson.getPosterPath()!=null)
                    {
                        kShowCastTextView.setVisibility(View.VISIBLE);
                        kShowCastPerson.add(castPerson);
                    }

                }
                kShowCastAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<CastPersonResponse> call, Throwable t) {

            }
        });
    }

    private void setAge(String dateOfBirth)
    {
        if(dateOfBirth !=null && !dateOfBirth.trim().isEmpty())
        {
            SimpleDateFormat date1=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat date2=new SimpleDateFormat("yyyy");
            try {
                Date birthday=date1.parse(dateOfBirth);
                kCastAgeText.setText(Calendar.getInstance().get(Calendar.YEAR)-Integer.parseInt(date2.format(birthday))+" ");

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}