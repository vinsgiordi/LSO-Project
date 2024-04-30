package com.example.moviehub.View;

import android.os.Bundle;
import androidx.core.content.ContextCompat;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviehub.Adapters.MovieAdapter;
import com.example.moviehub.Model.Api;
import com.example.moviehub.Model.Movie;
import com.example.moviehub.Model.ApiResponse;
import com.example.moviehub.Model.Listener;
import com.example.moviehub.MainActivity;
import com.example.moviehub.R;

import java.util.ArrayList;
import java.util.List;


public class HomepageFragment extends Fragment {

    private RecyclerView popolariRV, comingRV, seriesRV;
    private MovieAdapter popularAdapter, upcomingAdapter, seriesAdapter;
    private TextView popolariLbl, cominglbl, serieslbl;
    private ImageView popolariimg,comingimg,appimg, seriesimg;

    public HomepageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Api api = new Api(getContext());
        api.getRecommandedMovies(listener, "it-IT");
        api.getUpcomingMovies(listener,"it-IT");
        api.getRecommandedTv(listener,"it-IT");
    }

    final Listener<ApiResponse> listener = new Listener<ApiResponse>() {
        @Override
        public void onFetchMovie(List<Movie> list, String message) {
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "No data found!", Toast.LENGTH_SHORT).show();
            }
            else {
                if(popularAdapter == null){
                    popularAdapter = new MovieAdapter((ArrayList<Movie>) list, getContext());
                    popolariRV.setAdapter(popularAdapter);
                }
                else if(upcomingAdapter == null) {
                    upcomingAdapter = new MovieAdapter((ArrayList<Movie>) list, getContext());
                    comingRV.setAdapter(upcomingAdapter);
                }else{
                    seriesAdapter = new MovieAdapter((ArrayList<Movie>) list, getContext());
                    seriesRV.setAdapter(seriesAdapter);
                }
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getContext(), "An Error Occurred!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        popolariLbl = view.findViewById(R.id.popolari);
        popolariRV = view.findViewById(R.id.resultsRV);
        popolariRV.setHasFixedSize(true);
        popolariimg = view.findViewById(R.id.MovieHub);
        comingimg = view.findViewById(R.id.MovieHub2);
        appimg = view.findViewById(R.id.icon_app);
        cominglbl = view.findViewById(R.id.comingSoon);
        comingRV = view.findViewById(R.id.comingRV);
        comingRV.setHasFixedSize(true);
        seriesimg = view.findViewById(R.id.MovieHub3);
        serieslbl = view.findViewById(R.id.tvSeries);
        seriesRV= view.findViewById(R.id.seriesRV);
        seriesRV.setHasFixedSize(true);


        LinearLayoutManager ll1 = new LinearLayoutManager(this.getContext());
        ll1.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager ll2 = new LinearLayoutManager(this.getContext());
        ll2.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager ll3 = new LinearLayoutManager(this.getContext());
        ll3.setOrientation(LinearLayoutManager.HORIZONTAL);

        popolariRV.setLayoutManager(ll1);
        comingRV.setLayoutManager(ll2);
        seriesRV.setLayoutManager(ll3);

        switch (MainActivity.tema) {
            case "Disattivata":
                setFiltroDefault();
                break;

            case "Filtro biancoNero":
                setFiltroDefault();
                setFiltroBiancoNero();
                break;

            case "Filtro verde":
                setFiltroDefault();
                setFiltroDeuteranopia();
                break;

            case "Filtro blu/giallo":
                setFiltroDefault();
                setFiltroTritanopia();
                break;

            case "Incrementa Dimensioni":
                increaseText();
                break;

            case "Filtro biancoNero + Incrementa Dimensioni":
                setFiltroDefault();
                setFiltroBiancoNero();
                increaseText();
                break;

            case "Filtro verde + Incrementa Dimensioni":
                setFiltroDefault();
                setFiltroDeuteranopia();
                increaseText();
                break;

            case "Filtro blu/giallo + Incrementa Dimensioni":
                setFiltroDefault();
                setFiltroTritanopia();
                increaseText();
                break;
        }

        return  view;
    }

    private void setFiltroTritanopia() {
        cominglbl.setTextColor(getResources().getColor(R.color.tritanopia_color2));
        comingimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.tritanopia_color3));
        popolariLbl.setTextColor(getResources().getColor(R.color.tritanopia_color2));
        popolariimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.tritanopia_color3));
        serieslbl.setTextColor(getResources().getColor(R.color.tritanopia_color2));
        seriesimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.tritanopia_color3));
        appimg.setImageResource(R.drawable.icon_black);
        appimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.tritanopia_color3));
    }

    private void setFiltroDeuteranopia() {
        popolariLbl.setTextColor(getResources().getColor(R.color.deuteranopia_color1));
        popolariimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.deuteranopia_color2));
        cominglbl.setTextColor(getResources().getColor(R.color.deuteranopia_color1));
        comingimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.deuteranopia_color2));
        serieslbl.setTextColor(getResources().getColor(R.color.deuteranopia_color1));
        seriesimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.deuteranopia_color2));
        appimg.setImageResource(R.drawable.icon_black);
        appimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.deuteranopia_color2));
    }

    private void setFiltroBiancoNero() {
        popolariLbl.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
        cominglbl.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
        comingimg.setColorFilter(getResources().getColor(R.color.biancoNero_colore1));
        popolariimg.setColorFilter(getResources().getColor(R.color.biancoNero_colore1));
        serieslbl.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
        seriesimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.biancoNero_colore1));
        appimg.setImageResource(R.drawable.icon_black);
        appimg.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));

    }

    private void increaseText() {
        increaseSize(true);
    }

    private void setFiltroDefault() {
        increaseSize(false);
    }

    private void increaseSize(boolean increaseSize) {
        popolariLbl.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(increaseSize ? R.dimen.label_large : R.dimen.label_dimen));
        cominglbl.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(increaseSize ? R.dimen.label_large : R.dimen.label_dimen));
        serieslbl.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(increaseSize ? R.dimen.label_large : R.dimen.label_dimen));
        ViewGroup.LayoutParams paramspopular = popolariimg.getLayoutParams();
        ViewGroup.LayoutParams paramscoming = comingimg.getLayoutParams();
        ViewGroup.LayoutParams paramsseries = seriesimg.getLayoutParams();
        int imageWidth = (int) getResources().getDimension(increaseSize ? R.dimen.label_image_large : R.dimen.label_image_dimen);
        paramspopular.width = imageWidth;
        paramscoming.width = imageWidth;
        paramsseries.width = imageWidth;
        popolariimg.setLayoutParams(paramspopular);
        comingimg.setLayoutParams(paramscoming);
        seriesimg.setLayoutParams(paramsseries);
    }

}