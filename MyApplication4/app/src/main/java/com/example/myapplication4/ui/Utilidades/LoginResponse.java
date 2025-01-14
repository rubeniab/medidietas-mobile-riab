package com.example.myapplication4.ui.Utilidades;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{
    private String msg;
    private Usuario usuario;


    @SerializedName("access_token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public class Usuario {
        String correo;
        String nombre_usuario;
        String apellido_paterno;
        String calorias;
        // Otros campos de la respuesta
    }
}