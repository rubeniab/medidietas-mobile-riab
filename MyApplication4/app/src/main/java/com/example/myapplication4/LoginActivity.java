package com.example.myapplication4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import retrofit2.Call;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextInputEditText correo = findViewById(R.id.correo);
        TextInputEditText contrasena = findViewById(R.id.contrasena);
        Button btnSesion = findViewById(R.id.btnSesion);


        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userText = correo.getText() != null ? correo.getText().toString().trim() : "";
                String passwordText = contrasena.getText() != null ? contrasena.getText().toString().trim() : "";


                if (userText.isEmpty()) {
                    correo.setError("Por favor, ingresa tu nombre de usuario.");
                    correo.requestFocus();
                } else if (!isValidEmail(userText)) {
                    correo.setError("Correo electrónico no válido.");
                    correo.requestFocus();
                } else if (passwordText.isEmpty()) {
                    contrasena.setError("Por favor, ingresa tu contraseña.");
                    contrasena.requestFocus();
                } else {
                    login(userText, passwordText);
                }
            }
        });

        Button btnRegistro = findViewById(R.id.btnRegistro);
        Button btnObjetivo = findViewById(R.id.btnObjetivos);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this, registrarUsuarioActivity.class); // Cambia registrarUsuarioActivity con tu actividad de destino
                startActivity(intent2);
            }
        });

        btnObjetivo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent inten10 = new Intent(LoginActivity.this, RegistrarObjetivos.class);
                startActivity(inten10);
            }
        });
    }

    private void login(String correo, String contrasena){
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(correo, contrasena);

        apiService.login(loginRequest).enqueue(new retrofit2.Callback<LoginResponse>(){
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response){
                if(response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    // Obtener el token de la respuesta
                    String token = loginResponse.getToken();  // Suponiendo que LoginResponse tiene un método getToken()

                    // Guardar el token en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);  // Guarda el token
                    editor.apply();  // Guarda los cambios

                    // Mostrar un mensaje de bienvenida
                    Toast.makeText(LoginActivity.this, "Bienvenido, " + loginResponse.getUsuario().nombre_usuario, Toast.LENGTH_LONG).show();

                    // Redirigir al HomeActivity
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t){
                Toast.makeText(LoginActivity.this, "Error de conexión " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
