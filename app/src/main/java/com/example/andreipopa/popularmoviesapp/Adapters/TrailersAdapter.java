package com.example.andreipopa.popularmoviesapp.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.andreipopa.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

import com.example.andreipopa.popularmoviesapp.Objects.Trailer;

/**
 * Created by Andrei Popa on 3/31/2018.
 */



public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder>{

    private Context context;
    private LayoutInflater mInflater;
    private Trailer[] trailersList;
    private TrailerItemClickListener mTrailerItemClickListener;

    public interface TrailerItemClickListener{
        void TrailerItemClick(TrailersAdapter.TrailerViewHolder item);
    }

    public TrailersAdapter(Context context, Trailer[] trailersList, TrailerItemClickListener mTrailerItemClickListener){
      this.context= context;
      this.trailersList= trailersList;
      this.mTrailerItemClickListener=mTrailerItemClickListener;
      this.mInflater= LayoutInflater.from(context);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= mInflater.inflate(R.layout.trailer_item,parent,false);
        TrailerViewHolder trailerViewHolder= new TrailerViewHolder(view);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
       holder.setThumbnailLink(trailersList[position].getThumbnailLinks());
       holder.setTrailerLink(trailersList[position].getTrailerLink());

        Picasso.with(context)
                .load(Uri.parse(holder.getThumbnailLink()))
                .resize(480,360)
                .into(holder.thumbnailImageView);
    }

    @Override
    public int getItemCount() {
        if(trailersList==null){
            return 0;
        }else{
            return trailersList.length;
        }
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{

        public ImageView thumbnailImageView;
        private String thumbnailLink;
        private String trailerLink;
        private TrailerViewHolder trailerViewHolder;

        public TrailerViewHolder(View itemView) {
            super(itemView);
             this.trailerViewHolder=this;
            thumbnailImageView=(ImageView)itemView.findViewById(R.id.trailer_thumbnail_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTrailerItemClickListener.TrailerItemClick(trailerViewHolder);
                }
            });
        }

        public void setThumbnailLink(String s){
            thumbnailLink=s;
        }
        public void setTrailerLink(String s){
            trailerLink=s;
        }
        public String getThumbnailLink(){
            return thumbnailLink;
        }
        public String getTrailerLink(){
            return  trailerLink;
        }
    }
}
