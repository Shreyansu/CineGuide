package com.shreyansu.chillout.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.activities.getMovieDetails;
import com.shreyansu.chillout.network.movies.MovieDetail;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.Favourite;

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
                .into((holder.moviePosterImage));


        if(kMovies.get(position).getTitle()!=null)
            holder.movieTitleText.setText(kMovies.get(position).getTitle());
        else
            holder.movieTitleText.setText("");

        if(Favourite.isFavMovie(context,kMovies.get(position).getId()))
        {
            holder.movieFavButton.setImageResource(R.drawable.heart256);
            holder.movieFavButton.setEnabled(false);
        }
        else
        {
            holder.movieFavButton.setImageResource(R.drawable.ic_favourite);
            holder.movieFavButton.setEnabled(false);
        }






    }

    @Override
    public int getItemCount()
    {
        return kMovies.size();
    }

    public class MovieViewHolder  extends RecyclerView.ViewHolder
    {
        public CardView cardView;
        public ImageView moviePosterImage;
        public TextView movieTitleText;
        public ImageButton movieFavButton;


        public MovieViewHolder(@NonNull View itemView)
        {

            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.card_view_show_card);
            moviePosterImage=(ImageView) itemView.findViewById(R.id.image_view_show_card);
            movieTitleText=(TextView) itemView.findViewById(R.id.text_view_title_show_card);
            movieFavButton=(ImageButton) itemView.findViewById(R.id.image_button_fav_show_card);

            moviePosterImage.getLayoutParams().width=(int) (context.getResources().getDisplayMetrics().widthPixels*0.31);
            moviePosterImage.getLayoutParams().height=(int) (context.getResources().getDisplayMetrics().heightPixels*0.31/0.65);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(context, getMovieDetails.class);
                    intent.putExtra(Constants.MOVIE_ID,kMovies.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });

            movieFavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Favourite.addMovieToFav(context,kMovies.get(getAdapterPosition()).getId(),kMovies.get(getAdapterPosition()).getPosterPath(),kMovies.get(getAdapterPosition()).getTitle());
                    movieFavButton.setImageResource(R.drawable.heart256);
                    movieFavButton.setEnabled(false);


                }
            });

        }
    }
}
