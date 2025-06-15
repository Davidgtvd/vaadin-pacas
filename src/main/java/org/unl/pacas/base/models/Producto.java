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
@Table(name = "productos", indexes = {
    @Index(name = "idx_producto_codigo", columnList = "codigo", unique = true),
    @Index(name = "idx_producto_nombre", columnList = "nombre"),
    @Index(name = "idx_producto_categoria", columnList = "categoria"),
    @Index(name = "idx_producto_activo", columnList = "activo"),
    @Index(name = "idx_producto_stock", columnList = "stock")
})
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El código no puede estar vacío")
    @Size(min = 3, max = 20, message = "El código debe tener entre 3 y 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @NotEmpty(message = "La categoría no puede estar vacía")
    @Size(min = 2, max = 50, message = "La categoría debe tener entre 2 y 50 caracteres")
    @Column(nullable = false, length = 50)
    private String categoria;

    @Size(max = 50, message = "La marca no puede tener más de 50 caracteres")
    @Column(length = 50)
    private String marca;

    @Size(max = 30, message = "El color no puede tener más de 30 caracteres")
    @Column(length = 30)
    private String color;

    @Size(max = 20, message = "La talla no puede tener más de 20 caracteres")
    @Column(length = 20)
    private String talla;

    @Size(max = 30, message = "El material no puede tener más de 30 caracteres")
    @Column(length = 30)
    private String material;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock = 0;

    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    @Column(nullable = false)
    private Integer stockMinimo = 5;

    @Min(value = 0, message = "El stock máximo no puede ser negativo")
    @Column(nullable = false)
    private Integer stockMaximo = 1000;

    @NotNull(message = "El precio de compra no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio de compra debe tener máximo 10 enteros y 2 decimales")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioCompra;

    @NotNull(message = "El precio de venta no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio de venta debe tener máximo 10 enteros y 2 decimales")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo")
    @DecimalMax(value = "100.00", message = "El descuento no puede ser mayor a 100%")
    @Digits(integer = 3, fraction = 2, message = "El descuento debe tener máximo 3 enteros y 2 decimales")
    @Column(precision = 5, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "El IVA no puede ser negativo")
    @DecimalMax(value = "100.00", message = "El IVA no puede ser mayor a 100%")
    @Digits(integer = 3, fraction = 2, message = "El IVA debe tener máximo 3 enteros y 2 decimales")
    @Column(precision = 5, scale = 2)
    private BigDecimal iva = new BigDecimal("12.00"); // IVA por defecto en Ecuador

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false)
    private Boolean esServicio = false;

    @Column(nullable = false)
    private Boolean manejaInventario = true;

    @Column(nullable = false)
    private Boolean permiteVentaSinStock = false;

    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    @Column(length = 255)
    private String imagenUrl;

    @Size(max = 50, message = "La unidad de medida no puede tener más de 50 caracteres")
    @Column(length = 50)
    private String unidadMedida = "UNIDAD";

    @DecimalMin(value = "0.00", message = "El peso no puede ser negativo")
    @Digits(integer = 8, fraction = 3, message = "El peso debe tener máximo 8 enteros y 3 decimales")
    @Column(precision = 11, scale = 3)
    private BigDecimal peso;

    @Size(max = 100, message = "Las dimensiones no pueden tener más de 100 caracteres")
    @Column(length = 100)
    private String dimensiones;

    @Column
    private LocalDateTime fechaVencimiento;

    @Size(max = 50, message = "El proveedor no puede tener más de 50 caracteres")
    @Column(length = 50)
    private String proveedor;

    @Size(max = 100, message = "La ubicación no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String ubicacion;

    @Size(max = 1000, message = "Las observaciones no pueden tener más de 1000 caracteres")
    @Column(length = 1000)
    private String observaciones;

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

    // Relaciones
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransaccionDetalle> transaccionDetalles = new ArrayList<>();

    // Constructores
    public Producto() {}

    public Producto(String codigo, String nombre, String categoria, BigDecimal precioCompra, BigDecimal precioVenta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
    }

    // Métodos de negocio
    
    /**
     * Calcula el margen de ganancia en porcentaje
     */
    public BigDecimal calcularMargenGanancia() {
        if (precioCompra == null || precioCompra.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return precioVenta.subtract(precioCompra)
                .divide(precioCompra, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    /**
     * Calcula la ganancia unitaria
     */
    public BigDecimal calcularGananciaUnitaria() {
        if (precioVenta == null || precioCompra == null) {
            return BigDecimal.ZERO;
        }
        return precioVenta.subtract(precioCompra);
    }

    /**
     * Calcula el precio con descuento aplicado
     */
    public BigDecimal calcularPrecioConDescuento() {
        if (descuento == null || descuento.compareTo(BigDecimal.ZERO) == 0) {
            return precioVenta;
        }
        BigDecimal porcentajeDescuento = descuento.divide(new BigDecimal("100"));
        BigDecimal montoDescuento = precioVenta.multiply(porcentajeDescuento);
        return precioVenta.subtract(montoDescuento);
    }

    /**
     * Calcula el precio final con IVA incluido
     */
    public BigDecimal calcularPrecioConIva() {
        BigDecimal precioBase = calcularPrecioConDescuento();
        if (iva == null || iva.compareTo(BigDecimal.ZERO) == 0) {
            return precioBase;
        }
        BigDecimal porcentajeIva = iva.divide(new BigDecimal("100"));
        BigDecimal montoIva = precioBase.multiply(porcentajeIva);
        return precioBase.add(montoIva);
    }

    /**
     * Verifica si el producto tiene stock bajo
     */
    public boolean tieneStockBajo() {
        return stock != null && stockMinimo != null && stock <= stockMinimo;
    }

    /**
     * Verifica si el producto está sin stock
     */
    public boolean estaSinStock() {
        return stock == null || stock == 0;
    }

    /**
     * Verifica si se puede vender la cantidad solicitada
     */
    public boolean puedeVender(int cantidad) {
        if (!manejaInventario) {
            return true;
        }
        if (permiteVentaSinStock) {
            return true;
        }
        return stock != null && stock >= cantidad;
    }

    /**
     * Reduce el stock del producto
     */
    public void reducirStock(int cantidad) {
        if (manejaInventario && stock != null) {
            this.stock = Math.max(0, this.stock - cantidad);
        }
    }

    /**
     * Aumenta el stock del producto
     */
    public void aumentarStock(int cantidad) {
        if (manejaInventario) {
            this.stock = (this.stock == null ? 0 : this.stock) + cantidad;
        }
    }

    /**
     * Verifica si el producto está próximo a vencer
     */
    public boolean estaProximoAVencer(int diasAnticipacion) {
        if (fechaVencimiento == null) {
            return false;
        }
        LocalDateTime fechaLimite = LocalDateTime.now().plusDays(diasAnticipacion);
        return fechaVencimiento.isBefore(fechaLimite);
    }

    /**
     * Verifica si el producto está vencido
     */
    public boolean estaVencido() {
        if (fechaVencimiento == null) {
            return false;
        }
        return fechaVencimiento.isBefore(LocalDateTime.now());
    }

    /**
     * Obtiene el valor total del inventario de este producto
     */
    public BigDecimal calcularValorInventario() {
        if (stock == null || precioCompra == null) {
            return BigDecimal.ZERO;
        }
        return precioCompra.multiply(new BigDecimal(stock));
    }

    /**
     * Obtiene el valor potencial de venta del inventario
     */
    public BigDecimal calcularValorPotencialVenta() {
        if (stock == null || precioVenta == null) {
            return BigDecimal.ZERO;
        }
        return precioVenta.multiply(new BigDecimal(stock));
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public Integer getStockMaximo() { return stockMaximo; }
    public void setStockMaximo(Integer stockMaximo) { this.stockMaximo = stockMaximo; }

    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Boolean getEsServicio() { return esServicio; }
    public void setEsServicio(Boolean esServicio) { this.esServicio = esServicio; }

    public Boolean getManejaInventario() { return manejaInventario; }
    public void setManejaInventario(Boolean manejaInventario) { this.manejaInventario = manejaInventario; }

    public Boolean getPermiteVentaSinStock() { return permiteVentaSinStock; }
    public void setPermiteVentaSinStock(Boolean permiteVentaSinStock) { this.permiteVentaSinStock = permiteVentaSinStock; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public String getDimensiones() { return dimensiones; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }

    public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDateTime fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(String usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }

    public String getUsuarioModificacion() { return usuarioModificacion; }
    public void setUsuarioModificacion(String usuarioModificacion) { this.usuarioModificacion = usuarioModificacion; }

    public List<TransaccionDetalle> getTransaccionDetalles() { return transaccionDetalles; }
    public void setTransaccionDetalles(List<TransaccionDetalle> transaccionDetalles) { this.transaccionDetalles = transaccionDetalles; }

    // equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id) && Objects.equals(codigo, producto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", stock=" + stock +
                ", precioVenta=" + precioVenta +
                ", activo=" + activo +
                '}';
    }
}