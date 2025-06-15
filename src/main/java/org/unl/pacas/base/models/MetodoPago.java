package org.unl.pacas.base.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeración que define los métodos de pago disponibles en el sistema
 * Incluye funcionalidades para validación, descripción y manejo de JSON
 */
public enum MetodoPago {
    
    // Definición de los métodos de pago
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

    // Atributos del enum
    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final boolean activo;
    private final double comisionPorcentaje;

    /**
     * Constructor del enum
     * @param codigo Código único del método de pago
     * @param nombre Nombre descriptivo del método
     * @param descripcion Descripción detallada del método
     * @param activo Si el método está activo o no
     * @param comisionPorcentaje Porcentaje de comisión aplicable
     */
    MetodoPago(String codigo, String nombre, String descripcion, boolean activo, double comisionPorcentaje) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.comisionPorcentaje = comisionPorcentaje;
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

    public boolean isActivo() {
        return activo;
    }

    public double getComisionPorcentaje() {
        return comisionPorcentaje;
    }

    /**
     * Obtiene el valor para serialización JSON
     * @return El código del método de pago
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
    public static MetodoPago fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        for (MetodoPago metodo : MetodoPago.values()) {
            if (metodo.codigo.equalsIgnoreCase(value.trim()) || 
                metodo.name().equalsIgnoreCase(value.trim())) {
                return metodo;
            }
        }
        
        throw new IllegalArgumentException("Método de pago no válido: " + value);
    }

    /**
     * Busca un método de pago por su código
     * @param codigo El código a buscar
     * @return El método de pago encontrado o null si no existe
     */
    public static MetodoPago buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        
        for (MetodoPago metodo : MetodoPago.values()) {
            if (metodo.codigo.equalsIgnoreCase(codigo.trim())) {
                return metodo;
            }
        }
        return null;
    }

    /**
     * Busca un método de pago por su nombre
     * @param nombre El nombre a buscar
     * @return El método de pago encontrado o null si no existe
     */
    public static MetodoPago buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }
        
        for (MetodoPago metodo : MetodoPago.values()) {
            if (metodo.nombre.equalsIgnoreCase(nombre.trim())) {
                return metodo;
            }
        }
        return null;
    }

    /**
     * Obtiene todos los métodos de pago activos
     * @return Array de métodos de pago activos
     */
    public static MetodoPago[] getMetodosActivos() {
        return java.util.Arrays.stream(MetodoPago.values())
                .filter(MetodoPago::isActivo)
                .toArray(MetodoPago[]::new);
    }

    /**
     * Verifica si el método de pago requiere validación adicional
     * @return true si requiere validación adicional
     */
    public boolean requiereValidacionAdicional() {
        return this == CHEQUE || this == TARJETA_CREDITO || this == TARJETA_DEBITO || this == CREDITO;
    }

    /**
     * Verifica si el método de pago es inmediato
     * @return true si el pago es inmediato
     */
    public boolean esInmediato() {
        return this == EFECTIVO || this == TARJETA_CREDITO || this == TARJETA_DEBITO;
    }

    /**
     * Verifica si el método de pago tiene comisión
     * @return true si tiene comisión
     */
    public boolean tieneComision() {
        return this.comisionPorcentaje > 0.0;
    }

    /**
     * Calcula la comisión para un monto dado
     * @param monto El monto base
     * @return El valor de la comisión
     */
    public double calcularComision(double monto) {
        if (monto <= 0 || !tieneComision()) {
            return 0.0;
        }
        return (monto * this.comisionPorcentaje) / 100.0;
    }

    /**
     * Obtiene el monto total incluyendo comisión
     * @param montoBase El monto base
     * @return El monto total con comisión
     */
    public double calcularMontoTotal(double montoBase) {
        return montoBase + calcularComision(montoBase);
    }

    /**
     * Valida si el método de pago es válido para el contexto dado
     * @param esVentaOnline Si es una venta online
     * @param montoMinimo El monto mínimo requerido
     * @param monto El monto de la transacción
     * @return true si es válido
     */
    public boolean esValidoParaTransaccion(boolean esVentaOnline, double montoMinimo, double monto) {
        // Verificar si está activo
        if (!this.activo) {
            return false;
        }
        
        // Verificar monto mínimo
        if (monto < montoMinimo) {
            return false;
        }
        
        // Validaciones específicas para venta online
        if (esVentaOnline) {
            return this != EFECTIVO && this != CHEQUE;
        }
        
        return true;
    }

    /**
     * Obtiene información completa del método de pago
     * @return String con información detallada
     */
    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        info.append("Método: ").append(this.nombre);
        info.append(" | Código: ").append(this.codigo);
        info.append(" | Estado: ").append(this.activo ? "Activo" : "Inactivo");
        
        if (this.tieneComision()) {
            info.append(" | Comisión: ").append(this.comisionPorcentaje).append("%");
        }
        
        info.append(" | Tipo: ");
        if (this.esInmediato()) {
            info.append("Inmediato");
        } else {
            info.append("Diferido");
        }
        
        return info.toString();
    }

    /**
     * Representación en String del método de pago
     * @return El nombre del método de pago
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
        return String.format("MetodoPago{codigo='%s', nombre='%s', activo=%s, comision=%.2f%%}", 
                           codigo, nombre, activo, comisionPorcentaje);
    }
}