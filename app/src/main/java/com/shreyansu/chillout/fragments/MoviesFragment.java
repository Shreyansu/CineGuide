package com.shreyansu.chillout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shreyansu.chillout.R;


public class MoviesFragment extends Fragment
{
    private ProgressBar kprogressBar;

    private FrameLayout kNowshowinglayout;
    private FrameLayout kupcominglayout;
    private FrameLayout kpopularlayout;
    private FrameLayout ktopratedlayout;


    private TextView knowShowingAllText;
    private TextView kpopularAllText;
    private TextView kupcomingAllText;
    private TextView ktopratedALLText;


    private boolean knowshowingsectionload;
    private boolean kpopularmovieload;
    private boolean kupcomingmovieload;
    private boolean ktopratedmovieload;

    private RecyclerView knowshowingRecyView;
    private RecyclerView kpopularRecycView;
    private RecyclerView kupcomingRecycView;
    private RecyclerView ktopRatedRecycView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movies, container, false);


        kprogressBar=(ProgressBar)view.findViewById(R.id.progress_bar);
        kprogressBar.setVisibility(view.GONE);

        knowshowingsectionload=false;
        kpopularmovieload=false;
        kupcomingmovieload=false;
        ktopratedmovieload=false;

        kNowshowinglayout=(FrameLayout)view.findViewById(R.id.now_showing);
        kpopularlayout=(FrameLayout)view.findViewById(R.id.popular);
        kupcominglayout=(FrameLayout)view.findViewById(R.id.upcoming);
        ktopratedlayout=(FrameLayout)view.findViewById(R.id.top_rated);

        knowShowingAllText=(TextView)view.findViewById(R.id.all_showing_now);
        kupcomingAllText=(TextView)view.findViewById(R.id.text_view_all_upcoming);
        kpopularAllText=(TextView)view.findViewById(R.id.text_view_all_popular);
        ktopratedALLText=(TextView)view.findViewById(R.id.all_top_rated);


        knowshowingRecyView=(RecyclerView)view.findViewById(R.id.recycler_view_now_showing);
        (new LinearSnapHelper()).attachToRecyclerView(knowshowingRecyView);

        kpopularRecycView=(RecyclerView)view.findViewById(R.id.recycler_view_popular);
        kupcomingRecycView=(RecyclerView)view.findViewById(R.id.recycler_view_upcoming);
        (new LinearSnapHelper()).attachToRecyclerView(kupcomingRecycView);

        ktopRatedRecycView=(RecyclerView)view.findViewById(R.id.recycler_view_top_rated);
        //TODO 9-12



        return view;
    }


}