package com.example.gatobar.models;

public class Plato {

    public static Plato plato = null;

    private int plato_id;
    private String nombre;
    private String ingredientes;
    private double precio;
    private int categoria_id;

    public Plato() {
    }

    public Plato(int plato_id, String nombre, String ingredientes, double precio, int categoria_id) {
        this.plato_id = plato_id;
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.precio = precio;
        this.categoria_id = categoria_id;
    }

    public int getPlato_id() {
        return plato_id;
    }

    public void setPlato_id(int plato_id) {
        this.plato_id = plato_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(int categoria_id) {
        this.categoria_id = categoria_id;
    }
}