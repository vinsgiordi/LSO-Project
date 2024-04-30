package com.example.moviehub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviehub.Model.Movie;
import com.example.moviehub.View.MovieActivity;
import com.example.moviehub.MainActivity;
import com.example.moviehub.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Movie> listMovie;

    private static final float[] TRITANOPIA = {
            0.95f,0.05f,0,0,0,
            0,0.433f,0.567f,0,0,
            0,0.475f,0.525f,0,0,
            0,0,0,1,0,
            0,0,0,0,1
    };

    private static final float[] ACROMATOPSIA = {
            0.299f,0.587f,0.114f,0,0,
            0.299f,0.587f,0.114f,0,0,
            0.299f,0.587f,0.114f,0,0,
            0,0,0,1,0,
            0,0,0,0,1
    };

    private static final float[] DEUTERANOPIA = {
            0.625f,0.375f,0,0,0,
            0.7f,0.3f,0,0,0,
            0,0.3f,0.7f,0,0,
            0,0,0,1,0,
            0,0,0,0,1
    };

    public MovieAdapter(ArrayList<Movie> listMovie, Context context) {
        this.context = context;
        this.listMovie = listMovie;
    }

    public Movie getItem(int position) {
        return listMovie.get(position);
    }

    public ArrayList<Movie> getListMedia() {
        return listMovie;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false);
        return new MovieAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {
        holder.setMovie(listMovie.get(position));

        switch (MainActivity.tema) {
            case "Disattivata":
                setFiltroDefault(holder);
                break;

            case "Filtro biancoNero":
                setFiltroDefault(holder);
                setFiltroBiancoNero(holder);
                break;

            case "Filtro verde":
                setFiltroDefault(holder);
                setFiltroDeuteranopia(holder);
                break;

            case "Filtro blu/giallo":
                setFiltroDefault(holder);
                setFiltroTritanopia(holder);
                break;

            case "Incrementa Dimensioni":
                increaseSize(holder);
                break;

            case "Filtro biancoNero + Incrementa Dimensioni":
                setFiltroDefault(holder);
                setFiltroBiancoNero(holder);
                increaseSize(holder);
                break;

            case "Filtro verde + Incrementa Dimensioni":
                setFiltroDefault(holder);
                setFiltroDeuteranopia(holder);
                increaseSize(holder);
                break;

            case "Filtro blu/giallo + Incrementa Dimensioni":
                setFiltroDefault(holder);
                setFiltroTritanopia(holder);
                increaseSize(holder);
                break;
        }

        holder.movieImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieActivity.class);
                intent.putExtra("Media", listMovie.get(position));
                context.startActivity(intent);
            }
        });
    }

    private void setFiltroTritanopia(MyViewHolder holder) {
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(TRITANOPIA);
        holder.movieImg.setColorFilter(filter);
        holder.movieTitle.setTextColor(context.getResources().getColor(R.color.tritanopia_color2));
    }

    private void setFiltroDeuteranopia(MyViewHolder holder) {
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(DEUTERANOPIA);
        holder.movieImg.setColorFilter(filter);
        holder.movieTitle.setTextColor(context.getResources().getColor(R.color.deuteranopia_color1));
    }

    private void setFiltroBiancoNero(MyViewHolder holder) {
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(ACROMATOPSIA);
        holder.movieImg.setColorFilter(filter);
        holder.movieTitle.setTextColor(context.getResources().getColor(R.color.biancoNero_colore1));
    }

    private void increaseSize(MovieAdapter.MyViewHolder holder) {
        holder.movieTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.label_large));
        holder.movieImg.getLayoutParams().width = 400;
        holder.movieImg.getLayoutParams().height = 650;
    }

    private void setFiltroDefault(MovieAdapter.MyViewHolder holder) {
        holder.movieTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.label_dimen));
        holder.movieImg.getLayoutParams().width = 300;
        holder.movieImg.getLayoutParams().height = 450;
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView movieTitle;
        public ImageView movieImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.mediaTitle);
            movieImg = itemView.findViewById(R.id.mediaImg);
        }

        void setMovie(Movie movie) {
            movieTitle.setText(movie.getTitle());

            if (movie.getCover() == null) {
                movieImg.setImageResource(R.drawable.ic_launcher_background);
            }
            else {
                Picasso.with(context).load("https://image.tmdb.org/t/p/w500" + movie.getCover()).into(movieImg);
            }
        }
    }
}