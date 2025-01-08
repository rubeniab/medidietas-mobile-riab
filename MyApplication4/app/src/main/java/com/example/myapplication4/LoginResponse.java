package com.example.myapplication4;

public class LoginResponse{
    private String msg;
    private Usuario usuario;
    private String token;

    public String getMsg() {
        return msg;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getToken() {
        return token;
    }

    public class Usuario {
        private String correo;
        String nombre_usuario;
        // Otros campos de la respuesta
    }
}