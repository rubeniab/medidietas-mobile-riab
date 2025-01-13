package com.example.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.daos.UsuarioMovilDAO;
import com.example.myapplication4.ui.modelos.UsuarioMovil;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configuración de la vista
        TextInputEditText correo = findViewById(R.id.correo);
        TextInputEditText contrasena = findViewById(R.id.contrasena);
        Button btnSesion = findViewById(R.id.btnSesion);

        btnSesion.setOnClickListener(v -> {
            String email = correo.getText() != null ? correo.getText().toString().trim() : "";
            String password = contrasena.getText() != null ? contrasena.getText().toString().trim() : "";

            if (email.isEmpty()) {
                correo.setError("Por favor, ingresa tu correo electrónico.");
                correo.requestFocus();
            } else if (!isValidEmail(email)) {
                correo.setError("Correo electrónico no válido.");
                correo.requestFocus();
            } else if (password.isEmpty()) {
                contrasena.setError("Por favor, ingresa tu contraseña.");
                contrasena.requestFocus();
            } else {
                iniciarSesion(email, password);
            }
        });

        Button btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrarObjetivos.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion(String correo, String contrasena) {
        new Thread(() -> {
            HashMap<String, Object> respuesta = UsuarioMovilDAO.logIn(correo, contrasena, LoginActivity.this);

            runOnUiThread(() -> {
                if ((boolean) respuesta.get("error")) {
                    String mensaje = (String) respuesta.getOrDefault("mensaje", "Error desconocido");
                    Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                } else {
                    obtenerIdUsuario();
                }
            });
        }).start();
    }

    private void obtenerIdUsuario() {
        new Thread(() -> {
            HashMap<String, Object> respuesta = UsuarioMovilDAO.obtenerUsuarioPorNombre(Constantes.NOMBRE_USUARIO);

            runOnUiThread(() -> {
                if ((boolean) respuesta.get("error")) {
                    String mensaje = (String) respuesta.getOrDefault("mensaje", "Error desconocido");
                    Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                } else {
                    UsuarioMovil usuarioMovil = (UsuarioMovil) respuesta.get("objeto");
                    Constantes.ID_USUARIO = usuarioMovil.getId();
                    Log.d(TAG, "ID del usuario: " + Constantes.ID_USUARIO);
                    navigateToNextActivity();
                }
            });
        }).start();
    }

    public void navigateToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}