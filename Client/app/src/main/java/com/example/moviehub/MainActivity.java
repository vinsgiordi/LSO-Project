package com.example.moviehub;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import com.example.moviehub.Model.*;
import com.example.moviehub.View.AccountFragment;
import com.example.moviehub.View.LoginFragment;
import com.example.moviehub.View.HomepageFragment;
import com.example.moviehub.View.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Definizione delle variabili
    public static User user = new User(null, null, null);
    public static String tema = "Disattivata";
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recupera le informazioni dell'utente dalle SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Utente", MODE_PRIVATE);
        user = new User(
                sharedPreferences.getString("Username", ""),
                sharedPreferences.getString("Password", ""),
                sharedPreferences.getBoolean("Logged", false)
        );

        // Se l'utente è loggato, imposta il tema
        if (user.isLogged()) {
            tema = sharedPreferences.getString("Tema", "Disattivata");
        }

        // Inizializzazione della Bottom Navigation View
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.homepage);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomepageFragment()).commit();
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            // Gestione dei clic sui menu della Bottom Navigation View
            switch (item.getItemId()) {
                case R.id.homepage:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomepageFragment()).commit();
                    return true;

                case R.id.search:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                    return true;

                case R.id.account:
                    if (user.isLogged()) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                    }
                    return true;
            }
            return false;
        });

        // Applica il tema alla Bottom Navigation View
        switch (tema) {
            case "Filtro biancoNero":
                setFiltroBiancoNeroBar();
                break;

            case "Filtro verde":
                SetFiltroDeuteranopiaBar();
                break;

            case "Filtro blu/giallo":
                setFiltroTritanopiaBar();
                break;

            case "Incrementa Dimensioni":
                // Azioni da eseguire se il tema è "Incrementa Dimensioni"
                break;

            case "Filtro biancoNero + Incrementa Dimensioni":
                setFiltroBiancoNeroBar();
                break;

            case "Filtro verde + Incrementa Dimensioni":
                SetFiltroDeuteranopiaBar();
                break;

            case "Filtro blu/giallo + Incrementa Dimensioni":
                setFiltroTritanopiaBar();
                break;

            default:
                // Azioni predefinite se il tema non corrisponde a nessuno dei casi sopra
                break;
        }
    }

    // Imposta il filtro per la Tritanopia sulla Bottom Navigation View
    private void setFiltroTritanopiaBar() {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },
                new int[] { }
        };

        int[] colors = new int[] {
                getColor(R.color.tritanopia_color3),
                getColor(R.color.tritanopia_color4)
        };

        ColorStateList myColorList = new ColorStateList(states, colors);
        bottomNav.setItemIconTintList(myColorList);
        bottomNav.setItemTextColor(myColorList);
    }

    // Imposta il filtro per la Deuteranopia sulla Bottom Navigation View
    private void SetFiltroDeuteranopiaBar() {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },
                new int[] { }
        };

        int[] colors = new int[] {
                getColor(R.color.deuteranopia_color2),
                getColor(R.color.deuteranopia_color3)
        };

        ColorStateList myColorList = new ColorStateList(states, colors);
        bottomNav.setItemIconTintList(myColorList);
        bottomNav.setItemTextColor(myColorList);
    }

    // Imposta il filtro bianco e nero sulla Bottom Navigation View
    private void setFiltroBiancoNeroBar() {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },
                new int[] { }
        };

        int[] colors = new int[] {
                getColor(R.color.biancoNero_colore2),
                getColor(R.color.biancoNero_colore3)
        };

        ColorStateList myColorList = new ColorStateList(states, colors);
        bottomNav.setItemIconTintList(myColorList);
        bottomNav.setItemTextColor(myColorList);
    }
}
