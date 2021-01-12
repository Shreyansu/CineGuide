package com.shreyansu.chillout.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.activities.getMovieDetails;
import com.shreyansu.chillout.network.movies.MoviePersonCast;
import com.shreyansu.chillout.util.Constants;

import java.util.List;

public class MovieCastsOfPersonAdapter extends RecyclerView.Adapter<MovieCastsOfPersonAdapter.ViewHolder> {

    private Context kContext;
    private List<MoviePersonCast> kMovieCast;

    public MovieCastsOfPersonAdapter(Context kContext, List<MoviePersonCast> kMovieCast)
    {

        this.kContext = kContext;
        this.kMovieCast = kMovieCast;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(kContext).inflate(R.layout.item_show_of_cast,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Glide.with(kContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500 + kMovieCast.get(position).getPosterPath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.kMoviePoster);


        if(kMovieCast.get(position).getTitle() !=null)
            holder.kMovieTitle.setText(kMovieCast.get(position).getTitle());
        else
            holder.kMovieTitle.setText("");

        if(kMovieCast.get(position).getCharacter() !=null && !kMovieCast.get(position).getCharacter().isEmpty())
            holder.KCastCharacterName.setText("as" + kMovieCast.get(position).getCharacter());
        else
            holder.KCastCharacterName.setText("");


    }

    @Override
    public int getItemCount() {
        return kMovieCast.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private CardView kmovieCardView;
        private ImageView kMoviePoster;
        private TextView kMovieTitle;
        private TextView KCastCharacterName;

        public ViewHolder(@NonNull View itemView)
        {

            super(itemView);
            kmovieCardView=(CardView) itemView.findViewById(R.id.movie_cast_card);
            kMoviePoster =(ImageView) itemView.findViewById(R.id.image_view_show_cast);

            kMovieTitle=(TextView) itemView.findViewById(R.id.cast_movie_title);
            KCastCharacterName=(TextView) itemView.findViewById(R.id.cast_character_name);

            kMoviePoster.getLayoutParams().width=(int) (kContext.getResources().getDisplayMetrics().widthPixels*0.30);
            kMoviePoster.getLayoutParams().width=(int) ((kContext.getResources().getDisplayMetrics().widthPixels*030)/0.65);


            kmovieCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(kContext, getMovieDetails.class);
                    intent.putExtra(Constants.MOVIE_ID,kMovieCast.get(getAdapterPosition()).getId());
                    kContext.startActivity(intent);
                }
            });


        }
    }
}
