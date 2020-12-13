package com.shreyansu.chillout.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.shreyansu.chillout.util.MovieGenre;

import java.util.List;

public class MovieDetailLargeAdapter extends RecyclerView.Adapter<MovieDetailLargeAdapter.MovieViewHolder>{


    private Context context;
    private List<MovieDetail> kMovies;
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.large_item_adapter,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position)
    {
        Glide.with(context.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500 +kMovies.get(position).getBackdropPath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePosterImage);

         if(kMovies.get(position).getTitle() !=null)
             holder.movieTitle.setText(kMovies.get(position).getTitle());
         else
             holder.movieTitle.setText("");

         if((kMovies.get(position).getVoteAverage() !=null && kMovies.get(position).getVoteAverage()>0 ))
         {
             holder.movieRating.setVisibility(View.VISIBLE);
             holder.movieRating.setText(String.format("%.1f",kMovies.get(position).getVoteAverage())+Constants.RATING_SYMB);
         }
         else
             holder.movieRating.setVisibility(View.GONE);


         setGenre(holder,kMovies.get(position));

         if(Favourite.isFavMovie(context,kMovies.get(position).getId()))
         {
             holder.movieFavimage.setImageResource(R.drawable.heart256);
             holder.movieFavimage.setEnabled(false);
         }
         else
             holder.movieFavimage.setImageResource(R.drawable.ic_favourite);
            holder.movieFavimage.setEnabled(true);

    }

    private void setGenre(MovieViewHolder holder, MovieDetail movieDetail)
    {
        String genre="";
        for(int i=0;i<movieDetail.getGenreIds().size();i++)
        {
            if(movieDetail.getGenreIds().get(i)==null)
                continue;
            if(MovieGenre.getGenreName(movieDetail.getGenreIds().get(i))==null)
                continue;
            genre+=MovieGenre.getGenreName(movieDetail.getGenreIds().get(i));

        }
        if(!genre.isEmpty())
            holder.genreText.setText(genre.substring(0,genre.length()-2));

        else
            holder.genreText.setText("");

    }

    @Override
    public int getItemCount() {
        return kMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
    {
        public CardView movieCard;
        public RelativeLayout imageLayout;
        public ImageView moviePosterImage;
        public TextView movieTitle;
        public TextView movieRating;
        public TextView genreText;
        public ImageButton movieFavimage;

        public MovieViewHolder(@NonNull View itemView)
        {
            super(itemView);
            movieCard=(CardView)itemView.findViewById(R.id.movie_card);
            imageLayout=(RelativeLayout)itemView.findViewById(R.id.image_layout_show_card);
            moviePosterImage=(ImageView)itemView.findViewById(R.id.image_view_show_card);
            movieTitle=(TextView) itemView.findViewById(R.id.text_view_title_card);
            movieRating=(TextView)itemView.findViewById(R.id.rating_show_card_text);
            genreText=(TextView)itemView.findViewById(R.id.text_view_genre);
            movieFavimage=(ImageButton)itemView.findViewById(R.id.image_button_fav_show_card);

             imageLayout.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels*0.09);
              imageLayout.getLayoutParams().height =(int) (context.getResources().getDisplayMetrics().heightPixels*0.09/1.77);

              movieCard.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view)
                  {
                      Intent intent=new Intent(context, getMovieDetails.class);
                      intent.putExtra(Constants.MOVIE_ID,kMovies.get(getAdapterPosition()).getId());
                      context.startActivity(intent);

                  }
              });

              movieFavimage.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                      Favourite.addMovieToFav(context,kMovies.get(getAdapterPosition()).getId(),kMovies.get(getAdapterPosition()).getPosterPath(),kMovies.get(getAdapterPosition()).getTitle());
                      movieFavimage.setImageResource(R.drawable.heart256);
                      movieFavimage.setEnabled(false);

                  }
              });




        }
    }
}
