package com.example.myapplication4.ui.modelos;

public class Consumo {
    private String fecha;
    private double cantidad;
    private int idMomento;
    private int idConsumo;
    private int idUsuarioMovil;

    public Consumo(String fecha, double cantidad, int idMomento, int idConsumo, int idUsuarioMovil) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.idMomento = idMomento;
        this.idConsumo = idConsumo;
        this.idUsuarioMovil = idUsuarioMovil;
    }

    public String getFecha() { return fecha; }
    public double getCantidad() { return cantidad; }
    public int getIdMomento() { return idMomento; }
    public int getIdConsumo() { return idConsumo; }
    public int getIdUsuarioMovil() { return idUsuarioMovil; }

    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }
    public void setIdMomento(int idMomento) { this.idMomento = idMomento; }
    public void setIdConsumo(int idConsumo) { this.idConsumo = idConsumo; }
    public void setIdUsuarioMovil(int idUsuarioMovil) { this.idUsuarioMovil = idUsuarioMovil; }

    @Override
    public String toString() {
        return "Consumo{" +
                "fecha='" + fecha + '\'' +
                ", cantidad=" + cantidad +
                ", idMomento=" + idMomento +
                ", idConsumo=" + idConsumo +
                ", idUsuarioMovil=" + idUsuarioMovil +
                '}';
    }
}