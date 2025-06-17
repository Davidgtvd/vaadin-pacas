package org.unl.pacas.base.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MetodoPago {
    EFECTIVO("EFECTIVO", "Efectivo", "Pago en efectivo", true, 0.0),
    TRANSFERENCIA("TRANSFERENCIA", "Transferencia Bancaria", "Transferencia bancaria o interbancaria", true, 0.0),
    TARJETA_CREDITO("TARJETA_CREDITO", "Tarjeta de Crédito", "Pago con tarjeta de crédito", true, 3.5),
    TARJETA_DEBITO("TARJETA_DEBITO", "Tarjeta de Débito", "Pago con tarjeta de débito", true, 1.5),
    CHEQUE("CHEQUE", "Cheque", "Pago con cheque", false, 0.0),
    CREDITO("CREDITO", "Crédito", "Venta a crédito", true, 0.0),
    DEPOSITO("DEPOSITO", "Depósito Bancario", "Depósito directo en cuenta bancaria", true, 0.0),
    PAYPAL("PAYPAL", "PayPal", "Pago a través de PayPal", false, 4.0),
    BITCOIN("BITCOIN", "Bitcoin", "Pago con criptomoneda Bitcoin", false, 1.0),
    OTRO("OTRO", "Otro", "Otro método de pago", true, 0.0);

    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final boolean activo;
    private final double comisionPorcentaje;

    MetodoPago(String codigo, String nombre, String descripcion, boolean activo, double comisionPorcentaje) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.comisionPorcentaje = comisionPorcentaje;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public boolean isActivo() { return activo; }
    public double getComisionPorcentaje() { return comisionPorcentaje; }

    @JsonValue
    public String toValue() { return this.codigo; }

    @JsonCreator
    public static MetodoPago fromValue(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        for (MetodoPago metodo : MetodoPago.values()) {
            if (metodo.codigo.equalsIgnoreCase(value.trim()) || metodo.name().equalsIgnoreCase(value.trim())) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Método de pago no válido: " + value);
    }

    // Otros métodos (buscarPorCodigo, requiereValidacionAdicional, etc.) según tu código original
    // ...
}