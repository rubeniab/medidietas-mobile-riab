package com.example.myapplication4.ui.daos;

import android.util.Log;

import com.example.myapplication4.ApiService;
import com.example.myapplication4.LoginActivity;
import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.Utilidades.GestorToken;
import com.example.myapplication4.ui.modelos.UsuarioMovil;
import com.example.myapplication4.ui.perfil.Usuario;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class UsuarioDAO {

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
                Constantes.NOMBRE = usuarioJson.get("nombre").getAsString();
                Constantes.APELLIDO_PATERNO = usuarioJson.get("apellido_paterno").getAsString();
                Constantes.APELLIDO_MATERNO = usuarioJson.get("apellido_materno").getAsString();
                Constantes.NOMBRE_USUARIO = usuarioJson.get("nombre_usuario").getAsString();
                Constantes.CORREO = usuarioJson.get("correo").getAsString();

                if (usuarioJson.has("objetivo")) {
                    JsonObject objetivoJson = usuarioJson.getAsJsonObject("objetivo");

                    Log.d("UsuarioDAO", "Contenido de 'objetivo': " + objetivoJson.toString());

                    // Asignar calorías y otros valores
                    try {
                        Constantes.CALORIAS = objetivoJson.get("calorias").getAsDouble();
                        Constantes.CARBOHIDRATOS = objetivoJson.get("carbohidratos").getAsDouble();
                        Constantes.GRASAS = objetivoJson.get("grasas").getAsDouble();
                        Constantes.PROTEINAS = objetivoJson.get("proteinas").getAsDouble();

                        Log.d("UsuarioDAO", "Calorías: " + Constantes.CALORIAS);
                        Log.d("UsuarioDAO", "Carbohidratos: " + Constantes.CARBOHIDRATOS);
                        Log.d("UsuarioDAO", "Grasas: " + Constantes.GRASAS);
                        Log.d("UsuarioDAO", "Proteínas: " + Constantes.PROTEINAS);

                    } catch (Exception e) {
                        Log.e("UsuarioDAO", "Error al procesar datos de 'objetivo': " + e.getMessage());
                    }
                } else {
                    Log.d("UsuarioDAO", "'objetivo' no está presente en el JSON.");
                }


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
    /*public static HashMap<String, Object> actualizarUsuario(String nombreUsuario, UsuarioMovil usuario) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);

        try {
            // Obtener el token
            String token = GestorToken.TOKEN;
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            // Realizar la llamada PUT
            Call<JsonObject> call = ApiService.getService().actualizarUsuario(nombreUsuario, token, usuario);
            Response<JsonObject> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonObject usuarioJson = response.body();
                respuesta.put("error", false);
                respuesta.put("objeto", usuarioJson);
            } else {
                respuesta.put("mensaje", "Error al actualizar el usuario");
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
        }
        return respuesta;
    }*/
}
