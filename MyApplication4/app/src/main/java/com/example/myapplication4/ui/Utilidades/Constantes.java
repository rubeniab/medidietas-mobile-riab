package com.example.myapplication4.ui.Utilidades;

public class Constantes {

    // KEYS para los HashMaps
    public static final String KEY_ERROR = "error";
    public static final String KEY_MENSAJE = "mensaje";
    public static final String KEY_OBJETO = "objeto";
    public static final String KEY_IMAGEN = "imagen";

    // Constantes para la conexión con la API
    public static final String PUERTO_REST = "8081";
    public static final String PUERTO_GRPC = "50052";
    public static final String HOST = "10.0.2.2";
    public static final String URL_REST = "http://" + HOST + ":" + PUERTO_REST + "/api/medidietas/";
    public static final String URL_GRPC = HOST + ":" + PUERTO_GRPC;
}
