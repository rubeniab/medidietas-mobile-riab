package com.example.myapplication4.ui.daos;

<<<<<<< Updated upstream
=======
import static androidx.core.content.ContextCompat.startActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
>>>>>>> Stashed changes
import android.util.Log;

import com.example.myapplication4.ui.Utilidades.ApiService;
import com.example.myapplication4.LoginActivity;
import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.Utilidades.GestorToken;
import com.example.myapplication4.ui.modelos.UsuarioMovil;
import com.example.myapplication4.RegistrarObjetivos;
import com.example.myapplication4.ui.perfil.Usuario;
import com.google.gson.JsonObject;

import java.util.Base64;
import java.util.HashMap;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;
import android.app.ProgressDialog;
import android.content.Intent;

import android.widget.Toast;


import com.example.myapplication4.ui.modelos.RegistroUsuario;

public class UsuarioDAO {
    private ProgressDialog progressDialog;

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


                Constantes.NOMBRE = usuarioJson.get("nombre").getAsString();
                Constantes.APELLIDO_PATERNO = usuarioJson.get("apellido_paterno").getAsString();
                Constantes.APELLIDO_MATERNO = usuarioJson.get("apellido_materno").getAsString();
                Constantes.NOMBRE_USUARIO = usuarioJson.get("nombre_usuario").getAsString();
                Constantes.CORREO = usuarioJson.get("correo").getAsString();
                Constantes.FOTO = usuarioJson.get("foto").getAsString();

                if (usuarioJson.has("objetivo")) {
                    JsonObject objetivoJson = usuarioJson.getAsJsonObject("objetivo");

                    Log.d("UsuarioDAO", "Contenido de 'objetivo': " + objetivoJson.toString());

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
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

    public static void registrarUsuario(RegistroUsuario registroUsuario, RegistrarObjetivos context) {
        ApiService.Service apiService = ApiService.getService();
        Call<Void> call = apiService.registrarUsuario(registroUsuario);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                    context.finish();
                } else {
                    Toast.makeText(context, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

<<<<<<< Updated upstream
    public static void actualizarUsuario(UsuarioMovil usuario, String token, final Callback<JsonObject> callback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nombre", usuario.getNombre());
        jsonObject.addProperty("nombre_usuario", usuario.getNombre_usuario());
        jsonObject.addProperty("correo", usuario.getCorreo());
        jsonObject.addProperty("apellido_paterno", usuario.getApellido_paterno());
        jsonObject.addProperty("apellido_materno", usuario.getApellido_materno());

        Log.d("REQUEST_DATA", "Datos enviados: " + jsonObject.toString());
        Log.d("Token", "Token enviado: " + token);

        Call<JsonObject> call = ApiService.getService().actualizarUsuario(usuario.getNombre_usuario(), "Bearer " + token, jsonObject);
        call.enqueue(callback);
    }


    /*public static HashMap<String, Object> actualizarUsuario(String nombreUsuario, UsuarioMovil usuario) {
=======
    public static String guardarFotoPerfil(String nombreUsuario, String extension, String imagen) throws Exception {
        ProfileImageServiceGrpc.ProfileImageServiceBlockingStub stub =
                ProfileImageServiceGrpc.newBlockingStub(ManagedChannelBuilder.
                        forAddress(Constantes.IP, Constantes.PUERTO_API_GRPC_DOCKER).usePlaintext().build());

        ImageService.UploadImageRequest solicitud = ImageService.UploadImageRequest.newBuilder()
                .setName(nombreUsuario)
                .setExtension(extension)
                .setImageData(imagen)
                .build();

        ImageService.UploadFileResponse respuesta = stub.uploadProfileImage(solicitud);

        System.out.println(respuesta.getImageName());
        return respuesta.getImageName();
    }

    public static HashMap<String, Object> modificarPerfil(Context context, String nombreUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String correo) {
>>>>>>> Stashed changes
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN;
            Log.d("modificarPerfil", "Token:" + token);
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            JsonObject usuarioJson = new JsonObject();
            usuarioJson.addProperty("nombre_usuario", nombreUsuario);
            usuarioJson.addProperty("nombre", nombre);
            usuarioJson.addProperty("apellido_paterno", apellidoPaterno);
            usuarioJson.addProperty("apellido_materno", apellidoMaterno);
            usuarioJson.addProperty("correo", correo);

            //Llamar al servicio API para modificar perfil
            Call<JsonObject> call = ApiService.getService().actualizarUsuario(nombreUsuario, token, usuarioJson);
            Response<JsonObject> response = call.execute();

            Log.d("modificarPerfil", "Código de respuesta: " + response.code());
            Log.d("modificarPerfil", "Cuerpo de respuesta: " + (response.body() != null ? response.body().toString() : "null"));

            if (response.isSuccessful() && response.body() != null) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "Perfil modificado correctamente");
            } else {
                String errorBody = response.errorBody() != null
                        ? response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al modificar el perfil. " +
                        "Código de respuesta: " + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("PerfilError", e.getMessage(), e);
        }
        return respuesta;
    }




}
