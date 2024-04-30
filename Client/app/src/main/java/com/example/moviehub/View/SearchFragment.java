package com.example.moviehub.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviehub.MainActivity;
import com.example.moviehub.R;

public class SearchFragment extends Fragment {

    private TextView cercaTitolo;
    private EditText cercaEditText;
    private Button cercaBtn;

    public SearchFragment() {
        // Costruttore vuoto richiesto per Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla il layout per questo fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Inizializza le view
        cercaTitolo = view.findViewById(R.id.cercaTitolo);
        cercaEditText = view.findViewById(R.id.cercaEditText);
        cercaBtn = view.findViewById(R.id.cercaBtn);

        // Imposta il listener per il pulsante di ricerca
        cercaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Controlla se il campo di ricerca è vuoto
                if (cercaEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Il campo di ricerca è vuoto", Toast.LENGTH_SHORT).show();
                } else {
                    // Avvia l'attività dei risultati di ricerca
                    Intent intent = new Intent(getContext(), ResultsActivity.class);
                    intent.putExtra("query", cercaEditText.getText().toString());
                    startActivity(intent);
                }
            }
        });

        // Applica il tema corrente
        applyFilter();

        return view;
    }

    // Metodo per applicare il tema corrente in base alla variabile "tema" della MainActivity
    private void applyFilter() {
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

            case "Incrementa Dimensioni":
                increaseSize();
                break;

            case "Filtro biancoNero + Incrementa Dimensioni":
                setFiltroBiancoNero();
                increaseSize();
                break;

            case "Filtro verde + Incrementa Dimensioni":
                setFiltroDeuteranopia();
                increaseSize();
                break;

            case "Filtro blu/giallo + Incrementa Dimensioni":
                setFiltroTritanopia();
                increaseSize();
                break;

            default:
                setFiltroDefault();
                break;
        }
    }

    // Imposta il filtro Tritanopia
    private void setFiltroTritanopia() {
        cercaTitolo.setTextColor(getResources().getColor(R.color.tritanopia_color2));
        cercaEditText.setTextColor(getResources().getColor(R.color.tritanopia_color2));
        cercaBtn.setBackgroundColor(getResources().getColor(R.color.tritanopia_color3));
    }

    // Imposta il filtro Deuteranopia
    private void setFiltroDeuteranopia() {
        cercaTitolo.setTextColor(getResources().getColor(R.color.deuteranopia_color1));
        cercaEditText.setTextColor(getResources().getColor(R.color.deuteranopia_color1));
        cercaBtn.setBackgroundColor(getResources().getColor(R.color.deuteranopia_color2));
    }

    // Aumenta le dimensioni del testo
    private void increaseSize() {
        cercaEditText.setTextSize(getResources().getDimension(R.dimen.label_large2));
        cercaBtn.setTextSize(getResources().getDimension(R.dimen.label_large2));
    }

    // Imposta il filtro predefinito
    private void setFiltroDefault() {
        cercaTitolo.setTextColor(getResources().getColor(R.color.black));
        cercaEditText.setTextColor(getResources().getColor(R.color.black));
        cercaBtn.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    // Imposta il filtro Bianco e Nero
    private void setFiltroBiancoNero() {
        cercaTitolo.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
        cercaEditText.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
        cercaBtn.setBackgroundColor(getResources().getColor(R.color.biancoNero_colore1));
    }
}
