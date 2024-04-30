package com.example.moviehub.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviehub.Model.User;
import com.example.moviehub.MainActivity;
import com.example.moviehub.R;


public class AccountFragment extends Fragment {

    private Button logoutBtn;
    private ImageView accountFoto;
    private TextView accountTitolo, username, impostazioniLbl, impostazioniSchermo;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        accountTitolo = view.findViewById(R.id.accountTitolo);

        username = view.findViewById(R.id.username);
        username.setText(MainActivity.user.getUsername());

        impostazioniSchermo = view.findViewById(R.id.impostazioni);
        impostazioniSchermo.setText(MainActivity.tema);

        accountFoto = view.findViewById(R.id.accountPh);

        impostazioniLbl = view.findViewById(R.id.impostazioniTitolo);

        logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.user = new User(null, null, null);
                MainActivity.tema = "Disattivata";

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Utente", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("Logged", false);
                editor.commit();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        switch (MainActivity.tema) {
            case "Disattivata":
                setFiltroDefault();
                break;

            case "Filtro biancoNero":
                setFiltroDefault();
                setFiltroBiancoNero();
                accountFoto.setImageResource(R.drawable.ic_account_rabbit);
                break;

            case "Filtro verde":
                setFiltroDefault();
                setFiltroDeuteranopia();
                accountFoto.setImageResource(R.drawable.ic_account_dog);
                break;

            case "Filtro blu/giallo":
                setFiltroDefault();
                setFiltroTritanopia();
                accountFoto.setImageResource(R.drawable.ic_account_bear);
                break;

            case "Incrementa Dimensioni":
                increaseSize();
                break;

            case "Filtro biancoNero + Incrementa Dimensioni":
                setFiltroDefault();
                setFiltroBiancoNero();
                increaseSize();
                accountFoto.setImageResource(R.drawable.ic_account_rabbit);
                break;

            case "Filtro verde + Incrementa Dimensioni":
                setFiltroDefault();
                setFiltroDeuteranopia();
                increaseSize();
                accountFoto.setImageResource(R.drawable.ic_account_dog);
                break;

            case "Filtro blu/giallo + Incrementa Dimensioni":
                setFiltroDefault();
                setFiltroTritanopia();
                increaseSize();
                accountFoto.setImageResource(R.drawable.ic_account_bear);
                break;
        }

        return  view;
    }

    private void setFiltroTritanopia() {
        username.setTextColor(getResources().getColor(R.color.tritanopia_color2));
        impostazioniLbl.setTextColor(getResources().getColor(R.color.tritanopia_color2));
        logoutBtn.setBackgroundColor(getResources().getColor(R.color.tritanopia_color3));
        logoutBtn.setTextColor(getResources().getColor(R.color.white));
    }

    private void setFiltroDeuteranopia() {
        username.setTextColor(getResources().getColor(R.color.deuteranopia_color1));
        impostazioniLbl.setTextColor(getResources().getColor(R.color.deuteranopia_color1));
        logoutBtn.setBackgroundColor(getResources().getColor(R.color.deuteranopia_color2));
        logoutBtn.setTextColor(getResources().getColor(R.color.white));
    }

    private void setFiltroBiancoNero() {
        username.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
        impostazioniLbl.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
        logoutBtn.setBackgroundColor(getResources().getColor(R.color.biancoNero_colore4));
        logoutBtn.setTextColor(getResources().getColor(R.color.biancoNero_colore1));
    }

    private void increaseSize() {
        username.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_large));
        impostazioniLbl.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_large));
        impostazioniSchermo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_large));
        logoutBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_large));
    }

    private void setFiltroDefault() {
        username.setTextColor(getResources().getColor(R.color.black));
        impostazioniLbl.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_dimen));
        logoutBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.label_dimen));
        logoutBtn.setBackgroundColor(getResources().getColor(R.color.white));
        logoutBtn.setTextColor(getResources().getColor(R.color.black));
    }
}