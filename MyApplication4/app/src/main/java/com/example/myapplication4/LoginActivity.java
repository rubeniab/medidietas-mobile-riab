package com.example.myapplication4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication4.ui.Utilidades.GestorToken;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Verificar si ya existe un token en SharedPreferences
        /*String token = TokenManager.getToken(this);

        if (token != null) {
            Log.d("SharedPreferences", "Token encontrado: " + token);
            // Redirigir al HomeActivity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Finalizar la actividad actual para evitar regresar
            return;
        } else {
            Log.d("SharedPreferences", "No se encontró token. Usuario no autenticado.");
        }*/

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
            HashMap<String, Object> respuesta = logIn(correo, contrasena, LoginActivity.this);

            runOnUiThread(() -> {
                if ((boolean) respuesta.get("error")) {
                    String mensaje = (String) respuesta.getOrDefault("mensaje", "Error desconocido");
                    Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    public static HashMap<String, Object> logIn(String correo, String contrasena, Context context) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            JsonObject json = new JsonObject();
            json.addProperty("correo", correo);
            json.addProperty("contrasena", contrasena);

            Call<JsonObject> call = ApiService.getService().logIn(json);
            retrofit2.Response<JsonObject> response = call.execute();

            if (response.isSuccessful() && response.body().has("usuario")) {
                JsonObject usuarioJson = response.body().getAsJsonObject("usuario");

                respuesta.put("error", false);
                respuesta.put("objeto", usuarioJson);

                String token = response.headers().get("x-token");
                if (token != null) {
                    GestorToken.TOKEN = token;
                    Log.d("TokenLog", "Token recibido: " + token);
                }

                if (context instanceof LoginActivity) {
                    ((LoginActivity) context).navigateToNextActivity();
                }
            } else {
                respuesta.put("mensaje", "Error en la autenticación" );
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("LoginError", e.getMessage(), e);
        }
        return respuesta;
    }

    private void navigateToNextActivity() {
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
