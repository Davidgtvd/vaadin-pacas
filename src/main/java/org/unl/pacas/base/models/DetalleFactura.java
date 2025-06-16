package org.unl.pacas.base.models;

public class DetalleFactura {
    private int id;
    private float total;
    private int cantidad;
    private float precioUnitario;
    private int id_producto;
    private int id_compra;

    

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotal() {
        return this.total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioUnitario() {
        return this.precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getId_producto() {
        return this.id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_compra() {
        return this.id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }
      
}
