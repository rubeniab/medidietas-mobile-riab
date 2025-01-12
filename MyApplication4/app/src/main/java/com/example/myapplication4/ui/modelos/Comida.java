package com.example.myapplication4.ui.modelos;

import java.util.HashMap;

public class Comida {
    private int id;
    private String nombre;
    private String preparacion_video;
    private String receta;
    private boolean estado;
    private HashMap<String, Double> alimentos;

    public Comida(int id, String nombre, String preparacion_video, String receta, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.preparacion_video = preparacion_video;
        this.receta = receta;
        this.estado = estado;
    }

    public Comida(String nombre, String preparacion_video, String receta, HashMap<String, Double> alimentos) {
        this.nombre = nombre;
        this.preparacion_video = preparacion_video;
        this.receta = receta;
        this.alimentos = alimentos;
    }

    public Comida(int id, String nombre, String preparacion_video, String receta, boolean estado, HashMap<String, Double> alimentos) {
        this.id = id;
        this.nombre = nombre;
        this.preparacion_video = preparacion_video;
        this.receta = receta;
        this.estado = estado;
        this.alimentos = alimentos;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPreparacionVideo() { return preparacion_video; }
    public String getReceta() { return receta; }
    public boolean getEstado() { return estado; }
    public HashMap<String, Double> getAlimentos() { return alimentos; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPreparacionVideo(String preparacion_video) { this.preparacion_video = preparacion_video; }
    public void setReceta(String receta) { this.receta = receta; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public void setAlimentos(HashMap<String, Double> alimentos) { this.alimentos = alimentos; }
}

