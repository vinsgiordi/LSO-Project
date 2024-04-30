package com.example.moviehub.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviehub.Adapters.ResultAdapter;
import com.example.moviehub.Model.Api;
import com.example.moviehub.Model.Movie;
import com.example.moviehub.Model.ApiResponse;
import com.example.moviehub.Model.Listener;
import com.example.moviehub.MainActivity;
import com.example.moviehub.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private TextView querySearch;
    private RecyclerView resultsRV;
    private ImageButton backBtn;
    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Ottieni la query dalla MainActivity
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");

        // Inizializza le view
        querySearch = findViewById(R.id.queryTitolo);
        resultsRV = findViewById(R.id.resultsRV);
        backBtn = findViewById(R.id.backBtn);

        // Imposta il layout manager per il RecyclerView
        resultsRV.setHasFixedSize(true);
        resultsRV.setLayoutManager(new LinearLayoutManager(this));

        // Imposta il testo della query sulla TextView
        querySearch.setText(query);

        // Chiama l'API per ottenere i risultati di ricerca
        Api api = new Api(this);
        api.getSearchResults(listener, "it-IT", query);

        // Applica il tema corrente
        applyTheme();

        // Imposta il listener per il pulsante di ritorno
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    // Listener per l'API di ricerca
    final Listener<ApiResponse> listener = new Listener<ApiResponse>() {
        @Override
        public void onFetchMovie(List<Movie> list, String message) {
            if (list.isEmpty()) {
                Toast.makeText(ResultsActivity.this, "Nessun dato trovato!", Toast.LENGTH_SHORT).show();
            } else {
                // Se la lista non è vuota, crea e imposta l'adapter per il RecyclerView
                if (resultAdapter == null) {
                    resultAdapter = new ResultAdapter((ArrayList<Movie>) list, ResultsActivity.this);
                    resultsRV.setAdapter(resultAdapter);
                }
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(ResultsActivity.this, "Si è verificato un errore!", Toast.LENGTH_SHORT).show();
        }
    };

    // Metodo per applicare il tema corrente
    private void applyTheme() {
        switch (MainActivity.tema) {
            case "Filtro biancoNero":
                setFiltroBiancoNero();
                break;

            case "Filtro verde":
                setFiltroDeuteranopia();
                break;

            case "Filtro blu/giallo":
                setFiltroTritanopia();
                break;

            case "Disattivata":
                // Nessun filtro aggiuntivo
                break;

            default:
                // Nessuna azione per il tema predefinito
                break;
        }
    }

    // Imposta il filtro Tritanopia
    private void setFiltroTritanopia() {
        backBtn.setColorFilter(getResources().getColor(R.color.blue));
        querySearch.setTextColor(getResources().getColor(R.color.blue));
    }

    // Imposta il filtro Deuteranopia
    private void setFiltroDeuteranopia() {
        backBtn.setColorFilter(getResources().getColor(R.color.deuteranopia_color2));
        querySearch.setTextColor(getResources().getColor(R.color.deuteranopia_color2));
    }

    // Imposta il filtro Bianco e Nero
    private void setFiltroBiancoNero() {
        backBtn.setColorFilter(getResources().getColor(R.color.black));
        querySearch.setTextColor(getResources().getColor(R.color.black));
    }
}
