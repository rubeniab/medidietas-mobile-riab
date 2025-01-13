package com.example.myapplication4.ui.perfil;

public class Usuario {
    private int id;
    private String nombre_usuario;
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String contrasena;
    private String correo;
    private String fecha_nacimiento;
    private String foto;
    private double estatura;
    private double peso;
    private boolean sexo;
    private int id_objetivo;
    private double calorias;
    private double carbohidratos;
    private double grasas;
    private double proteinas;

    public Usuario(int id, String nombre_usuario, String nombre, String apellido_paterno, String apellido_materno, String contrasena, String correo, String fecha_nacimiento, String foto, double estatura, double peso, boolean sexo, int id_objetivo, double calorias, double carbohidratos, double grasas, double proteinas) {
        this.id = id;
        this.nombre_usuario = nombre_usuario;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.contrasena = contrasena;
        this.correo = correo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.foto = foto;
        this.estatura = estatura;
        this.peso = peso;
        this.sexo = sexo;
        this.id_objetivo = id_objetivo;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.proteinas = proteinas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public boolean isSexo() {
        return sexo;
    }

    public void setSexo(boolean sexo) {
        this.sexo = sexo;
    }

    public int getId_objetivo() {
        return id_objetivo;
    }

    public void setId_objetivo(int id_objetivo) {
        this.id_objetivo = id_objetivo;
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

}
