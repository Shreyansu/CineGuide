package com.shreyansu.chillout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.network.MovieDetail;
import com.shreyansu.chillout.util.Constants;

import java.util.List;

public class MovieDetailSmallAdapter extends RecyclerView.Adapter<MovieDetailSmallAdapter.MovieViewHolder>
{
    private Context context;
    private List<MovieDetail> kMovies;

    public MovieDetailSmallAdapter(Context context, List<MovieDetail> kMovies)
    {
        this.context = context;
        this.kMovies = kMovies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.small_tem_adapter,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position)
    {
        Glide.with(context.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500 + kMovies.get(position).getPosterPath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((holder.moviePosterImageView));


        if(kMovies.get(position).getTitle()!=null)
            holder.movieTitleTextView.setText(kMovies.get(pos).getTitle());
        else
            holder.movieTitleTextView.setText("");

        if()






    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MovieViewHolder  extends RecyclerView.ViewHolder
    {

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
