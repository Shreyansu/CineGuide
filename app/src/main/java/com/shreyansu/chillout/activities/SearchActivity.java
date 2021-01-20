package com.shreyansu.chillout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shreyansu.chillout.R;
import com.shreyansu.chillout.adapters.SearchAdapter;
import com.shreyansu.chillout.network.search.SearchLoader;
import com.shreyansu.chillout.network.search.SearchResponse;
import com.shreyansu.chillout.network.search.SearchResult;
import com.shreyansu.chillout.util.Constants;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity
{
    private String query;
    private RecyclerView kSearchRecView;
    private List<SearchResult> kSearchResults;
    private SearchAdapter kSearchAdapter;

    private TextView kEmptyTextView;

    private boolean lastPage=false;
    private int presentPage=1;
    private boolean loading=true;
    private int previoustotal=0;
    private int visibleThreshold=5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        query=getIntent().getStringExtra(Constants.QUERY);
        if(query==null || query.trim().isEmpty())
            finish();

        setTitle(query);

        kSearchRecView=(RecyclerView)findViewById(R.id.recycler_view_result);
        kSearchResults=new ArrayList<>();
        kSearchAdapter=new SearchAdapter(SearchActivity.this,kSearchResults);
        kSearchRecView.setAdapter(kSearchAdapter);
        final LinearLayoutManager linearLayoutManager= new LinearLayoutManager(SearchActivity.this,RecyclerView.VERTICAL,false);
        kSearchRecView.setLayoutManager(linearLayoutManager);



        kSearchRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {

                super.onScrolled(recyclerView, dx, dy);

                int visibleCount=linearLayoutManager.getChildCount();
                int totalCount=linearLayoutManager.getItemCount();
                int firstVisibleItem=linearLayoutManager.findFirstVisibleItemPosition();

                if(loading)
                {
                    if(totalCount>previoustotal)
                    {
                        loading=false;
                        previoustotal=totalCount;
                    }
                }
                if(!loading && (totalCount-visibleCount)<=firstVisibleItem+visibleThreshold)
                {
//                    loadSearchResults();
                    loading=true;
                }
            }
        });

//        loadSearchResults();
    }


}