package com.example.myapplication4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.daos.UsuarioDAO;
import com.example.myapplication4.ui.modelos.UsuarioMovil;

import com.example.myapplication4.ui.perfil.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

<<<<<<< Updated upstream
=======

        // Configuración de la vista
>>>>>>> Stashed changes
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

    private void mostrarProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);  // Evitar que se cierre accidentalmente
        progressDialog.show();
    }
    private void ocultarProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void iniciarSesion(String correo, String contrasena) {
        mostrarProgressDialog();

        new Thread(() -> {
<<<<<<< Updated upstream
            HashMap<String, Object> respuesta = UsuarioDAO.logIn(correo, contrasena, LoginActivity.this);
=======
            HashMap<String, Object> respuesta;
            try {
                respuesta = UsuarioDAO.logIn(correo, contrasena, LoginActivity.this);
            } catch (Exception e) {
                Log.e(TAG, "Error al conectar con la API: ", e);
                respuesta = new HashMap<>();
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo conectar con el servidor. Por favor, verifica tu conexión.");
            }
>>>>>>> Stashed changes

            HashMap<String, Object> finalRespuesta = respuesta;
            runOnUiThread(() -> {
<<<<<<< Updated upstream
                ocultarProgressDialog();

                if ((boolean) respuesta.get("error")) {
                    String mensaje = (String) respuesta.getOrDefault("mensaje", "Error desconocido");
                    Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
=======
                if ((boolean) finalRespuesta.get("error")) {
                    actualizarProgressDialogMensaje("No se pudo conectar con el servidor. Verifica tu conexión.");
                    // Mantener el ProgressDialog visible unos segundos antes de ocultarlo
                    new android.os.Handler().postDelayed(this::ocultarProgressDialog, 3000);
>>>>>>> Stashed changes
                } else {
                    ocultarProgressDialog();
                    obtenerIdUsuario();
                }
            });
        }).start();
    }

    private void obtenerIdUsuario() {
        mostrarProgressDialog();
        new Thread(() -> {
            HashMap<String, Object> respuesta;
            try {
                respuesta = UsuarioDAO.consultarUsuario(Constantes.NOMBRE_USUARIO);
            } catch (Exception e) {
                Log.e(TAG, "Error al obtener datos del usuario: ", e);
                respuesta = new HashMap<>();
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se pudo conectar con el servidor. Por favor, intenta nuevamente.");
            }

            HashMap<String, Object> finalRespuesta = respuesta;
            runOnUiThread(() -> {
                if ((boolean) finalRespuesta.get("error")) {
                    actualizarProgressDialogMensaje("No se pudo conectar con el servidor. Intenta nuevamente.");
                    // Mantener el ProgressDialog visible unos segundos antes de ocultarlo
                    new android.os.Handler().postDelayed(this::ocultarProgressDialog, 3000);
                } else {
                    ocultarProgressDialog();
                    Usuario usuario = (Usuario) finalRespuesta.get("objeto");
                    if (usuario != null) {
                        Constantes.ID_USUARIO = usuario.getId();
                        Log.d(TAG, "ID del usuario: " + Constantes.ID_USUARIO);
                    } else {
                        Log.e(TAG, "El objeto usuario es null");
                    }
                    navigateToNextActivity();
                }
            });
        }).start();
    }
    private void actualizarProgressDialogMensaje(String mensaje) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(mensaje);
        }
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