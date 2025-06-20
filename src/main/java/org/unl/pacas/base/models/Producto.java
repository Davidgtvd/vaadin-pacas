package org.unl.pacas.base.models;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private float precio;
    private int stock;
    private float pvp;
    private CategoriaEmun categoria;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public float getPrecio() {
        return this.precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getPvp() {
        return this.pvp;
    }

    public void setPvp(float pvp) {
        this.pvp = pvp;
    }

    public CategoriaEmun getCategoria() {
        return this.categoria;
    }

    public void setCategoria(CategoriaEmun categoria) {
        this.categoria = categoria;
    }
    
}
