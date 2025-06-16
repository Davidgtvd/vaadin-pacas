package org.unl.pacas.base.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @NotBlank(message = "El código del producto es obligatorio")
    @Size(max = 50, message = "El código no puede tener más de 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "El precio de costo es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de costo debe ser mayor a 0")
    @Column(name = "precio_costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCosto;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @NotNull(message = "La categoría es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CategoriaProducto categoria;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoProducto estado;

    @Size(max = 20, message = "La unidad de medida no puede tener más de 20 caracteres")
    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    @Size(max = 100, message = "La marca no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String marca;

    @Size(max = 100, message = "El modelo no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String modelo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Size(max = 200, message = "La ubicación no puede tener más de 200 caracteres")
    @Column(length = 200)
    private String ubicacion;

    @Size(max = 100, message = "El proveedor no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String proveedor;

    // Constructores
    public Producto() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = EstadoProducto.ACTIVO;
        this.stock = 0;
        this.stockMinimo = 0;
    }

    public Producto(String nombre, String codigo, BigDecimal precio, BigDecimal precioCosto, 
                CategoriaProducto categoria) {
        this();
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.precioCosto = precioCosto;
        this.categoria = categoria;
    }

    public Producto(String nombre, String descripcion, String codigo, BigDecimal precio, 
                BigDecimal precioCosto, Integer stock, CategoriaProducto categoria) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.precio = precio;
        this.precioCosto = precioCosto;
        this.stock = stock;
        this.categoria = categoria;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Métodos utilitarios para las vistas
    public String getNombreCompleto() {
        return nombre + " (" + codigo + ")";
    }

    public String getDisplayName() {
        return getNombreCompleto() + " - $" + precio;
    }

    public BigDecimal getMargenGanancia() {
        if (precio != null && precioCosto != null && precioCosto.compareTo(BigDecimal.ZERO) > 0) {
            return precio.subtract(precioCosto);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getPorcentajeMargen() {
        if (precio != null && precioCosto != null && precioCosto.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal margen = getMargenGanancia();
            return margen.divide(precioCosto, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getValorTotalStock() {
        if (stock != null && precioCosto != null) {
            return precioCosto.multiply(new BigDecimal(stock));
        }
        return BigDecimal.ZERO;
    }

    public boolean isStockBajo() {
        if (stock != null && stockMinimo != null) {
            return stock <= stockMinimo;
        }
        return false;
    }

    public boolean isDisponible() {
        return estado == EstadoProducto.ACTIVO && stock != null && stock > 0;
    }

    public String getEstadoStock() {
        if (stock == null) return "Sin definir";
        if (stock == 0) return "Sin stock";
        if (isStockBajo()) return "Stock bajo";
        return "Stock normal";
    }

    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        info.append(getNombreCompleto());
        if (marca != null && !marca.trim().isEmpty()) {
            info.append(" - ").append(marca);
        }
        if (modelo != null && !modelo.trim().isEmpty()) {
            info.append(" ").append(modelo);
        }
        return info.toString();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}