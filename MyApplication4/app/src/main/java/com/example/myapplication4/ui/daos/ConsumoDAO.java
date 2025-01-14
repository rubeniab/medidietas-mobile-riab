package com.example.myapplication4.ui.daos;

import android.content.Context;
import android.util.Log;

import com.example.myapplication4.ui.Utilidades.ApiService;
import com.example.myapplication4.ui.Utilidades.GestorToken;
import com.example.myapplication4.ui.modelos.Alimento;
import com.example.myapplication4.ui.modelos.Categoria;
import com.example.myapplication4.ui.modelos.Comida;
import com.example.myapplication4.ui.modelos.Consumo;
import com.example.myapplication4.ui.modelos.ConsumoDiario;
import com.example.myapplication4.ui.modelos.Momento;
import com.example.myapplication4.ui.modelos.UnidadMedida;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
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

    public static HashMap<String, Object> registrarConsumo(Context context, Consumo consumo, boolean esComida) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN; // Obtener el token de alguna clase de gestión de tokens
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            // Crear el objeto JSON para el consumo
            JsonObject consumoJson = new JsonObject();
            consumoJson.addProperty("fecha", consumo.getFecha());
            consumoJson.addProperty("cantidad", consumo.getCantidad());
            consumoJson.addProperty("id_momento", consumo.getIdMomento());
            consumoJson.addProperty("id_usuario_movil", consumo.getIdUsuarioMovil());

            // Diferenciar entre alimento y comida
            if (esComida) {
                consumoJson.addProperty("id_comida", consumo.getIdConsumo());
            } else {
                consumoJson.addProperty("id_alimento", consumo.getIdConsumo());
            }

            // Llamar al servicio API para registrar el consumo
            Call<JsonObject> call = ApiService.getService().registrarConsumo(token, consumoJson);
            Response<JsonObject> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "Consumo registrado correctamente");
            } else {
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al registrar el consumo. Código de respuesta: " + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("ConsumoError", e.getMessage(), e);
        }
        return respuesta;
    }

    public static HashMap<String, Object> consultarConsumosHoy(String nombreUsuario, String fecha) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN;
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            Call<JsonArray> call = ApiService.getService().obtenerConsumos(nombreUsuario, fecha, token);
            Response<JsonArray> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonArray consumosJson = response.body();
                List<ConsumoDiario> consumosDia = new ArrayList<>();
                for (int i = 0; i < consumosJson.size(); i++) {
                    JsonObject jsonObject = (JsonObject) consumosJson.get(i);

                    int id = jsonObject.get("id").getAsInt();
                    String nombre = jsonObject.get("nombre").getAsString();
                    String tamanoRacion = jsonObject.get("tamano_racion").getAsString();
                    double calorias = jsonObject.get("calorias").getAsDouble();
                    double carbohidratos = jsonObject.get("carbohidratos").getAsDouble();
                    double grasas = jsonObject.get("grasas").getAsDouble();
                    double proteinas = jsonObject.get("proteinas").getAsDouble();
                    double cantidad = jsonObject.get("cantidad").getAsDouble();
                    String momento = jsonObject.get("momento").getAsString();

                    ConsumoDiario consumo = new ConsumoDiario(id, nombre, tamanoRacion, calorias, carbohidratos, grasas, proteinas, cantidad, momento);
                    consumosDia.add(consumo);
                }

                respuesta.put("error", false);
                respuesta.put("objeto", consumosDia);

            } else {
                String errorBody = response.errorBody() != null ?
                        response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al obtener los consumos. Código de respuesta: "
                        + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("ConsumoError", e.getMessage(), e);
        }
        return respuesta;
    }

    public static HashMap<String, Object> modificarConsumo(Context context, int idConsumo, double cantidad, int idMomento) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN;
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            JsonObject consumoJson = new JsonObject();
            consumoJson.addProperty("cantidad", cantidad);
            consumoJson.addProperty("id_momento", idMomento);

            // Llamar al servicio API para modificar el consumo
            Call<JsonObject> call = ApiService.getService().modificarConsumo(token, idConsumo, consumoJson);
            Response<JsonObject> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "Consumo modificado correctamente");
            } else {
                String errorBody = response.errorBody() != null
                        ? response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al modificar el consumo. " +
                        "Código de respuesta: " + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("ConsumoError", e.getMessage(), e);
        }
        return respuesta;
    }

    public static HashMap<String, Object> eliminarConsumo(Context context, int idConsumo) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        try {
            String token = GestorToken.TOKEN;
            if (token == null || token.isEmpty()) {
                throw new Exception("Token no válido");
            }

            Call<ResponseBody> call = ApiService.getService().eliminarConsumo(token, idConsumo);
            Response<ResponseBody> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                respuesta.put("error", false);
                respuesta.put("mensaje", "Consumo eliminado correctamente");
            } else {
                String errorBody = response.errorBody() != null
                        ? response.errorBody().string() : "Cuerpo de error vacío";
                respuesta.put("mensaje", "Error al eliminar el consumo. " +
                        "Código de respuesta: " + response.code() + ", Cuerpo de error: " + errorBody);
            }
        } catch (Exception e) {
            respuesta.put("mensaje", "Error: " + e.getMessage());
            Log.e("ConsumoError", e.getMessage(), e);
        }
        return respuesta;
    }
}
