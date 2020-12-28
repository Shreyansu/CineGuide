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

import com.shreyansu.chillout.R;
import com.shreyansu.chillout.activities.getShowDetail;
import com.shreyansu.chillout.network.tvshows.ShowDetail;
import com.shreyansu.chillout.util.Constants;
import com.shreyansu.chillout.util.Favourite;

import java.util.List;

public class ShowsDetailSmallAdapter extends RecyclerView.Adapter<ShowsDetailSmallAdapter.ShowViewHolder>{

    private Context context;
    private List<ShowDetail> kShows;

    public ShowsDetailSmallAdapter(Context context, List<ShowDetail> kShows) {
        this.context = context;
        this.kShows = kShows;
    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShowViewHolder(LayoutInflater.from(context).inflate(R.layout.small_tem_adapter,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ShowViewHolder holder, int position)
    {


    }

    @Override
    public int getItemCount() {
        return kShows.size();
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder
    {
        private CardView ShowCard;
        private ImageView ShowPosterImage;
        private ImageButton ShowFavButtonImage;
        private TextView ShowTitle;
        public ShowViewHolder(@NonNull View itemView)
        {

            super(itemView);
            ShowCard=(CardView) itemView.findViewById(R.id.card_view_show_card);
            ShowPosterImage=(ImageView) itemView.findViewById(R.id.image_view_show_card);
            ShowTitle=(TextView) itemView.findViewById(R.id.text_view_title_show_card);
            ShowFavButtonImage=(ImageButton) itemView.findViewById(R.id.image_button_fav_show_card);

            ShowPosterImage.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.31);
            ShowPosterImage.getLayoutParams().height = (int) ((context.getResources().getDisplayMetrics().widthPixels * 0.31) / 0.9);


            ShowCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent =new Intent(context, getShowDetail.class);
                    intent.putExtra(Constants.TV_SHOW_ID,kShows.get(getAdapterPosition()).getId());
                    context.startActivity(intent);

                }
            });
            ShowFavButtonImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Favourite.addTVShowsToFav(context,kShows.get(getAdapterPosition()).getId(),kShows.get(getAdapterPosition()).getPosterPath(),kShows.get(getAdapterPosition()).getName());
                    ShowFavButtonImage.setImageResource(R.drawable.ic_facourite);
                    ShowFavButtonImage.setEnabled(false);
                }
            });
        }
    }

}
