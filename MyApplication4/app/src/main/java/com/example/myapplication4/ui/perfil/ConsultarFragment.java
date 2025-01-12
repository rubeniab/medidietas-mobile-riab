package com.example.myapplication4.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication4.LoginActivity;
import com.example.myapplication4.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConsultarFragment extends Fragment {

    private TextView nombre_UsuarioInfoTextView;

    public ConsultarFragment() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Inicializar vistas
        nombre_UsuarioInfoTextView = rootView.findViewById(R.id.nombre_usuarioInfoTextView);

        // Obtener datos del argumento
        Bundle arguments = getArguments();
        if (arguments != null) {
            String usuarioJsonString = arguments.getString("usuario");
            Log.d("UsuarioJson", "JSON recibido: " + usuarioJsonString);

            if (usuarioJsonString != null) {
                JsonObject usuarioJson = new JsonParser().parse(usuarioJsonString).getAsJsonObject();

                Log.d("UsuarioJson", "Contenido del JSON: " + usuarioJson.toString());

                mostrarInformacionUsuario(usuarioJson);
            } else {
                nombre_UsuarioInfoTextView.setText("No se pudo cargar la información del usuario.");
            }
        }

        return rootView;
    }

    private void mostrarInformacionUsuario(JsonObject usuarioJson) {
        // Verificar si los campos existen y obtener sus valores
        String nombre = usuarioJson.has("nombre") && !usuarioJson.get("nombre").isJsonNull()
                ? usuarioJson.get("nombre").getAsString()
                : "Nombre no disponible";

        String apellidoPaterno = usuarioJson.has("apellido_paterno") && !usuarioJson.get("apellido_paterno").isJsonNull()
                ? usuarioJson.get("apellido_paterno").getAsString()
                : "Apellido paterno no disponible";

        String correo = usuarioJson.has("correo") && !usuarioJson.get("correo").isJsonNull()
                ? usuarioJson.get("correo").getAsString()
                : "Correo no disponible";

        // Ajustar la cadena de información con los datos obtenidos
        String info = "Nombre: " + nombre + "\nApellido: " + apellidoPaterno + "\nCorreo: " + correo;
        nombre_UsuarioInfoTextView.setText(info);
    }

    private void cerrarSesion() {
        // Implementa la lógica para cerrar la sesión aquí
        Toast.makeText(getContext(), "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        // Redirigir al usuario a la pantalla de inicio de sesión (LoginActivity)
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish(); // Cerrar la actividad actual
    }
}
