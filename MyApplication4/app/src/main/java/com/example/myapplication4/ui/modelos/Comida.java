package com.example.myapplication4.ui.modelos;

import java.util.HashMap;

public class Comida {
    private int id;
    private String nombre;
    private String preparacion_video;
    private String receta;
    private boolean estado;
    private HashMap<String, Double> alimentos;
    private double calorias;
    private double carbohidratos;
    private double grasas;
    private double proteinas;

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

    public Comida(int id, String nombre, String preparacion_video, String receta, boolean estado, double calorias, double carbohidratos, double grasas, double proteinas) {
        this.id = id;
        this.nombre = nombre;
        this.preparacion_video = preparacion_video;
        this.receta = receta;
        this.estado = estado;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.proteinas = proteinas;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPreparacionVideo() { return preparacion_video; }
    public String getReceta() { return receta; }
    public boolean getEstado() { return estado; }
    public HashMap<String, Double> getAlimentos() { return alimentos; }
    public double getCalorias() { return calorias; }
    public double getCarbohidratos() { return carbohidratos; }
    public double getGrasas() { return grasas; }
    public double getProteinas() { return proteinas; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPreparacionVideo(String preparacion_video) { this.preparacion_video = preparacion_video; }
    public void setReceta(String receta) { this.receta = receta; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public void setAlimentos(HashMap<String, Double> alimentos) { this.alimentos = alimentos; }
    public void setCalorias(double calorias) { this.calorias = calorias; }
    public void setCarbohidratos(double carbohidratos) { this.carbohidratos = carbohidratos; }
    public void setGrasas(double grasas) { this.grasas = grasas; }
    public void setProteinas(double proteinas) { this.proteinas = proteinas; }
}