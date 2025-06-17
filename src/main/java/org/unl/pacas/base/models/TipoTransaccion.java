package org.unl.pacas.base.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoTransaccion {
    COMPRA("COMPRA", "Compra", "Transacción de compra de productos", true, true),
    VENTA("VENTA", "Venta", "Transacción de venta de productos", true, false),
    DEVOLUCION("DEVOLUCION", "Devolución", "Devolución de productos vendidos", true, false),
    AJUSTE_INVENTARIO("AJUSTE_INVENTARIO", "Ajuste de Inventario", "Ajuste por diferencias en inventario", true, true),
    TRANSFERENCIA("TRANSFERENCIA", "Transferencia", "Transferencia entre almacenes", false, true),
    MERMA("MERMA", "Merma", "Pérdida de productos por deterioro", true, true),
    PROMOCION("PROMOCION", "Promoción", "Venta con descuento promocional", true, false),
    CONSIGNACION("CONSIGNACION", "Consignación", "Productos en consignación", false, true),
    PRESTAMO("PRESTAMO", "Préstamo", "Préstamo de productos", false, true),
    REGALO("REGALO", "Regalo", "Productos entregados como regalo", true, false),
    MUESTRA("MUESTRA", "Muestra", "Productos entregados como muestra", true, false),
    CANCELACION("CANCELACION", "Cancelación", "Cancelación de transacción", true, false),
    OTRO("OTRO", "Otro", "Otro tipo de transacción", true, true);

    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final boolean afectaInventario;
    private final boolean requiereAprobacion;

    TipoTransaccion(String codigo, String nombre, String descripcion, boolean afectaInventario, boolean requiereAprobacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.afectaInventario = afectaInventario;
        this.requiereAprobacion = requiereAprobacion;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public boolean isAfectaInventario() { return afectaInventario; }
    public boolean isRequiereAprobacion() { return requiereAprobacion; }

    @JsonValue
    public String toValue() { return this.codigo; }

    @JsonCreator
    public static TipoTransaccion fromValue(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        for (TipoTransaccion tipo : TipoTransaccion.values()) {
            if (tipo.codigo.equalsIgnoreCase(value.trim()) || tipo.name().equalsIgnoreCase(value.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de transacción no válido: " + value);
    }

    // Otros métodos según tu código original
    // ...
}