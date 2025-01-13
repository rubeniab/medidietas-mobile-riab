package com.example.myapplication4.ui.daos;

import com.example.myapplication4.ApiService;
import com.example.myapplication4.ui.Utilidades.GestorToken;
import com.example.myapplication4.ui.perfil.Usuario;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class UsuarioDAO {
    public static HashMap<String, Object> consultarUsuario(String nombreUsuario) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);

        try {
            String token = GestorToken.TOKEN;
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            Call<JsonObject> call = ApiService.getService().obtenerUsuarioPorNombre(nombreUsuario, token);
            Response<JsonObject> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonObject usuarioJson = response.body();
                JsonObject objetivoJson = usuarioJson.getAsJsonObject("objetivo");
                Usuario usuario = new Usuario(
                        usuarioJson.get("id").getAsInt(),
                        usuarioJson.get("nombre_usuario").getAsString(),
                        usuarioJson.get("nombre").getAsString(),
                        usuarioJson.get("apellido_paterno").getAsString(),
                        usuarioJson.get("apellido_materno").getAsString(),
                        usuarioJson.get("contrasena").getAsString(),
                        usuarioJson.get("correo").getAsString(),
                        usuarioJson.get("fecha_nacimiento").getAsString(),
                        usuarioJson.get("foto").getAsString(),
                        usuarioJson.get("estatura").getAsDouble(),
                        usuarioJson.get("peso").getAsDouble(),
                        usuarioJson.get("sexo").getAsBoolean(),
                        usuarioJson.get("id_objetivo").getAsInt(),
                        objetivoJson.get("calorias").getAsDouble(),
                        objetivoJson.get("carbohidratos").getAsDouble(),
                        objetivoJson.get("grasas").getAsDouble(),
                        objetivoJson.get("proteinas").getAsDouble()
                );

                respuesta.put("error", false);
                respuesta.put("objeto", usuario);
            } else {
                respuesta.put("mensaje", "Error al consultar el usuario");
            }
        } catch (Exception e) {
            respuesta.put("mensaje", e.getMessage());
            e.printStackTrace();
            return respuesta;
        }
        return respuesta;
    }
}
