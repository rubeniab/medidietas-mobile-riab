package com.example.myapplication4.ui.modelos;

public class ConsumoDiario {
    private String nombre;
    private String tamano_racion;
    private double calorias;
    private double carbohidratos;
    private double grasas;
    private double proteinas;
    private double cantidad;
    private String momento;

    public ConsumoDiario(String nombre, String tamano_racion, double calorias, double carbohidratos, double grasas, double proteinas, double cantidad, String momento) {
        this.nombre = nombre;
        this.tamano_racion = tamano_racion;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.proteinas = proteinas;
        this.cantidad = cantidad;
        this.momento = momento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTamano_racion() {
        return tamano_racion;
    }

    public void setTamano_racion(String tamano_racion) {
        this.tamano_racion = tamano_racion;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public double getGrasas() {
        return grasas;
    }

    public void setGrasas(double grasas) {
        this.grasas = grasas;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getMomento() {
        return momento;
    }

    public void setMomento(String momento) {
        this.momento = momento;
    }
}
