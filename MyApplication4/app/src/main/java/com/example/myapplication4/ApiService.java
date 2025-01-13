package com.example.myapplication4;

import androidx.annotation.OptIn;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.example.myapplication4.ui.perfil.Usuario;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ApiService {

    private static final String BASE_URL = "http://192.168.0.253:8081/";

    // Interfaz para las llamadas a la API
    public interface Service {
        @Headers("Content-Type: application/json")

        @POST("api/medidietas/usuarios/login")
        Call<JsonObject> logIn(@Body JsonObject body);

        @POST("api/medidietas/usuarios/logout")
        Call<ResponseBody> logOut(@Header("Authorization") String token);

        @POST("api/medidietas/usuarios/signup")
        Call<Void> registrarUsuario(@Body RegistroUsuario registroUsuario);

        @GET("api/medidietas/usuarios/{nombre_usuario}")
        Call<JsonObject> obtenerUsuarioPorNombre(@Path("nombre_usuario") String nombreUsuario, @Header("x-token") String token);

        @GET("api/medidietas/alimentos")
        Call<JsonArray> obtenerAlimentos(@Header("x-token") String token);

        @GET("api/medidietas/comidas")
        Call<JsonArray> obtenerComidas(@Header("x-token") String token);

        @GET("api/medidietas/alimentos/unidades-medida")
        Call<JsonArray> obtenerUnidadesMedida(@Header("x-token") String token);

        @GET("api/medidietas/alimentos/categorias")
        Call<JsonArray> obtenerCategorias(@Header("x-token") String token);

        @GET("api/medidietas/momentos")
        Call<JsonArray> obtenerMomentos(@Header("x-token") String token);

        @GET("api/medidietas/consumos/{nombre_usuario}/{fecha}")
        Call<JsonArray> obtenerConsumos(@Path("nombre_usuario") String nombreUsuario, @Path("fecha") String fecha,@Header("x-token") String token);

        @POST("api/medidietas/consumos")
        Call<JsonObject> registrarConsumo(@Header("x-token") String token, @Body JsonObject consumo);

    }

    // Singleton para Retrofit
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Crear una instancia de la interfaz
    public static Service getService() {
        return retrofit.create(Service.class);
    }

    // Método auxiliar para los logs
    @OptIn(markerClass = UnstableApi.class)
    public static void logApiResponse(int responseCode, String responseBody) {
        Log.d("API Response", "Response code: " + responseCode);
        Log.d("API Response", "Body: " + responseBody);
    }
}