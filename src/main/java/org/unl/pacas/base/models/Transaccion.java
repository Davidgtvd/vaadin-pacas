package org.unl.pacas.base.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "transacciones", indexes = {
    @Index(name = "idx_transaccion_numero", columnList = "numeroFactura", unique = true),
    @Index(name = "idx_transaccion_fecha", columnList = "fecha"),
    @Index(name = "idx_transaccion_tipo", columnList = "tipo"),
    @Index(name = "idx_transaccion_estado", columnList = "estado"),
    @Index(name = "idx_transaccion_cliente", columnList = "identificacionCliente")
})
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El número de factura no puede estar vacío")
    @Size(min = 5, max = 30, message = "El número de factura debe tener entre 5 y 30 caracteres")
    @Column(nullable = false, unique = true, length = 30)
    private String numeroFactura;

    @NotNull(message = "La fecha no puede ser nula")
    @Column(nullable = false)
    private LocalDateTime fecha;

    @NotEmpty(message = "El tipo de transacción no puede estar vacío")
    @Pattern(regexp = "VENTA|COMPRA|DEVOLUCION|AJUSTE|AJUSTE_INVENTARIO", message = "El tipo debe ser: VENTA, COMPRA, DEVOLUCION, AJUSTE o AJUSTE_INVENTARIO")
    @Column(nullable = false, length = 20)
    private String tipo;

    @NotEmpty(message = "El estado no puede estar vacío")
    @Pattern(regexp = "PENDIENTE|COMPLETADA|CANCELADA|ANULADA", message = "El estado debe ser: PENDIENTE, COMPLETADA, CANCELADA o ANULADA")
    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @NotEmpty(message = "El método de pago no puede estar vacío")
    @Pattern(regexp = "EFECTIVO|TARJETA_CREDITO|TARJETA_DEBITO|TRANSFERENCIA|CHEQUE|CREDITO", message = "Método de pago inválido")
    @Column(nullable = false, length = 30)
    private String metodoPago;

    @NotNull(message = "El subtotal no puede ser nulo")
    @DecimalMin(value = "0.00", message = "El subtotal no puede ser negativo")
    @Digits(integer = 12, fraction = 2, message = "El subtotal debe tener máximo 12 enteros y 2 decimales")
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo")
    @Digits(integer = 12, fraction = 2, message = "El descuento debe tener máximo 12 enteros y 2 decimales")
    @Column(precision = 14, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "El IVA no puede ser negativo")
    @Digits(integer = 12, fraction = 2, message = "El IVA debe tener máximo 12 enteros y 2 decimales")
    @Column(precision = 14, scale = 2)
    private BigDecimal iva = BigDecimal.ZERO;

    @NotNull(message = "El total no puede ser nulo")
    @DecimalMin(value = "0.00", message = "El total no puede ser negativo")
    @Digits(integer = 12, fraction = 2, message = "El total debe tener máximo 12 enteros y 2 decimales")
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    // Información del cliente
    @Size(max = 100, message = "El nombre del cliente no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String nombreCliente;

    @Size(max = 20, message = "La identificación del cliente no puede tener más de 20 caracteres")
    @Column(length = 20)
    private String identificacionCliente;

    @Size(max = 15, message = "El teléfono del cliente no puede tener más de 15 caracteres")
    @Column(length = 15)
    private String telefonoCliente;

    @Email(message = "El email del cliente debe ser válido")
    @Size(max = 100, message = "El email del cliente no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String emailCliente;

    @Size(max = 200, message = "La dirección del cliente no puede tener más de 200 caracteres")
    @Column(length = 200)
    private String direccionCliente;

    // Información de pago
    @DecimalMin(value = "0.00", message = "El monto pagado no puede ser negativo")
    @Digits(integer = 12, fraction = 2, message = "El monto pagado debe tener máximo 12 enteros y 2 decimales")
    @Column(precision = 14, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;

    @Digits(integer = 12, fraction = 2, message = "El cambio debe tener máximo 12 enteros y 2 decimales")
    @Column(precision = 14, scale = 2)
    private BigDecimal cambio = BigDecimal.ZERO;

    @Size(max = 50, message = "La referencia de pago no puede tener más de 50 caracteres")
    @Column(length = 50)
    private String referenciaPago;

    @Size(max = 4, message = "Los últimos 4 dígitos de tarjeta no pueden tener más de 4 caracteres")
    @Column(length = 4)
    private String ultimosDigitosTarjeta;

    // Información adicional
    @Size(max = 1000, message = "Las observaciones no pueden tener más de 1000 caracteres")
    @Column(length = 1000)
    private String observaciones;

    @Size(max = 500, message = "Las notas internas no pueden tener más de 500 caracteres")
    @Column(length = 500)
    private String notasInternas;

    @Column
    private LocalDateTime fechaVencimiento;

    @Column
    private LocalDateTime fechaPago;

    @Column
    private LocalDateTime fechaAnulacion;

    @Size(max = 200, message = "El motivo de anulación no puede tener más de 200 caracteres")
    @Column(length = 200)
    private String motivoAnulacion;

    // Información de entrega
    @Size(max = 200, message = "La dirección de entrega no puede tener más de 200 caracteres")
    @Column(length = 200)
    private String direccionEntrega;

    @Column
    private LocalDateTime fechaEntrega;

    @Size(max = 100, message = "El transportista no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String transportista;

    @Size(max = 50, message = "El número de guía no puede tener más de 50 caracteres")
    @Column(length = 50)
    private String numeroGuia;

    // Campos de auditoría
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @Size(max = 50, message = "El usuario creador no puede tener más de 50 caracteres")
    @Column(length = 50)
    private String usuarioCreacion;

    @Size(max = 50, message = "El usuario modificador no puede tener más de 50 caracteres")
    @Column(length = 50)
    private String usuarioModificacion;

    // RELACIÓN CORRECTA CON PRODUCTO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TransaccionDetalle> detalles = new ArrayList<>();

    // ====== GETTERS Y SETTERS ======

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getIdentificacionCliente() { return identificacionCliente; }
    public void setIdentificacionCliente(String identificacionCliente) { this.identificacionCliente = identificacionCliente; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public String getDireccionCliente() { return direccionCliente; }
    public void setDireccionCliente(String direccionCliente) { this.direccionCliente = direccionCliente; }

    public BigDecimal getMontoPagado() { return montoPagado; }
    public void setMontoPagado(BigDecimal montoPagado) { this.montoPagado = montoPagado; }

    public BigDecimal getCambio() { return cambio; }
    public void setCambio(BigDecimal cambio) { this.cambio = cambio; }

    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }

    public String getUltimosDigitosTarjeta() { return ultimosDigitosTarjeta; }
    public void setUltimosDigitosTarjeta(String ultimosDigitosTarjeta) { this.ultimosDigitosTarjeta = ultimosDigitosTarjeta; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getNotasInternas() { return notasInternas; }
    public void setNotasInternas(String notasInternas) { this.notasInternas = notasInternas; }

    public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDateTime fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public LocalDateTime getFechaAnulacion() { return fechaAnulacion; }
    public void setFechaAnulacion(LocalDateTime fechaAnulacion) { this.fechaAnulacion = fechaAnulacion; }

    public String getMotivoAnulacion() { return motivoAnulacion; }
    public void setMotivoAnulacion(String motivoAnulacion) { this.motivoAnulacion = motivoAnulacion; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getTransportista() { return transportista; }
    public void setTransportista(String transportista) { this.transportista = transportista; }

    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(String usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }

    public String getUsuarioModificacion() { return usuarioModificacion; }
    public void setUsuarioModificacion(String usuarioModificacion) { this.usuarioModificacion = usuarioModificacion; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }

    public List<TransaccionDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<TransaccionDetalle> detalles) { this.detalles = detalles; }

    // equals y hashCode (opcional pero recomendado)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaccion)) return false;
        Transaccion that = (Transaccion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString (opcional)
    @Override
    public String toString() {
        return "Transaccion{" +
                "id=" + id +
                ", numeroFactura='" + numeroFactura + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado='" + estado + '\'' +
                ", total=" + total +
                ", fecha=" + fecha +
                '}';
    }
}