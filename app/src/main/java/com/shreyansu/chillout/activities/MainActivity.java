package com.shreyansu.chillout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.fragments.FavouritesFragment;
import com.shreyansu.chillout.fragments.MoviesFragment;
import com.shreyansu.chillout.fragments.tvShowFragment;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.connectivityStatus;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mdrawer;
    private NavigationView navigationView;

    private boolean doublePressExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if(sharedPreferences.getBoolean(Constants.FIRST_TIME_LAUNNCHING_ACTIVITY,true))
        {
            startActivity(new Intent(MainActivity.this, IntroductionActivity.class));
            SharedPreferences.Editor sharedPreferenceEditor=sharedPreferences.edit();
            sharedPreferenceEditor.putBoolean(Constants.FIRST_TIME_LAUNNCHING_ACTIVITY,false);
            sharedPreferenceEditor.commit();
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mdrawer=(DrawerLayout)findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mdrawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mdrawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView =(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                int id=menuItem.getItemId();
                mdrawer.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.nav_movies:
                        setTitle("Movies");
                        setFragment(new MoviesFragment());
                        return true;
                    case R.id.nav_tv_show:
                        setTitle("TV Shows");
                        setFragment(new tvShowFragment());
                        return true;
                    case R.id.nav_favourites:
                        setTitle("Favourites");
                        setFragment(new FavouritesFragment());
                        return true;
                    case R.id.nav_about:
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        return false;

                }
                return true;
            }
        });
        //TODO in upper declaration
        navigationView.setCheckedItem(R.id.nav_movies);
        setTitle("Movies");
        setFragment(new MoviesFragment());











    }

    private void setFragment(Fragment fragment)
    {
        FragmentManager fManager=getSupportFragmentManager();
        FragmentTransaction fTransaction=fManager.beginTransaction();
        fTransaction.replace(R.id.main_activity_fragment,fragment);
        fTransaction.commit();

    }

    @Override
    public void onBackPressed()
    {
        mdrawer=(DrawerLayout)findViewById(R.id.drawerlayout);
        if(mdrawer.isDrawerOpen(GravityCompat.START))
            mdrawer.closeDrawer(GravityCompat.START);
        else
        {
            if(doublePressExit)
            {
                super.onBackPressed();
                return;
            }
            doublePressExit=true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doublePressExit=false;
                }

            },2000);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Movies, TV Shows,people by Name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!connectivityStatus.isConnected(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra(Constants.QUERY, query);
                startActivity(intent);
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;
    }

}