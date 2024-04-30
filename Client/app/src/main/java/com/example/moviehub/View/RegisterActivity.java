package com.example.moviehub.View;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviehub.Adapters.SpinnerAdapter;
import com.example.moviehub.MainActivity;
import com.example.moviehub.R;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText username, password;
    private Button registraBtn,accettaBtn;
    private ImageButton backBtn;
    private Spinner impostaFiltro;
    private SpinnerAdapter spinnerAdapter;
    private TextView terms;
    private CheckBox checkBoxTerms;
    private String filtro = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inizializza le view
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registraBtn = findViewById(R.id.registraBtn);
        backBtn = findViewById(R.id.backBtn);
        impostaFiltro = findViewById(R.id.spinner);
        impostaFiltro.setOnItemSelectedListener(this);
        checkBoxTerms = findViewById(R.id.checkboxTerms);
        terms = findViewById(R.id.linkTerms);


        // Inizializza lo spinner per le impostazioni del filtro
        spinnerAdapter = new SpinnerAdapter(this);
        impostaFiltro.setAdapter(spinnerAdapter);

        // Imposta il listener per il pulsante di ritorno
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Imposta il listener per il testo di termini&condizioni
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupTerms();
            }
        });


        // Imposta il listener per il pulsante di registrazione
        registraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                // Verifica che la password sia una combinazione di almeno 4 caratteri e almeno 1 numero
                Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{4,}$");
                Matcher matcher = pattern.matcher(enteredPassword);

                if (!matcher.matches()) {
                    Toast.makeText(RegisterActivity.this, "La password deve essere una combinazione di caratteri e numeri", Toast.LENGTH_SHORT).show();
                } else if (enteredUsername.length() < 4 || enteredPassword.length() < 4) {
                    Toast.makeText(RegisterActivity.this, "Username e password devono essere almeno di 4 caratteri", Toast.LENGTH_SHORT).show();
                } else if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Alcuni campi sono vuoti", Toast.LENGTH_SHORT).show();
                } else if (!checkBoxTerms.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "Devi accettare i termini e le condizioni", Toast.LENGTH_SHORT).show();
                } else {
                    // Esegue la registrazione dell'utente
                    RegisterUser registerUser = new RegisterUser(username.getText().toString(), password.getText().toString(), filtro);
                    registerUser.execute();

                    // Salva le informazioni dell'utente nelle SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("Utente", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Username", username.getText().toString());
                    editor.putString("Password", password.getText().toString());
                    editor.putBoolean("Logged", true);
                    editor.putString("Tema", filtro);
                    editor.apply();

                    // Mostra un messaggio di registrazione avvenuta con successo
                    Toast.makeText(RegisterActivity.this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();

                    // Avvia l'activity principale
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Ottiene l'elemento selezionato nello spinner delle impostazioni del filtro
        filtro = spinnerAdapter.getItem(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Nessuna azione richiesta quando non viene selezionato nulla nello spinner
    }

    // Metodo per mostrare i termini&condizioni
    private void showPopupTerms() {
        Dialog popup = new Dialog(RegisterActivity.this);
        popup.setContentView(R.layout.popup_terms);
        accettaBtn = popup.findViewById(R.id.ok);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.show();
        accettaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

    // AsyncTask per registrare l'utente
    class RegisterUser extends AsyncTask<Void, Void, String> {
        private static final String SERVER_IP = "172.23.241.15";
        private static final int SERVER_PORT = 8080;
        private InputStream in;
        private BufferedWriter out;
        private Socket socket;
        private String username, password, tema;
        private String response = "";
        private ProgressDialog pdLoading;

        RegisterUser(String username, String password, String tema) {
            this.username = username;
            this.password = password;
            this.tema = tema;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Mostra un ProgressDialog durante la registrazione
            pdLoading = new ProgressDialog(RegisterActivity.this);
            pdLoading.setMessage("\tSto registrando il nuovo utente...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                // Connessione al server
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);

                // Inizializza gli stream di input e output
                in = socket.getInputStream();
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                // Invia i dati al server per la registrazione
                sendDataToServer("Registrazione;" + username + ";" + password + ";" + tema);

                // Legge la risposta dal server
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;

                if ((bytesRead = in.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response = byteArrayOutputStream.toString("UTF-8");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // Chiude la connessione e gli stream
                    socket.close();
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Chiude il ProgressDialog dopo la registrazione
            pdLoading.dismiss();

            // Gestisce la risposta dal server
            if (result.equals("OK")) {
                // Se la registrazione ha successo, salva le informazioni dell'utente nelle SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("Utente", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Username", username);
                editor.putString("Password", password);
                editor.putBoolean("Logged", true);
                editor.putString("Tema", tema);
                editor.apply();

                // Mostra un messaggio di registrazione avvenuta con successo
                Toast.makeText(RegisterActivity.this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();

                // Avvia l'activity principale
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // Altrimenti, mostra un messaggio di errore
                Toast.makeText(RegisterActivity.this, "Si è verificato un errore durante la registrazione", Toast.LENGTH_SHORT).show();
            }
        }

        // Metodo per inviare i dati al server
        public void sendDataToServer(final String data) {
            try {
                out.write(data);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
