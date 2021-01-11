package com.shreyansu.chillout.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shreyansu.chillout.R;
import com.shreyansu.chillout.activities.CastDetailActivity;
import com.shreyansu.chillout.network.tvshows.CastDetail;
import com.shreyansu.chillout.util.Constants;

import java.util.List;

public class ShowCastsAdapte extends RecyclerView.Adapter<ShowCastsAdapte.CastShowViewHolder>
{
    private Context kContext;
    private List<CastDetail> kCasts;

    public ShowCastsAdapte(Context kContext, List<CastDetail> kCasts)
    {

        this.kContext = kContext;
        this.kCasts = kCasts;
    }

    @NonNull
    @Override
    public CastShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return new CastShowViewHolder(LayoutInflater.from(kContext).inflate(R.layout.cast_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull CastShowViewHolder holder, int position)
    {
        Glide.with(kContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500 +kCasts.get(position).getProfilePath())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.castImageView);

        if(kCasts.get(position).getName()!=null)
            holder.nameText.setText(kCasts.get(position).getName());
        else
            holder.nameText.setText("");

        if(kCasts.get(position).getCharacter()!=null)
            holder.characterText.setText(kCasts.get(position).getCharacter());
        else
            holder.characterText.setText("");

    }

    @Override
    public int getItemCount() {
        return kCasts.size();
    }

    public class CastShowViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView castImageView;
        private TextView nameText;
        private TextView characterText;

        public CastShowViewHolder(@NonNull View itemView) {
            super(itemView);
            castImageView=(ImageView) itemView.findViewById(R.id.cast_image);
            nameText=(TextView) itemView.findViewById(R.id.original_name);
            characterText=(TextView) itemView.findViewById(R.id.character_name);

            castImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(kContext, CastDetailActivity.class);
                    intent.putExtra(Constants.PERSON_ID,kCasts.get(getAdapterPosition()).getId());
                    kContext.startActivity(intent);
                 }
            });
        }
    }
}
