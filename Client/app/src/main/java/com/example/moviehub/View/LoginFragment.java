package com.example.moviehub.View;

import static android.content.Context.MODE_PRIVATE;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Executor;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviehub.MainActivity;
import com.example.moviehub.R;

public class LoginFragment extends Fragment {

    // Dichiarazione delle variabili
    private EditText username, password;
    private TextView registratiLbl, oppureLbl;
    private CheckBox ricordaUsername;
    private ImageView biometricBtn;
    private Button loginBtn;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    // Costruttore pubblico vuoto
    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate del layout per questo frammento
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inizializzazione delle view
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        registratiLbl = view.findViewById(R.id.registratiLbl);
        oppureLbl = view.findViewById(R.id.oppureLbl);
        ricordaUsername = view.findViewById(R.id.ricordaUsername);
        biometricBtn = view.findViewById(R.id.fingerprint);
        loginBtn = view.findViewById(R.id.logoutBtn);

        // Recupera le credenziali salvate se il checkbox è selezionato
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Utente", MODE_PRIVATE);
        if (ricordaUsername.isChecked()) {
            username.setText(sharedPreferences.getString("Username", ""));
        }

        // Verifica il supporto al riconoscimento biometrico
        checkBioMetricSupported();

        // Imposta il listener per il clic su "Registrati"
        registratiLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Imposta il listener per il clic sul pulsante di login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Alcuni campi sono vuoti", Toast.LENGTH_SHORT).show();
                } else {
                    // Esegue il login con le credenziali inserite
                    LoginUser loginUser = new LoginUser(username.getText().toString(), password.getText().toString(), 0, getContext());
                    loginUser.execute();
                }
            }
        });

        // Inizializza l'executor e il BiometricPrompt per il riconoscimento biometrico
        executor = ContextCompat.getMainExecutor(this.getContext());
        biometricPrompt = new BiometricPrompt((FragmentActivity) this.getContext(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getContext(), "Errore durante autenticazione: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                // Esegue il login utilizzando il riconoscimento biometrico
                LoginUser loginUser = new LoginUser(username.getText().toString(), password.getText().toString(), 1, getContext());
                loginUser.execute();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Tentativo non riconosciuto di autenticazione biometrica
                Toast.makeText(getContext(), "Autenticazione fallita", Toast.LENGTH_SHORT).show();
            }
        });

        // Configura le informazioni di prompt per il riconoscimento biometrico
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Accedi a MovieHub")
                .setSubtitle("Aggiorna le impostazioni di sicurezza del tuo dispositivo per accedere")
                .setNegativeButtonText("Annulla")
                .build();

        // Imposta il listener per il clic sul pulsante di riconoscimento biometrico
        biometricBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        return  view;
    }

    // Verifica se il riconoscimento biometrico è supportato sul dispositivo
    void checkBioMetricSupported() {
        BiometricManager manager = BiometricManager.from(getContext());

        switch (manager.canAuthenticate(BIOMETRIC_WEAK | BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // L'app può autenticarsi utilizzando la biometria
                enableButton(true);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // Nessuna funzione biometrica disponibile su questo dispositivo
                enableButton(false);
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                // Le funzioni biometriche non sono attualmente disponibili
                enableButton(false);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // È necessario registrare almeno un'impronta digitale
                enableButton(false);
                break;

            default:
                // Causa sconosciuta
                enableButton(false);
        }
    }

    // Abilita o disabilita il pulsante biometrico in base al supporto
    void enableButton(boolean enable) {
        if (enable) {
            oppureLbl.setVisibility(View.VISIBLE);
            biometricBtn.setVisibility(View.VISIBLE);
        } else {
            oppureLbl.setVisibility(View.INVISIBLE);
            biometricBtn.setVisibility(View.INVISIBLE);
        }
    }

    // AsyncTask per gestire il login
    class LoginUser extends AsyncTask<Void, Void, String> {
        private static final String SERVER_IP = "172.23.241.15";
        private static final int SERVER_PORT = 8080;
        private InputStream in;
        private BufferedWriter out;
        private Socket socket;
        private String username, password;
        private Context context;
        private int flag = 0;
        private String response = "";
        private ProgressDialog pdLoading;

        // Costruttore
        LoginUser(String username, String password, int flag, Context context) {
            this.username = username;
            this.password = password;
            this.flag = flag;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(context);
            pdLoading.setMessage("\tSto effettuando il login...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);

                in = socket.getInputStream();
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                if (flag == 0) {
                    sendDataToServer("Login;" + username + ";" + password + ";");
                } else {
                    sendDataToServer("LoginFinger;" + username + ";null;");
                }

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
            pdLoading.dismiss();

            // Gestisce la risposta dal server dopo il login
            if (!result.equals("Errore")) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Utente", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Username", username);
                editor.putString("Password", password);
                editor.putBoolean("Logged", true);
                editor.putBoolean("Check", ricordaUsername.isChecked());
                editor.putString("Tema", result);
                editor.apply();

                Toast.makeText(context, "Login avvenuto con successo", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(context, "Si è verificato un errore durante il login", Toast.LENGTH_SHORT).show();
            }
        }

        // Invia i dati al server
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
