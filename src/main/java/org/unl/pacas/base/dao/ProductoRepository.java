package org.unl.pacas.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // ==================== BÚSQUEDAS BÁSICAS ====================
    
    /**
     * Busca productos activos
     */
    List<Producto> findByActivoTrue();
    
    /**
     * Busca productos inactivos
     */
    List<Producto> findByActivoFalse();
    
    /**
     * Busca producto por nombre exacto (activo)
     */
    Optional<Producto> findByNombreAndActivoTrue(String nombre);
    
    /**
     * Busca productos por nombre que contenga el texto (case insensitive)
     */
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    
    /**
     * Busca productos por categoría exacta
     */
    List<Producto> findByCategoriaAndActivoTrue(String categoria);
    
    /**
     * Busca productos por categoría que contenga el texto
     */
    List<Producto> findByCategoriaContainingIgnoreCaseAndActivoTrue(String categoria);
    
    /**
     * Busca productos por código/SKU
     */
    Optional<Producto> findByCodigoAndActivoTrue(String codigo);
    
    /**
     * Busca productos por marca
     */
    List<Producto> findByMarcaContainingIgnoreCaseAndActivoTrue(String marca);

    // ==================== BÚSQUEDAS POR STOCK ====================
    
    /**
     * Busca productos con stock mayor a cero
     */
    List<Producto> findByStockGreaterThanAndActivoTrue(int stock);
    
    /**
     * Busca productos sin stock
     */
    List<Producto> findByStockEqualsAndActivoTrue(int stock);
    
    /**
     * Busca productos con stock bajo (menor al mínimo)
     */
    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo();
    
    /**
     * Busca productos sin stock
     */
    @Query("SELECT p FROM Producto p WHERE p.stock = 0 AND p.activo = true")
    List<Producto> findProductosSinStock();
    
    /**
     * Busca productos con stock entre rangos
     */
    List<Producto> findByStockBetweenAndActivoTrue(int stockMin, int stockMax);

    // ==================== BÚSQUEDAS POR PRECIO ====================
    
    /**
     * Busca productos por rango de precio de venta
     */
    List<Producto> findByPrecioVentaBetweenAndActivoTrue(BigDecimal precioMin, BigDecimal precioMax);
    
    /**
     * Busca productos por precio de venta menor a
     */
    List<Producto> findByPrecioVentaLessThanAndActivoTrue(BigDecimal precio);
    
    /**
     * Busca productos por precio de venta mayor a
     */
    List<Producto> findByPrecioVentaGreaterThanAndActivoTrue(BigDecimal precio);

    // ==================== CONSULTAS PERSONALIZADAS ====================
    
    /**
     * Obtiene todas las categorías únicas
     */
    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.activo = true ORDER BY p.categoria")
    List<String> findAllCategorias();
    
    /**
     * Obtiene todas las marcas únicas
     */
    @Query("SELECT DISTINCT p.marca FROM Producto p WHERE p.activo = true ORDER BY p.marca")
    List<String> findAllMarcas();
    
    /**
     * Busca productos por múltiples criterios
     */
    @Query("SELECT p FROM Producto p WHERE " +
           "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:categoria IS NULL OR LOWER(p.categoria) = LOWER(:categoria)) AND " +
           "(:marca IS NULL OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
           "(:precioMin IS NULL OR p.precioVenta >= :precioMin) AND " +
           "(:precioMax IS NULL OR p.precioVenta <= :precioMax) AND " +
           "(:conStock IS NULL OR (:conStock = true AND p.stock > 0) OR (:conStock = false)) AND " +
           "p.activo = true " +
           "ORDER BY p.nombre")
    List<Producto> buscarProductosConFiltros(
        @Param("nombre") String nombre,
        @Param("categoria") String categoria,
        @Param("marca") String marca,
        @Param("precioMin") BigDecimal precioMin,
        @Param("precioMax") BigDecimal precioMax,
        @Param("conStock") Boolean conStock
    );
    
    /**
     * Busca productos más vendidos
     */
    @Query("SELECT p FROM Producto p " +
           "LEFT JOIN TransaccionDetalle td ON td.producto.id = p.id " +
           "LEFT JOIN Transaccion t ON t.id = td.transaccion.id " +
           "WHERE p.activo = true AND t.tipo = 'VENTA' " +
           "GROUP BY p.id " +
           "ORDER BY SUM(td.cantidad) DESC")
    List<Producto> findProductosMasVendidos();
    
    /**
     * Busca productos menos vendidos
     */
    @Query("SELECT p FROM Producto p " +
           "LEFT JOIN TransaccionDetalle td ON td.producto.id = p.id " +
           "LEFT JOIN Transaccion t ON t.id = td.transaccion.id " +
           "WHERE p.activo = true " +
           "GROUP BY p.id " +
           "ORDER BY COALESCE(SUM(td.cantidad), 0) ASC")
    List<Producto> findProductosMenosVendidos();

    // ==================== ESTADÍSTICAS Y CONTEOS ====================
    
    /**
     * Cuenta productos activos
     */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true")
    long countProductosActivos();
    
    /**
     * Cuenta productos por categoría
     */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria = :categoria AND p.activo = true")
    long countProductosPorCategoria(@Param("categoria") String categoria);
    
    /**
     * Cuenta productos con stock bajo
     */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.stock <= p.stockMinimo AND p.activo = true")
    long countProductosConStockBajo();
    
    /**
     * Cuenta productos sin stock
     */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.stock = 0 AND p.activo = true")
    long countProductosSinStock();

    // ==================== CÁLCULOS FINANCIEROS ====================
    
    /**
     * Calcula el valor total del inventario a precio de compra
     */
    @Query("SELECT COALESCE(SUM(p.precioCompra * p.stock), 0) FROM Producto p WHERE p.activo = true")
    BigDecimal calcularValorInventarioCompra();
    
    /**
     * Calcula el valor total del inventario a precio de venta
     */
    @Query("SELECT COALESCE(SUM(p.precioVenta * p.stock), 0) FROM Producto p WHERE p.activo = true")
    BigDecimal calcularValorInventarioVenta();
    
    /**
     * Calcula la ganancia potencial del inventario
     */
    @Query("SELECT COALESCE(SUM((p.precioVenta - p.precioCompra) * p.stock), 0) FROM Producto p WHERE p.activo = true")
    BigDecimal calcularGananciaPotencialInventario();
    
    /**
     * Calcula el valor del inventario por categoría
     */
    @Query("SELECT COALESCE(SUM(p.precioVenta * p.stock), 0) FROM Producto p WHERE p.categoria = :categoria AND p.activo = true")
    BigDecimal calcularValorInventarioPorCategoria(@Param("categoria") String categoria);

    // ==================== BÚSQUEDAS AVANZADAS ====================
    
    /**
     * Busca productos creados en un rango de fechas
     */
    List<Producto> findByFechaCreacionBetweenAndActivoTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Busca productos actualizados recientemente
     */
    @Query("SELECT p FROM Producto p WHERE p.fechaActualizacion >= :fecha AND p.activo = true ORDER BY p.fechaActualizacion DESC")
    List<Producto> findProductosActualizadosDesde(@Param("fecha") LocalDateTime fecha);
    
    /**
     * Busca productos por margen de ganancia
     */
    @Query("SELECT p FROM Producto p WHERE " +
           "((p.precioVenta - p.precioCompra) / p.precioCompra * 100) >= :margenMinimo AND " +
           "p.activo = true " +
           "ORDER BY ((p.precioVenta - p.precioCompra) / p.precioCompra * 100) DESC")
    List<Producto> findProductosPorMargenGanancia(@Param("margenMinimo") double margenMinimo);
    
    /**
     * Busca productos próximos a vencer (si manejas fechas de vencimiento)
     */
    @Query("SELECT p FROM Producto p WHERE p.fechaVencimiento <= :fecha AND p.activo = true ORDER BY p.fechaVencimiento ASC")
    List<Producto> findProductosProximosAVencer(@Param("fecha") LocalDateTime fecha);

    // ==================== VALIDACIONES ====================
    
    /**
     * Verifica si existe un producto con el mismo nombre (para evitar duplicados)
     */
    boolean existsByNombreAndActivoTrue(String nombre);
    
    /**
     * Verifica si existe un producto con el mismo código
     */
    boolean existsByCodigoAndActivoTrue(String codigo);
    
    /**
     * Verifica si existe un producto con el mismo código excluyendo un ID específico
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.codigo = :codigo AND p.id != :id AND p.activo = true")
    boolean existsByCodigoAndIdNotAndActivoTrue(@Param("codigo") String codigo, @Param("id") Long id);

    // ==================== ORDENAMIENTO PERSONALIZADO ====================
    
    /**
     * Busca productos ordenados por nombre
     */
    List<Producto> findByActivoTrueOrderByNombreAsc();
    
    /**
     * Busca productos ordenados por precio (menor a mayor)
     */
    List<Producto> findByActivoTrueOrderByPrecioVentaAsc();
    
    /**
     * Busca productos ordenados por precio (mayor a menor)
     */
    List<Producto> findByActivoTrueOrderByPrecioVentaDesc();
    
    /**
     * Busca productos ordenados por stock (menor a mayor)
     */
    List<Producto> findByActivoTrueOrderByStockAsc();
    
    /**
     * Busca productos ordenados por fecha de creación (más recientes primero)
     */
    List<Producto> findByActivoTrueOrderByFechaCreacionDesc();

    // ==================== BÚSQUEDAS PARA REPORTES ====================
    
    /**
     * Obtiene resumen de productos por categoría
     */
    @Query("SELECT p.categoria, COUNT(p), SUM(p.stock), AVG(p.precioVenta) FROM Producto p WHERE p.activo = true GROUP BY p.categoria ORDER BY p.categoria")
    List<Object[]> getResumenPorCategoria();
    
    /**
     * Obtiene productos con mayor rotación
     */
    @Query("SELECT p, COALESCE(SUM(td.cantidad), 0) as totalVendido FROM Producto p " +
           "LEFT JOIN TransaccionDetalle td ON td.producto.id = p.id " +
           "LEFT JOIN Transaccion t ON t.id = td.transaccion.id " +
           "WHERE p.activo = true AND (t.tipo = 'VENTA' OR t.tipo IS NULL) " +
           "GROUP BY p.id " +
           "ORDER BY totalVendido DESC")
    List<Object[]> getProductosConRotacion();
}