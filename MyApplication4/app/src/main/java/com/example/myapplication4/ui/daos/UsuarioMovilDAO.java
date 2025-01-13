package com.example.myapplication4.ui.daos;

import android.util.Log;

import com.example.myapplication4.ApiService;
import com.example.myapplication4.LoginActivity;
import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.Utilidades.GestorToken;
import com.example.myapplication4.ui.modelos.UsuarioMovil;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class UsuarioMovilDAO {

    public static HashMap<String, Object> logIn(String correo, String contrasena, LoginActivity context) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            JsonObject json = new JsonObject();
            json.addProperty("correo", correo);
            json.addProperty("contrasena", contrasena);

            Call<JsonObject> call = ApiService.getService().logIn(json);
            Response<JsonObject> response = call.execute();

            if (response.isSuccessful() && response.body() != null && response.body().has("usuario")) {
                JsonObject usuarioJson = response.body().getAsJsonObject("usuario");

                // Guardar datos del usuario en variables globales
                Constantes.NOMBRE_USUARIO = usuarioJson.get("nombre_usuario").getAsString();
                Constantes.CORREO = usuarioJson.get("correo").getAsString();

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
                respuesta.put("mensaje", "Error en la autenticación");
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("LoginError", e.getMessage(), e);
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerUsuarioPorNombre(String nombreUsuario) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            Call<JsonObject> call = ApiService.getService().obtenerUsuarioPorNombre(nombreUsuario);
            Response<JsonObject> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonObject usuarioJson = response.body();

                UsuarioMovil usuarioMovil = new UsuarioMovil();
                usuarioMovil.setId(usuarioJson.get("id").getAsInt()); // Asumiendo que tienes un método setId() en UsuarioMovil
                usuarioMovil.setNombre_usuario(usuarioJson.get("nombre_usuario").getAsString());
                usuarioMovil.setNombre(usuarioJson.get("nombre").getAsString());
                usuarioMovil.setApellido_paterno(usuarioJson.get("apellido_paterno").getAsString());
                usuarioMovil.setApellido_materno(usuarioJson.get("apellido_materno").getAsString());
                usuarioMovil.setContrasena(usuarioJson.get("contrasena").getAsString());
                usuarioMovil.setCorreo(usuarioJson.get("correo").getAsString());
                usuarioMovil.setFecha_nacimiento(usuarioJson.get("fecha_nacimiento").getAsString());
                usuarioMovil.setFoto(usuarioJson.get("foto").isJsonNull() ? null : usuarioJson.get("foto").getAsString());
                usuarioMovil.setEstatura(usuarioJson.get("estatura").getAsDouble());
                usuarioMovil.setPeso(usuarioJson.get("peso").getAsDouble());
                usuarioMovil.setSexo(usuarioJson.get("sexo").getAsBoolean());

                JsonObject objetivoJson = usuarioJson.getAsJsonObject("objetivo");
                usuarioMovil.setCalorias(objetivoJson.get("calorias").getAsDouble());
                usuarioMovil.setCarbohidratos(objetivoJson.get("carbohidratos").getAsDouble());
                usuarioMovil.setGrasas(objetivoJson.get("grasas").getAsDouble());
                usuarioMovil.setProteinas(objetivoJson.get("proteinas").getAsDouble());

                respuesta.put("error", false);
                respuesta.put("objeto", usuarioMovil);
            } else {
                respuesta.put("mensaje", "Error al obtener el usuario");
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("UsuarioError", e.getMessage(), e);
        }
        return respuesta;
    }
}
