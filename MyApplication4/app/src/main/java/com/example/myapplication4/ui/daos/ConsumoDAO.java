package com.example.myapplication4.ui.daos;

import android.content.Context;
import android.util.Log;

import com.example.myapplication4.ApiService;
import com.example.myapplication4.ui.Utilidades.GestorToken;
import com.example.myapplication4.ui.modelos.Alimento;
import com.example.myapplication4.ui.modelos.Categoria;
import com.example.myapplication4.ui.modelos.Comida;
import com.example.myapplication4.ui.modelos.Momento;
import com.example.myapplication4.ui.modelos.UnidadMedida;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ConsumoDAO {
    public static HashMap<String, Object> obtenerAlimentos(Context context) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN;
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            Call<JsonArray> call = ApiService.getService().obtenerAlimentos(token);
            Response<JsonArray> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonArray alimentosJson = response.body();
                List<Alimento> alimentos = new ArrayList<>();

                for (int i = 0; i < alimentosJson.size(); i++) {
                    JsonObject jsonObject = alimentosJson.get(i).getAsJsonObject();
                    Alimento alimento = new Alimento(
                            jsonObject.get("id").getAsInt(),
                            jsonObject.get("nombre").getAsString(),
                            jsonObject.get("calorias").getAsInt(),
                            jsonObject.get("carbohidratos").getAsDouble(),
                            jsonObject.get("grasas").getAsDouble(),
                            jsonObject.get("proteinas").getAsDouble(),
                            jsonObject.get("imagen").getAsString(),
                            jsonObject.get("tamano_racion").getAsDouble(),
                            jsonObject.get("estado").getAsBoolean(),
                            jsonObject.get("marca").getAsString(),
                            jsonObject.get("id_categoria").getAsInt(),
                            jsonObject.get("id_unidad_medida").getAsInt()
                    );
                    alimentos.add(alimento);
                }

                respuesta.put("error", false);
                respuesta.put("objeto", alimentos);

            } else {
                String errorBody = response.errorBody() != null ?
                        response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al obtener los alimentos. Código de respuesta: "
                        + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("AlimentoError", e.getMessage(), e);
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerComidas(Context context) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN;
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            Call<JsonArray> call = ApiService.getService().obtenerComidas(token);
            Response<JsonArray> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonArray comidasJson = response.body();
                List<Comida> comidas = new ArrayList<>();

                for (int i = 0; i < comidasJson.size(); i++) {
                    JsonObject jsonObject = comidasJson.get(i).getAsJsonObject();
                    Comida comida = new Comida(
                            jsonObject.get("id").getAsInt(),
                            jsonObject.get("nombre").getAsString(),
                            jsonObject.get("preparacion_video").getAsString(),
                            jsonObject.get("receta").getAsString(),
                            jsonObject.get("estado").getAsBoolean(),
                            jsonObject.get("calorias").getAsDouble(),
                            jsonObject.get("carbohidratos").getAsDouble(),
                            jsonObject.get("grasas").getAsDouble(),
                            jsonObject.get("proteinas").getAsDouble()
                    );
                    comidas.add(comida);
                }

                respuesta.put("error", false);
                respuesta.put("objeto", comidas);

            } else {
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al obtener las comidas. Código de respuesta: " + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("ComidaError", e.getMessage(), e);
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerUnidadesMedida(Context context) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN;
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            Call<JsonArray> call = ApiService.getService().obtenerUnidadesMedida(token);
            Response<JsonArray> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonArray unidadesMedidaJson = response.body();
                List<UnidadMedida> unidadesMedida = new ArrayList<>();

                for (int i = 0; i < unidadesMedidaJson.size(); i++) {
                    JsonObject jsonObject = unidadesMedidaJson.get(i).getAsJsonObject();
                    UnidadMedida unidadMedida = new UnidadMedida(
                            jsonObject.get("id").getAsInt(),
                            jsonObject.get("nombre").getAsString()
                    );
                    unidadesMedida.add(unidadMedida);
                }

                respuesta.put("error", false);
                respuesta.put("objeto", unidadesMedida);

            } else {
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al obtener las unidades de medida. Código de respuesta: " + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("UnidadMedidaError", e.getMessage(), e);
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerCategorias(Context context) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN; // Obtener el token de alguna clase de gestión de tokens
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            Call<JsonArray> call = ApiService.getService().obtenerCategorias(token);
            Response<JsonArray> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonArray categoriasJson = response.body();
                List<Categoria> categorias = new ArrayList<>();

                for (int i = 0; i < categoriasJson.size(); i++) {
                    JsonObject jsonObject = categoriasJson.get(i).getAsJsonObject();
                    Categoria categoria = new Categoria(
                            jsonObject.get("id").getAsInt(),
                            jsonObject.get("nombre").getAsString()
                    );
                    categorias.add(categoria);
                }

                respuesta.put("error", false);
                respuesta.put("objeto", categorias);

            } else {
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al obtener las categorías. Código de respuesta: " + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("CategoriaError", e.getMessage(), e);
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerMomentos(Context context) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN; // Obtener el token de alguna clase de gestión de tokens
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            Call<JsonArray> call = ApiService.getService().obtenerMomentos(token);
            Response<JsonArray> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonArray momentosJson = response.body();
                List<Momento> momentos = new ArrayList<>();

                for (int i = 0; i < momentosJson.size(); i++) {
                    JsonObject jsonObject = momentosJson.get(i).getAsJsonObject();
                    Momento momento = new Momento(
                            jsonObject.get("id").getAsInt(),
                            jsonObject.get("nombre").getAsString()
                    );
                    momentos.add(momento);
                }

                respuesta.put("error", false);
                respuesta.put("objeto", momentos);

            } else {
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al obtener los momentos. Código de respuesta: " + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("MomentoError", e.getMessage(), e);
        }
        return respuesta;
    }

}
