package com.example.myapplication4;

import com.example.myapplication4.ui.perfil.Usuario;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/medidietas/usuarios/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/medidietas/usuarios/logout")
    Call<ResponseBody> logout(@Header("x-token") String token);

    @GET("api/medidietas/usuarios/nombre_usuario")
    Call<Usuario> obtenerUsuarioPorNombre(@Path("nombreUsuario") String nombreUsuario, @Header("Authorization") String token);

    @POST("api/medidietas/usuarios/signup")
    Call<Void> registrarUsuario(@Body RegistroUsuario registroUsuario);
}
