package com.shreyansu.chillout.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.shreyansu.chillout.R;

public class IntroductionActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Welcome to PopCorn", "Find and Discover your favourite Movies, TV Shows, Actors and more.", R.drawable.ic_launcher_large, Color.DKGRAY));
        addSlide(AppIntroFragment.newInstance("Favorites", "Mark your Movies and TV Shows favourite.\nSo you never miss them again.", R.drawable.heart256, Color.DKGRAY));
        addSlide(AppIntroFragment.newInstance("Explore", "Search your loved Movies and TV Shows from vast database.", R.drawable.search256, Color.DKGRAY));
        addSlide(AppIntroFragment.newInstance("Share", "Share your Movies and TV Shows with friends.", R.drawable.share256, Color.DKGRAY));


        showStatusBar(false);


    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
