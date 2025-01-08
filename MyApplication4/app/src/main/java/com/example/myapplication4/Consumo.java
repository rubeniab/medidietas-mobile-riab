package com.example.myapplication4;

public class Consumo {
    private String nombre;
    private String racion;
    private int calorias;
    private String categoria;

    public Consumo(String nombre, String racion, int calorias, String categoria) {
        this.nombre = nombre;
        this.racion = racion;
        this.calorias = calorias;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRacion() {
        return racion;
    }

    public int getCalorias() {
        return calorias;
    }

    public String getCategoria() {
        return categoria;
    }
}
