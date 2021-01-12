package com.shreyansu.chillout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.shreyansu.chillout.BroadCastConnectivtiy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.adapters.MovieCastsOfPersonAdapter;
import com.shreyansu.chillout.adapters.ShowCastOfPersonAdapter;
import com.shreyansu.chillout.network.movies.MovieCastPersonResponse;
import com.shreyansu.chillout.network.movies.MoviePersonCast;
import com.shreyansu.chillout.network.tvshows.CastDetail;
import com.shreyansu.chillout.network.tvshows.CastPerson;
import com.shreyansu.chillout.network.tvshows.CastPersonResponse;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.connectivityStatus;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

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


    private Call<CastPerson> kCastpersonDetail;

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

    }
}