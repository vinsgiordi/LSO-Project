package com.example.moviehub.View;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.moviehub.Model.Movie;
import com.example.moviehub.MainActivity;
import com.example.moviehub.R;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageView movieCover;
    private TextView movieTitolo, movieTrama, movieDataRilascio, movieValutazione, data;
    // Matrici di filtro per diverse condizioni di visione
    private static final float[] TRITANOPIA = {
            0.95f, 0.05f, 0, 0, 0,
            0, 0.433f, 0.567f, 0, 0,
            0, 0.475f, 0.525f, 0, 0,
            0, 0, 0, 1, 0,
            0, 0, 0, 0, 1
    };

    private static final float[] ACROMATOPSIA = {
            0.299f, 0.587f, 0.114f, 0, 0,
            0.299f, 0.587f, 0.114f, 0, 0,
            0.299f, 0.587f, 0.114f, 0, 0,
            0, 0, 0, 1, 0,
            0, 0, 0, 0, 1
    };

    private static final float[] DEUTERANOPIA = {
            0.625f, 0.375f, 0, 0, 0,
            0.7f, 0.3f, 0, 0, 0,
            0, 0.3f, 0.7f, 0, 0,
            0, 0, 0, 1, 0,
            0, 0, 0, 0, 1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Inizializza le view
        backBtn = findViewById(R.id.backBtn);
        movieCover = findViewById(R.id.movieCover);
        movieTitolo = findViewById(R.id.movieTitle);
        movieTrama = findViewById(R.id.tramaMedia);
        movieDataRilascio = findViewById(R.id.dataRilascio);
        movieValutazione = findViewById(R.id.valutazioneMedia);
        data = findViewById(R.id.data);

        // Imposta il listener per il pulsante di ritorno
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Ottiene il media dalla view precedente
        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra("Media");

        // Carica l'immagine del film utilizzando Picasso
        Picasso.with(MovieActivity.this).load("https://image.tmdb.org/t/p/w500" + movie.getBackdrop()).into(movieCover);

        // Imposta i dettagli del film nelle TextView
        movieTitolo.setText(movie.getTitle());
        movieTrama.setText(movie.getDescription());
        movieDataRilascio.setText(movie.getReleaseDate());
        movieValutazione.setText(movie.getValutation());

        // Applica il filtro in base alle impostazioni di visione
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
                increaseSize();
                break;

            case "Filtro biancoNero + Incrementa Dimensioni":
                setFiltroDefault();
                setFiltroBiancoNero();
                increaseSize();
                break;

            case "Filtro verde + Incrementa Dimensioni":
                setFiltroDefault();
                setFiltroDeuteranopia();
                increaseSize();
                break;

            case "Filtro blu/giallo + Incrementa Dimensioni":
                setFiltroDefault();
                setFiltroTritanopia();
                increaseSize();
                break;
        }
    }

    // Imposta il filtro per la tritanopia
    private void setFiltroTritanopia() {
        movieCover.setColorFilter(new ColorMatrixColorFilter(TRITANOPIA));
        movieTitolo.setTextColor(getResources().getColor(R.color.tritanopia_color3));
        backBtn.setColorFilter(getResources().getColor(R.color.tritanopia_color3));
        movieTrama.setTextColor(getResources().getColor(R.color.tritanopia_color2));
        movieDataRilascio.setTextColor(getResources().getColor(R.color.tritanopia_color3));
        data.setTextColor(getResources().getColor(R.color.tritanopia_color3));
    }

    // Imposta il filtro per la deuteranopia
    private void setFiltroDeuteranopia() {
        movieCover.setColorFilter(new ColorMatrixColorFilter(DEUTERANOPIA));
        backBtn.setColorFilter(getResources().getColor(R.color.deuteranopia_color2));
        movieTitolo.setTextColor(getResources().getColor(R.color.deuteranopia_color2));
        movieTrama.setTextColor(getResources().getColor(R.color.deuteranopia_color1));
        movieDataRilascio.setTextColor(getResources().getColor(R.color.deuteranopia_color2));
        data.setTextColor(getResources().getColor(R.color.deuteranopia_color2));
    }

    // Imposta il filtro per il bianco e nero
    private void setFiltroBiancoNero() {
        movieCover.setColorFilter(new ColorMatrixColorFilter(ACROMATOPSIA));
        backBtn.setColorFilter(getResources().getColor(R.color.biancoNero_colore4));
        movieTrama.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
        movieDataRilascio.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
        data.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
    }

    // Aumenta le dimensioni dei testi
    private void increaseSize() {
        movieCover.clearColorFilter();
        movieTrama.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_large));
        movieDataRilascio.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_large));
        movieValutazione.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_large));
    }

    // Imposta i valori predefiniti
    private void setFiltroDefault() {
        movieTrama.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_dimen));
        movieDataRilascio.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_dimen));
    }
}

