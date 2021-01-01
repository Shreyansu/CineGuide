package com.shreyansu.chillout.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.shreyansu.chillout.network.movies.MovieCastDetail;
import com.shreyansu.chillout.util.Constants;

import java.util.List;

public class MovieCastsAdapter extends RecyclerView.Adapter<MovieCastsAdapter.CastViewHolder> {


    private Context kContext;
    private List<MovieCastDetail> Kcasts;

    public MovieCastsAdapter(Context kContext, List<MovieCastDetail> kcasts) {
        this.kContext = kContext;
        Kcasts = kcasts;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new CastViewHolder(LayoutInflater.from(kContext).inflate(R.layout.video_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position)
    {
        Glide.with(kContext.getApplicationContext()).load(Constants.IMAGE_LOADING_BASE_URL_500+Kcasts.get(position).getProfilePath())
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.CastImage);

        if(Kcasts.get(position).getName()!=null)
            holder.OriginalName.setText(Kcasts.get(position).getName());
        else
            holder.OriginalName.setText("");
        if(Kcasts.get(position).getCharacter()!=null)
            holder.CharacterName.setText(Kcasts.get(position).getCharacter());
        else
            holder.CharacterName.setText("");


    }

    @Override
    public int getItemCount() {
        return Kcasts.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView CastImage;
        private TextView OriginalName;
        private TextView CharacterName;
        public CastViewHolder(@NonNull View itemView)
        {
            super(itemView);

            CastImage=(ImageView)itemView.findViewById(R.id.cast_image);
            CharacterName=(TextView) itemView.findViewById(R.id.character_name);
            OriginalName=(TextView)itemView.findViewById(R.id.original_name);


            CastImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent =new Intent(kContext, CastDetailActivity.class);
                    intent.putExtra(Constants.PERSON_ID,Kcasts.get(getAdapterPosition()).getId());
                    kContext.startActivity(intent);
                }
            });
        }
    }
}
