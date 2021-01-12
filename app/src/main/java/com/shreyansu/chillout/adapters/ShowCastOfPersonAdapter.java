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
import com.shreyansu.chillout.activities.getShowDetail;
import com.shreyansu.chillout.network.tvshows.CastPerson;
import com.shreyansu.chillout.util.Constants;
import java.util.List;

public class ShowCastOfPersonAdapter extends RecyclerView.Adapter<ShowCastOfPersonAdapter.ViewHolder> {

    private Context kContext;
    private List<CastPerson> kShowCast;

    public ShowCastOfPersonAdapter(Context kContext, List<CastPerson> kShowCast) {
        this.kContext = kContext;
        this.kShowCast = kShowCast;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(kContext).inflate(R.layout.item_show_of_cast,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Glide.with(kContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500+kShowCast.get(position).getPosterPath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.kShowPosterImage);


        if(kShowCast.get(position).getName()!=null)
            holder.kShowTitle.setText(kShowCast.get(position).getName());
        else
            holder.kShowTitle.setText("");

        if(kShowCast.get(position).getCharacter()!=null)
            holder.kShowCharacterName.setText(kShowCast.get(position).getCharacter());
        else
            holder.kShowCharacterName.setText("");

    }

    @Override
    public int getItemCount() {
        return kShowCast.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private CardView kShowCardView;
        private ImageView kShowPosterImage;
        private TextView kShowTitle;
        private TextView kShowCharacterName;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            kShowCardView=(CardView) itemView.findViewById(R.id.movie_cast_card);
            kShowPosterImage =(ImageView) itemView.findViewById(R.id.image_view_show_cast);

            kShowTitle=(TextView) itemView.findViewById(R.id.cast_movie_title);
            kShowCharacterName=(TextView) itemView.findViewById(R.id.cast_character_name);

            kShowPosterImage.getLayoutParams().width=(int) (kContext.getResources().getDisplayMetrics().widthPixels*0.30);
            kShowPosterImage.getLayoutParams().width=(int) ((kContext.getResources().getDisplayMetrics().widthPixels*030)/0.65);


            kShowCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(kContext, getShowDetail.class);
                    intent.putExtra(Constants.TV_SHOW_ID,kShowCast.get(getAdapterPosition()).getId());
                    kContext.startActivity(intent);
                }
            });
        }
    }
}
