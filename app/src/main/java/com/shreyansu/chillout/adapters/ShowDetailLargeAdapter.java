package com.shreyansu.chillout.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
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
import com.shreyansu.chillout.activities.getShowDetail;
import com.shreyansu.chillout.network.tvshows.ShowDetail;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.Favourite;
import com.shreyansu.chillout.util.ShowGenre;

import java.util.List;

public class ShowDetailLargeAdapter extends RecyclerView.Adapter<ShowDetailLargeAdapter.ShowViewHolder>
{
    private Context context;
    private List<ShowDetail> kShows;

    public ShowDetailLargeAdapter(Context context, List<ShowDetail> kShows) {
        this.context = context;
        this.kShows = kShows;
    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return new ShowViewHolder(LayoutInflater.from(context).inflate(R.layout.large_item_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowViewHolder holder, int position)
    {
        Glide.with(context.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500 +kShows.get(position).getBackdropPath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ShowPosterImage);

        if(kShows.get(position).getName() !=null)
            holder.ShowTitle.setText(String.format(kShows.get(position).getName()));
        else
            holder.ShowTitle.setText("");

        if(kShows.get(position).getVoteAverage()!=null && kShows.get(position).getVoteAverage()>0)
        {
            holder.ShowRating.setText(String.format("%.1f",kShows.get(position).getVoteAverage())+Constants.RATING_SYMB);
        }
        else
            holder.ShowRating.setVisibility(View.GONE);

        setGenres(holder, kShows.get(position));

        if(Favourite.isTVShowFav(context,kShows.get(position).getId()))
        {
            holder.ShowFavimage.setImageResource(R.drawable.ic_facourite);
            holder.ShowFavimage.setEnabled(false);

        }
        else
        {
            holder.ShowFavimage.setImageResource(R.drawable.ic_favourite);
            holder.ShowFavimage.setEnabled(true);
        }





    }

    private void setGenres(ShowViewHolder holder, ShowDetail showDetail)
    {
        String genrelist="";
        for(int i=0;i< showDetail.getGenreIds().size();i++)
        {
            if(showDetail.getGenreIds().get(i)==null)
                continue;
            if(ShowGenre.getGenreName(showDetail.getGenreIds().get(i))==null)
                continue;
            genrelist+=ShowGenre.getGenreName(showDetail.getGenreIds().get(i))+",";
        }
        if(!genrelist.isEmpty())
            holder.ShowgenreText.setText(genrelist.substring(0,genrelist.length()-2));
        else
            holder.ShowgenreText.setText("");
    }

    @Override
    public int getItemCount() {
        return kShows.size();
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder
    {
        public CardView ShowCard;
        public RelativeLayout imageLayout;
        public ImageView ShowPosterImage;
        public TextView ShowTitle;
        public TextView ShowRating;
        public TextView ShowgenreText;
        public ImageButton ShowFavimage;
        public ShowViewHolder(@NonNull View itemView)
        {

            super(itemView);
            ShowCard=(CardView)itemView.findViewById(R.id.movie_card);
            imageLayout=(RelativeLayout)itemView.findViewById(R.id.image_layout_show_card);
            ShowPosterImage=(ImageView)itemView.findViewById(R.id.image_view_show_card);
            ShowTitle=(TextView) itemView.findViewById(R.id.text_view_title_card);
            ShowRating=(TextView)itemView.findViewById(R.id.rating_show_card_text);
            ShowgenreText=(TextView)itemView.findViewById(R.id.text_view_genre);
            ShowFavimage=(ImageButton)itemView.findViewById(R.id.image_button_fav_show_card);

            imageLayout.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels*0.5);
            imageLayout.getLayoutParams().height =(int) (context.getResources().getDisplayMetrics().heightPixels*0.5/1.6);

            ShowCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent =new Intent(context, getShowDetail.class);
                    intent.putExtra(Constants.TV_SHOW_ID,kShows.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
            ShowFavimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Favourite.addTVShowsToFav(context,kShows.get(getAdapterPosition()).getId(),kShows.get(getAdapterPosition()).getPosterPath(),kShows.get(getAdapterPosition()).getName());
                    ShowFavimage.setImageResource(R.drawable.ic_facourite);
                    ShowFavimage.setEnabled(false);
                }
            });
        }
    }
}
