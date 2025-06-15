package org.unl.pacas.base.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeración que define los tipos de transacciones disponibles en el sistema
 * Incluye funcionalidades para validación, descripción y manejo de JSON
 */
public enum TipoTransaccion {
    
    // Definición de los tipos de transacción
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

    // Atributos del enum
    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final boolean afectaInventario;
    private final boolean requiereAprobacion;

    /**
     * Constructor del enum
     * @param codigo Código único del tipo de transacción
     * @param nombre Nombre descriptivo del tipo
     * @param descripcion Descripción detallada del tipo
     * @param afectaInventario Si la transacción afecta el inventario
     * @param requiereAprobacion Si requiere aprobación especial
     */
    TipoTransaccion(String codigo, String nombre, String descripcion, boolean afectaInventario, boolean requiereAprobacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.afectaInventario = afectaInventario;
        this.requiereAprobacion = requiereAprobacion;
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isAfectaInventario() {
        return afectaInventario;
    }

    public boolean isRequiereAprobacion() {
        return requiereAprobacion;
    }

    /**
     * Obtiene el valor para serialización JSON
     * @return El código del tipo de transacción
     */
    @JsonValue
    public String toValue() {
        return this.codigo;
    }

    /**
     * Crea una instancia desde un valor JSON
     * @param value El valor del código
     * @return La instancia del enum correspondiente
     */
    @JsonCreator
    public static TipoTransaccion fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        for (TipoTransaccion tipo : TipoTransaccion.values()) {
            if (tipo.codigo.equalsIgnoreCase(value.trim()) || 
                tipo.name().equalsIgnoreCase(value.trim())) {
                return tipo;
            }
        }
        
        throw new IllegalArgumentException("Tipo de transacción no válido: " + value);
    }

    /**
     * Busca un tipo de transacción por su código
     * @param codigo El código a buscar
     * @return El tipo de transacción encontrado o null si no existe
     */
    public static TipoTransaccion buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        
        for (TipoTransaccion tipo : TipoTransaccion.values()) {
            if (tipo.codigo.equalsIgnoreCase(codigo.trim())) {
                return tipo;
            }
        }
        return null;
    }

    /**
     * Busca un tipo de transacción por su nombre
     * @param nombre El nombre a buscar
     * @return El tipo de transacción encontrado o null si no existe
     */
    public static TipoTransaccion buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }
        
        for (TipoTransaccion tipo : TipoTransaccion.values()) {
            if (tipo.nombre.equalsIgnoreCase(nombre.trim())) {
                return tipo;
            }
        }
        return null;
    }

    /**
     * Obtiene todos los tipos que afectan inventario
     * @return Array de tipos que afectan inventario
     */
    public static TipoTransaccion[] getTiposQueAfectanInventario() {
        return java.util.Arrays.stream(TipoTransaccion.values())
                .filter(TipoTransaccion::isAfectaInventario)
                .toArray(TipoTransaccion[]::new);
    }

    /**
     * Obtiene todos los tipos que requieren aprobación
     * @return Array de tipos que requieren aprobación
     */
    public static TipoTransaccion[] getTiposQueRequierenAprobacion() {
        return java.util.Arrays.stream(TipoTransaccion.values())
                .filter(TipoTransaccion::isRequiereAprobacion)
                .toArray(TipoTransaccion[]::new);
    }

    /**
     * Obtiene todos los tipos de venta
     * @return Array de tipos relacionados con ventas
     */
    public static TipoTransaccion[] getTiposVenta() {
        return new TipoTransaccion[]{VENTA, DEVOLUCION, PROMOCION, REGALO, MUESTRA};
    }

    /**
     * Obtiene todos los tipos de compra
     * @return Array de tipos relacionados con compras
     */
    public static TipoTransaccion[] getTiposCompra() {
        return new TipoTransaccion[]{COMPRA, CONSIGNACION};
    }

    /**
     * Verifica si es un tipo de transacción de entrada (aumenta inventario)
     * @return true si es de entrada
     */
    public boolean esTransaccionEntrada() {
        return this == COMPRA || this == DEVOLUCION || this == AJUSTE_INVENTARIO || 
               this == TRANSFERENCIA || this == CONSIGNACION || this == PRESTAMO;
    }

    /**
     * Verifica si es un tipo de transacción de salida (disminuye inventario)
     * @return true si es de salida
     */
    public boolean esTransaccionSalida() {
        return this == VENTA || this == MERMA || this == PROMOCION || 
               this == REGALO || this == MUESTRA;
    }

    /**
     * Verifica si requiere validación de stock
     * @return true si requiere validación de stock
     */
    public boolean requiereValidacionStock() {
        return esTransaccionSalida() && this != AJUSTE_INVENTARIO;
    }

    /**
     * Verifica si genera factura
     * @return true si genera factura
     */
    public boolean generaFactura() {
        return this == VENTA || this == COMPRA || this == PROMOCION;
    }

    /**
     * Verifica si permite descuentos
     * @return true si permite descuentos
     */
    public boolean permiteDescuentos() {
        return this == VENTA || this == PROMOCION;
    }

    /**
     * Obtiene el factor de multiplicación para el inventario
     * @return 1 para entrada, -1 para salida, 0 para neutro
     */
    public int getFactorInventario() {
        if (esTransaccionEntrada()) {
            return 1;
        } else if (esTransaccionSalida()) {
            return -1;
        }
        return 0;
    }

    /**
     * Valida si el tipo es compatible con el método de pago
     * @param metodoPago El método de pago a validar
     * @return true si es compatible
     */
    public boolean esCompatibleConMetodoPago(MetodoPago metodoPago) {
        // Validaciones específicas según el tipo de transacción
        switch (this) {
            case REGALO:
            case MUESTRA:
                return metodoPago == MetodoPago.OTRO;
            case PRESTAMO:
            case CONSIGNACION:
                return metodoPago == MetodoPago.OTRO || metodoPago == MetodoPago.CREDITO;
            case CANCELACION:
                return true; // Puede usar cualquier método para reversar
            default:
                return metodoPago != MetodoPago.OTRO || this == OTRO;
        }
    }

    /**
     * Obtiene información completa del tipo de transacción
     * @return String con información detallada
     */
    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        info.append("Tipo: ").append(this.nombre);
        info.append(" | Código: ").append(this.codigo);
        info.append(" | Afecta Inventario: ").append(this.afectaInventario ? "Sí" : "No");
        info.append(" | Requiere Aprobación: ").append(this.requiereAprobacion ? "Sí" : "No");
        
        if (this.afectaInventario) {
            info.append(" | Tipo Movimiento: ");
            if (esTransaccionEntrada()) {
                info.append("Entrada (+)");
            } else if (esTransaccionSalida()) {
                info.append("Salida (-)");
            } else {
                info.append("Neutro");
            }
        }
        
        info.append(" | Genera Factura: ").append(generaFactura() ? "Sí" : "No");
        
        return info.toString();
    }

    /**
     * Obtiene las reglas de negocio aplicables
     * @return String con las reglas de negocio
     */
    public String getReglasNegocio() {
        StringBuilder reglas = new StringBuilder();
        
        if (requiereValidacionStock()) {
            reglas.append("• Requiere validación de stock disponible\n");
        }
        
        if (requiereAprobacion) {
            reglas.append("• Requiere aprobación de supervisor\n");
        }
        
        if (generaFactura()) {
            reglas.append("• Genera documento fiscal\n");
        }
        
        if (permiteDescuentos()) {
            reglas.append("• Permite aplicar descuentos\n");
        }
        
        return reglas.toString();
    }

    /**
     * Representación en String del tipo de transacción
     * @return El nombre del tipo de transacción
     */
    @Override
    public String toString() {
        return this.nombre;
    }

    /**
     * Obtiene una representación detallada para logs
     * @return String detallado para logging
     */
    public String toDetailedString() {
        return String.format("TipoTransaccion{codigo='%s', nombre='%s', afectaInventario=%s, requiereAprobacion=%s}", 
                           codigo, nombre, afectaInventario, requiereAprobacion);
    }
}