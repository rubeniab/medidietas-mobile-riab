package com.example.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication4.ui.Utilidades.GestorToken;
import com.example.myapplication4.ui.daos.UsuarioDAO;
import com.example.myapplication4.ui.modelos.UsuarioMovil;
import com.example.myapplication4.ui.perfil.ConsultarFragment;
import com.example.myapplication4.ui.perfil.Usuario;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class modificarPerfil extends AppCompatActivity {

    private EditText nombreEdit;
    private EditText nombreUsuarioEdit;
    private EditText correoEdit;
    private EditText apellidoPaternoEdit;
    private EditText apellidoMaternoEdit;


    /*private EditText caloriasEdit;
    private EditText carbohidratosEdit;
    private EditText grasasEdit;
    private EditText proteinasEdit;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_perfil);

        nombreEdit = findViewById(R.id.nombreEdit);
        apellidoPaternoEdit = findViewById(R.id.apellidoPaternoEdit);
        apellidoMaternoEdit = findViewById(R.id.apellidoMaternoEdit);
        nombreUsuarioEdit = findViewById(R.id.nombreusuarioEdit);
        correoEdit = findViewById(R.id.correousuarioEdit);


        String nombre = getIntent().getStringExtra("nombre");
        String apellidoPaterno = getIntent().getStringExtra("apellido_paterno");
        String apellidoMaterno = getIntent().getStringExtra("apellido_materno");
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        String correo = getIntent().getStringExtra("correo");
        /*String calorias = getIntent().getStringExtra("calorias");
        String carbohidratos = getIntent().getStringExtra("carbohidratos");
        String grasas = getIntent().getStringExtra("grasas");
        String proteinas = getIntent().getStringExtra("proteinas");*/


        nombreEdit.setText(nombre);
        apellidoPaternoEdit.setText(apellidoPaterno);
        apellidoMaternoEdit.setText(apellidoMaterno);
        nombreUsuarioEdit.setText(nombreUsuario);
        correoEdit.setText(correo);


        Button btnCancelarModificacion = findViewById(R.id.btnCancelarModificacion);
        btnCancelarModificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(modificarPerfil.this, ConsultarFragment.class);
                startActivity(intent);
            }
        });

        Button btnGuardarModificacion = findViewById(R.id.btnModificarPerfil); // Asegúrate de tener este botón en el XML
        btnGuardarModificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarModificacion(); // Llama a guardar la modificación
            }
        });
    }

    public void actualizarUsuario() {

    }


    public void guardarModificacion() {

        UsuarioMovil usuario = new UsuarioMovil();
        usuario.setNombre(nombreEdit.getText().toString());
        usuario.setNombre_usuario(nombreUsuarioEdit.getText().toString());
        usuario.setCorreo(correoEdit.getText().toString());
        usuario.setApellido_paterno(apellidoPaternoEdit.getText().toString());
        usuario.setApellido_materno(apellidoMaternoEdit.getText().toString());
        // If you want to include calorias, carbohidratos, grasas, and proteinas, add them back as needed:
    /*usuario.setCalorias(Double.parseDouble(caloriasEdit.getText().toString()));
    usuario.setCarbohidratos(Double.parseDouble(carbohidratosEdit.getText().toString()));
    usuario.setGrasas(Double.parseDouble(grasasEdit.getText().toString()));
    usuario.setProteinas(Double.parseDouble(proteinasEdit.getText().toString()));*/

        String token = GestorToken.TOKEN;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nombre", usuario.getNombre());
        jsonObject.addProperty("nombre_usuario", usuario.getNombre_usuario());
        jsonObject.addProperty("correo", usuario.getCorreo());
        jsonObject.addProperty("apellido_paterno", usuario.getApellido_paterno());
        jsonObject.addProperty("apellido_materno", usuario.getApellido_materno());
        // Add other properties as needed
    /*jsonObject.addProperty("calorias", usuario.getCalorias());
    jsonObject.addProperty("carbohidratos", usuario.getCarbohidratos());
    jsonObject.addProperty("grasas", usuario.getGrasas());
    jsonObject.addProperty("proteinas", usuario.getProteinas());*/

        Call<JsonObject> call = ApiService.getService().actualizarUsuario("Bearer " + token, jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(modificarPerfil.this, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show();
                    // Navegar de regreso a la pantalla de consulta
                    Intent intent = new Intent(modificarPerfil.this, ConsultarFragment.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(modificarPerfil.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(modificarPerfil.this, "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}