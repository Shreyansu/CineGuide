package com.shreyansu.chillout.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
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
import com.shreyansu.chillout.activities.CastDetailActivity;
import com.shreyansu.chillout.activities.getMovieDetails;
import com.shreyansu.chillout.activities.getShowDetail;
import com.shreyansu.chillout.network.search.SearchResult;
import com.shreyansu.chillout.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context kContext;
    private List<SearchResult> kSearchResults;

    public SearchAdapter(Context kContext, List<SearchResult> kSearchResults) {
        this.kContext = kContext;
        this.kSearchResults = kSearchResults;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return new SearchViewHolder(LayoutInflater.from(kContext).inflate(R.layout.item_search_result,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position)
    {
        Glide.with(kContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500+ kSearchResults.get(position).getPosterPath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.kImageView);

        if(kSearchResults.get(position).getName() !=null && !kSearchResults.get(position).getName().trim().isEmpty())
            holder.nameTextView.setText(kSearchResults.get(position).getName());
        else
            holder.nameTextView.setText("");

        if(kSearchResults.get(position).getMediaType()!=null && kSearchResults.get(position).getMediaType().equals("movie"))
            holder.mediaTypeTextView.setText("Movie");
        else if(kSearchResults.get(position).getMediaType()!=null && kSearchResults.get(position).getMediaType().equals("tv"))
            holder.mediaTypeTextView.setText("TV Show");
        else if(kSearchResults.get(position).getMediaType()!=null && kSearchResults.get(position).getMediaType().equals("person"))
            holder.mediaTypeTextView.setText("Person");
        else
            holder.mediaTypeTextView.setText("");

        if(kSearchResults.get(position).getOverview()!=null && !kSearchResults.get(position).getOverview().trim().isEmpty())
            holder.overView.setText(kSearchResults.get(position).getOverview());
        else
            holder.overView.setText("");


        if(kSearchResults.get(position).getReleaseDate() !=null && !kSearchResults.get(position).getReleaseDate().trim().isEmpty())
        {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
            try {
                Date releaseDate = sdf1.parse(kSearchResults.get(position).getReleaseDate());
                holder.year.setText(sdf2.format(releaseDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.year.setText("");
        }



    }

    @Override
    public int getItemCount()
    {
        return kSearchResults.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder
    {
        private CardView cardView;
        private ImageView kImageView;
        private TextView nameTextView;
        private TextView mediaTypeTextView;
        private TextView overView;
        private TextView year;

        public SearchViewHolder(@NonNull View itemView)
        {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.search_card_view);
            kImageView=(ImageView)itemView.findViewById(R.id.poster_image_search);
            nameTextView=(TextView)itemView.findViewById(R.id.text_view_name_search);
            mediaTypeTextView=(TextView)itemView.findViewById(R.id.text_view_media_type_search);
            overView=(TextView) itemView.findViewById(R.id.text_view_overview);
            year=(TextView) itemView.findViewById(R.id.text_year_search);


            kImageView.getLayoutParams().width=(int) (kContext.getResources().getDisplayMetrics().widthPixels*0.31);
            kImageView.getLayoutParams().height=(int) (kContext.getResources().getDisplayMetrics().widthPixels*0.31/0.65);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(kSearchResults.get(getAdapterPosition()).getMediaType().equals("movie"))
                    {
                        Intent intent =new Intent(kContext, getMovieDetails.class);
                        intent.putExtra(Constants.MOVIE_ID,kSearchResults.get(getAdapterPosition()).getId());
                        kContext.startActivity(intent);

                    }
                    else if(kSearchResults.get(getAdapterPosition()).getMediaType().equals("tv"))
                    {
                        Intent intent =new Intent(kContext, getShowDetail.class);
                        intent.putExtra(Constants.TV_SHOW_ID,kSearchResults.get(getAdapterPosition()).getId());
                        kContext.startActivity(intent);
                    }
                    else if(kSearchResults.get(getAdapterPosition()).getMediaType().equals("person"))
                    {
                        Intent intent =new Intent(kContext, CastDetailActivity.class);
                        intent.putExtra(Constants.PERSON_ID,kSearchResults.get(getAdapterPosition()).getId());
                        kContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
