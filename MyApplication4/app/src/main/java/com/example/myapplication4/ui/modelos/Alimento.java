package com.example.myapplication4.ui.modelos;

public class Alimento {
    private int id;
    private String nombre;
    private int calorias;
    private double carbohidratos;
    private double grasas;
    private double proteinas;
    private String imagen;
    private double tamano_racion;
    private boolean estado;
    private String marca;
    private int id_categoria;
    private int id_unidad_medida;

    public Alimento(int id, String nombre, int calorias, double carbohidratos, double grasas, double proteinas,
                    String imagen, double tamano_racion, boolean estado, String marca, int id_categoria,
                    int id_unidad_medida) {
        this.id = id;
        this.nombre = nombre;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.proteinas = proteinas;
        this.imagen = imagen;
        this.tamano_racion = tamano_racion;
        this.estado = estado;
        this.marca = marca;
        this.id_categoria = id_categoria;
        this.id_unidad_medida = id_unidad_medida;
    }

    public Alimento(String nombre, int calorias, double carbohidratos, double grasas, double proteinas, String imagen, double tamano_racion, String marca, int id_categoria, int id_unidad_medida) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.proteinas = proteinas;
        this.imagen = imagen;
        this.tamano_racion = tamano_racion;
        this.marca = marca;
        this.id_categoria = id_categoria;
        this.id_unidad_medida = id_unidad_medida;
    }

    public Alimento(int id, String nombre, int calorias, double carbohidratos, double grasas, double proteinas,
                    String imagen, double tamano_racion, String marca, int id_categoria,
                    int id_unidad_medida) {
        this.id = id;
        this.nombre = nombre;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.proteinas = proteinas;
        this.imagen = imagen;
        this.tamano_racion = tamano_racion;
        this.marca = marca;
        this.id_categoria = id_categoria;
        this.id_unidad_medida = id_unidad_medida;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCalorias() { return calorias; }
    public double getCarbohidratos() { return carbohidratos; }
    public double getGrasas() { return grasas; }
    public double getProteinas() { return proteinas; }
    public String getImagen() { return imagen; }
    public double getTamanoRacion() { return tamano_racion; }
    public boolean isEstado() { return estado; }
    public String getMarca() { return marca; }
    public int getIdCategoria() { return id_categoria; }
    public int getIdUnidadMedida() { return id_unidad_medida; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCalorias(int calorias) { this.calorias = calorias; }
    public void setCarbohidratos(double carbohidratos) { this.carbohidratos = carbohidratos; }
    public void setGrasas(double grasas) { this.grasas = grasas; }
    public void setProteinas(double proteinas) { this.proteinas = proteinas; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public void setTamanoRacion(double tamano_racion) { this.tamano_racion = tamano_racion; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setIdCategoria(int id_categoria) { this.id_categoria = id_categoria; }
    public void setIdUnidadMedida(int id_unidad_medida) { this.id_unidad_medida = id_unidad_medida; }

}
