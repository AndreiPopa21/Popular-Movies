package com.example.andreipopa.popularmoviesapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.andreipopa.popularmoviesapp.NetworkUtils;
import com.example.andreipopa.popularmoviesapp.Objects.Movie;
import com.example.andreipopa.popularmoviesapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayInputStream;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{


    private Movie[] moviesList;
    private LayoutInflater mInflater;
    private Context context;
    private MovieClickItemListener mMovieClickListener;
    private boolean wasOnlineQuery;

    public interface MovieClickItemListener{
        void onMovieClickItem(MovieViewHolder movieViewHolder,boolean wasOnlineQuery,Bitmap posterBitmap);
    }



    public MoviesAdapter(Context ctxt, Movie[] list,MovieClickItemListener listener,boolean wasOnlineQuery){

        this.moviesList=list;
        this.mInflater=LayoutInflater.from(ctxt);
        this.context=ctxt;
        this.mMovieClickListener=listener;
        this.wasOnlineQuery=wasOnlineQuery;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= mInflater.inflate(R.layout.movie_item,parent,false);
        MovieViewHolder movieViewHolder=new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        holder.movieClass=moviesList[position];

        if(wasOnlineQuery){

            Picasso.with(context).
                    load(holder.movieClass.getFullPosterLink()).
                    into(holder.target);



        }

        if(!wasOnlineQuery){
            // Log.v("BYTE ARRAY SIZE",String.valueOf(holder.movieClass.getTheActualPosterByteArray().length));
            // ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(holder.movieClass.getTheActualPosterByteArray());
            // Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
            // holder.moviePoster.setImageBitmap(bitmap);
            Bitmap bitmap= NetworkUtils.convertByteArrayToBitmap(holder.movieClass.getPosterByteArray());
            holder.moviePoster.setImageBitmap(bitmap);
        }


    }

    @Override
    public int getItemCount() {
        if(moviesList==null || moviesList.length==0){
            return 0;
        }
        else{
            return moviesList.length;
        }
    }



    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePoster;
        public Movie movieClass;
        public RelativeLayout posterFrame;
        public MovieViewHolder movieViewHolder;
        public Bitmap posterImageBitmap;

        private Target target= new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                posterImageBitmap= bitmap;
               // byte[] posterByteArray= NetworkUtils.convertBitmapToByteArray(bitmap);
               // movieClass.setPosterByteArray(posterByteArray);
                moviePoster.setImageBitmap(posterImageBitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                posterImageBitmap=null;
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieViewHolder=this;
            moviePoster= (ImageView) itemView.findViewById(R.id.movie_poster_id);
            posterFrame= (RelativeLayout)itemView.findViewById(R.id.item_frame);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMovieClickListener.onMovieClickItem(movieViewHolder,wasOnlineQuery,posterImageBitmap);
                }
            });
        }


    }
}
