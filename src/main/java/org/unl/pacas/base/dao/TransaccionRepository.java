package org.unl.pacas.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Transaccion;
import org.unl.pacas.base.models.Producto;
import org.unl.pacas.base.models.Cuenta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    // ==================== BÚSQUEDAS BÁSICAS ====================
    
    /**
     * Busca transacciones por producto ordenadas por fecha descendente
     */
    List<Transaccion> findByProductoOrderByFechaDesc(Producto producto);
    
    /**
     * Busca transacciones por tipo ordenadas por fecha descendente
     */
    List<Transaccion> findByTipoOrderByFechaDesc(String tipo);
    
    /**
     * Busca transacciones por método de pago ordenadas por fecha descendente
     */
    List<Transaccion> findByMetodoPagoOrderByFechaDesc(String metodoPago);
    
    /**
     * Busca transacciones por estado
     */
    List<Transaccion> findByEstadoOrderByFechaDesc(String estado);
    
    /**
     * Busca transacciones por usuario/cuenta
     */
    List<Transaccion> findByCuentaIdOrderByFechaDesc(Long cuentaId);
    
    /**
     * Busca transacciones por número de factura
     */
    Optional<Transaccion> findByNumeroFactura(String numeroFactura);

    // ==================== BÚSQUEDAS POR FECHA (CORREGIDAS) ====================
    
    /**
     * Busca transacciones en un rango de fechas
     */
    List<Transaccion> findByFechaBetweenOrderByFechaDesc(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Busca transacciones de hoy
     */
    @Query("SELECT t FROM Transaccion t WHERE t.fecha >= :inicioHoy AND t.fecha < :finHoy ORDER BY t.fecha DESC")
    List<Transaccion> findTransaccionesHoy(@Param("inicioHoy") LocalDateTime inicioHoy, @Param("finHoy") LocalDateTime finHoy);
    
    /**
     * Busca transacciones de esta semana
     */
    @Query("SELECT t FROM Transaccion t WHERE t.fecha >= :inicioSemana AND t.fecha < :finSemana ORDER BY t.fecha DESC")
    List<Transaccion> findTransaccionesSemanaActual(@Param("inicioSemana") LocalDateTime inicioSemana, @Param("finSemana") LocalDateTime finSemana);
    
    /**
     * Busca transacciones de este mes
     */
    @Query("SELECT t FROM Transaccion t WHERE t.fecha >= :inicioMes AND t.fecha < :finMes ORDER BY t.fecha DESC")
    List<Transaccion> findTransaccionesMesActual(@Param("inicioMes") LocalDateTime inicioMes, @Param("finMes") LocalDateTime finMes);
    
    /**
     * Busca transacciones de este año
     */
    @Query("SELECT t FROM Transaccion t WHERE t.fecha >= :inicioAnio AND t.fecha < :finAnio ORDER BY t.fecha DESC")
    List<Transaccion> findTransaccionesAnioActual(@Param("inicioAnio") LocalDateTime inicioAnio, @Param("finAnio") LocalDateTime finAnio);
    
    /**
     * Busca transacciones por fecha específica
     */
    @Query("SELECT t FROM Transaccion t WHERE t.fecha >= :inicioFecha AND t.fecha < :finFecha ORDER BY t.fecha DESC")
    List<Transaccion> findByFecha(@Param("inicioFecha") LocalDateTime inicioFecha, @Param("finFecha") LocalDateTime finFecha);

    // ==================== BÚSQUEDAS POR TIPO Y ESTADO ====================
    
    /**
     * Busca ventas (transacciones de tipo VENTA)
     */
    @Query("SELECT t FROM Transaccion t WHERE t.tipo = 'VENTA' ORDER BY t.fecha DESC")
    List<Transaccion> findVentas();
    
    /**
     * Busca compras (transacciones de tipo COMPRA)
     */
    @Query("SELECT t FROM Transaccion t WHERE t.tipo = 'COMPRA' ORDER BY t.fecha DESC")
    List<Transaccion> findCompras();
    
    /**
     * Busca devoluciones
     */
    @Query("SELECT t FROM Transaccion t WHERE t.tipo = 'DEVOLUCION' ORDER BY t.fecha DESC")
    List<Transaccion> findDevoluciones();
    
    /**
     * Busca transacciones pendientes
     */
    @Query("SELECT t FROM Transaccion t WHERE t.estado = 'PENDIENTE' ORDER BY t.fecha DESC")
    List<Transaccion> findTransaccionesPendientes();
    
    /**
     * Busca transacciones canceladas
     */
    @Query("SELECT t FROM Transaccion t WHERE t.estado = 'CANCELADA' ORDER BY t.fecha DESC")
    List<Transaccion> findTransaccionesCanceladas();

    // ==================== BÚSQUEDAS POR MONTO ====================
    
    /**
     * Busca transacciones por rango de monto
     */
    List<Transaccion> findByTotalBetweenOrderByFechaDesc(BigDecimal montoMin, BigDecimal montoMax);
    
    /**
     * Busca transacciones mayores a un monto
     */
    List<Transaccion> findByTotalGreaterThanOrderByTotalDesc(BigDecimal monto);
    
    /**
     * Busca transacciones menores a un monto
     */
    List<Transaccion> findByTotalLessThanOrderByTotalDesc(BigDecimal monto);

    // ==================== CONSULTAS PERSONALIZADAS AVANZADAS ====================
    
    /**
     * Busca transacciones con múltiples filtros
     */
    @Query("SELECT t FROM Transaccion t WHERE " +
           "(:tipo IS NULL OR t.tipo = :tipo) AND " +
           "(:estado IS NULL OR t.estado = :estado) AND " +
           "(:metodoPago IS NULL OR t.metodoPago = :metodoPago) AND " +
           "(:cuentaId IS NULL OR t.cuenta.id = :cuentaId) AND " +
           "(:fechaInicio IS NULL OR t.fecha >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR t.fecha <= :fechaFin) AND " +
           "(:montoMin IS NULL OR t.total >= :montoMin) AND " +
           "(:montoMax IS NULL OR t.total <= :montoMax) " +
           "ORDER BY t.fecha DESC")
    List<Transaccion> buscarTransaccionesConFiltros(
        @Param("tipo") String tipo,
        @Param("estado") String estado,
        @Param("metodoPago") String metodoPago,
        @Param("cuentaId") Long cuentaId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin,
        @Param("montoMin") BigDecimal montoMin,
        @Param("montoMax") BigDecimal montoMax
    );
    
    /**
     * Busca transacciones por cliente (nombre o identificación)
     */
    @Query("SELECT t FROM Transaccion t WHERE " +
           "LOWER(t.nombreCliente) LIKE LOWER(CONCAT('%', :cliente, '%')) OR " +
           "t.identificacionCliente LIKE CONCAT('%', :cliente, '%') " +
           "ORDER BY t.fecha DESC")
    List<Transaccion> findByCliente(@Param("cliente") String cliente);
    
    /**
     * Busca transacciones que incluyen un producto específico
     */
    @Query("SELECT DISTINCT t FROM Transaccion t " +
           "JOIN t.detalles td " +
           "WHERE td.producto.id = :productoId " +
           "ORDER BY t.fecha DESC")
    List<Transaccion> findTransaccionesConProducto(@Param("productoId") Long productoId);
    
    /**
     * Busca transacciones que incluyen productos de una categoría
     */
    @Query("SELECT DISTINCT t FROM Transaccion t " +
           "JOIN t.detalles td " +
           "WHERE td.producto.categoria = :categoria " +
           "ORDER BY t.fecha DESC")
    List<Transaccion> findTransaccionesPorCategoriaProducto(@Param("categoria") String categoria);

    // ==================== CÁLCULOS Y ESTADÍSTICAS (CORREGIDAS) ====================
    
    /**
     * Calcula las ventas de hoy
     */
    @Query("SELECT COALESCE(SUM(t.total), 0) FROM Transaccion t WHERE t.fecha >= :inicioHoy AND t.fecha < :finHoy AND t.tipo = 'VENTA' AND t.estado = 'COMPLETADA'")
    BigDecimal calcularVentasHoy(@Param("inicioHoy") LocalDateTime inicioHoy, @Param("finHoy") LocalDateTime finHoy);
    
    /**
     * Cuenta las ventas de hoy
     */
    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.fecha >= :inicioHoy AND t.fecha < :finHoy AND t.tipo = 'VENTA' AND t.estado = 'COMPLETADA'")
    long contarVentasHoy(@Param("inicioHoy") LocalDateTime inicioHoy, @Param("finHoy") LocalDateTime finHoy);
    
    /**
     * Calcula las ventas de esta semana
     */
    @Query("SELECT COALESCE(SUM(t.total), 0) FROM Transaccion t WHERE t.fecha >= :inicioSemana AND t.fecha < :finSemana AND t.tipo = 'VENTA' AND t.estado = 'COMPLETADA'")
    BigDecimal calcularVentasSemana(@Param("inicioSemana") LocalDateTime inicioSemana, @Param("finSemana") LocalDateTime finSemana);
    
    /**
     * Calcula las ventas de este mes
     */
    @Query("SELECT COALESCE(SUM(t.total), 0) FROM Transaccion t WHERE t.fecha >= :inicioMes AND t.fecha < :finMes AND t.tipo = 'VENTA' AND t.estado = 'COMPLETADA'")
    BigDecimal calcularVentasMes(@Param("inicioMes") LocalDateTime inicioMes, @Param("finMes") LocalDateTime finMes);
    
    /**
     * Calcula las ventas de este año
     */
    @Query("SELECT COALESCE(SUM(t.total), 0) FROM Transaccion t WHERE t.fecha >= :inicioAnio AND t.fecha < :finAnio AND t.tipo = 'VENTA' AND t.estado = 'COMPLETADA'")
    BigDecimal calcularVentasAnio(@Param("inicioAnio") LocalDateTime inicioAnio, @Param("finAnio") LocalDateTime finAnio);
    
    /**
     * Calcula el total por tipo y rango de fechas
     */
    @Query("SELECT COALESCE(SUM(t.total), 0) FROM Transaccion t WHERE t.tipo = :tipo AND t.fecha BETWEEN :fechaInicio AND :fechaFin AND t.estado = 'COMPLETADA'")
    BigDecimal calcularTotalPorTipoYFecha(@Param("tipo") String tipo, @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Calcula el promedio de ventas por día (simplificado)
     */
    @Query("SELECT AVG(t.total) FROM Transaccion t WHERE t.tipo = 'VENTA' AND t.estado = 'COMPLETADA'")
    BigDecimal calcularPromedioVentasDiarias();
    
    /**
     * Obtiene el ticket promedio
     */
    @Query("SELECT AVG(t.total) FROM Transaccion t WHERE t.tipo = 'VENTA' AND t.estado = 'COMPLETADA'")
    BigDecimal calcularTicketPromedio();

    // ==================== CONTEOS Y ESTADÍSTICAS ====================
    
    /**
     * Cuenta transacciones por tipo
     */
    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.tipo = :tipo")
    long countByTipo(@Param("tipo") String tipo);
    
    /**
     * Cuenta transacciones por estado
     */
    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.estado = :estado")
    long countByEstado(@Param("estado") String estado);
    
    /**
     * Cuenta transacciones por método de pago
     */
    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.metodoPago = :metodoPago")
    long countByMetodoPago(@Param("metodoPago") String metodoPago);
    
    /**
     * Cuenta transacciones de hoy
     */
    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.fecha >= :inicioHoy AND t.fecha < :finHoy")
    long countTransaccionesHoy(@Param("inicioHoy") LocalDateTime inicioHoy, @Param("finHoy") LocalDateTime finHoy);
    
    /**
     * Cuenta transacciones pendientes
     */
    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.estado = 'PENDIENTE'")
    long countTransaccionesPendientes();

    // ==================== REPORTES Y ANÁLISIS (SIMPLIFICADOS) ====================
    
    /**
     * Obtiene resumen de ventas por día (últimos 30 días)
     */
    @Query("SELECT t.fecha, COUNT(t), SUM(t.total) FROM Transaccion t WHERE t.tipo = 'VENTA' AND t.estado = 'COMPLETADA' AND t.fecha >= :fechaInicio GROUP BY t.fecha ORDER BY t.fecha DESC")
    List<Object[]> getResumenVentasPorDia(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    /**
     * Obtiene resumen por método de pago
     */
    @Query("SELECT t.metodoPago, COUNT(t), SUM(t.total) FROM Transaccion t WHERE t.tipo = 'VENTA' AND t.estado = 'COMPLETADA' GROUP BY t.metodoPago ORDER BY SUM(t.total) DESC")
    List<Object[]> getResumenPorMetodoPago();
    
    /**
     * Obtiene los mejores clientes por monto
     */
    @Query("SELECT t.nombreCliente, t.identificacionCliente, COUNT(t), SUM(t.total) FROM Transaccion t WHERE t.tipo = 'VENTA' AND t.estado = 'COMPLETADA' AND t.nombreCliente IS NOT NULL GROUP BY t.nombreCliente, t.identificacionCliente ORDER BY SUM(t.total) DESC")
    List<Object[]> getMejoresClientes();
    
    /**
     * Obtiene productos más vendidos
     */
    @Query("SELECT td.producto.nombre, SUM(td.cantidad), SUM(td.subtotal) FROM Transaccion t JOIN t.detalles td WHERE t.tipo = 'VENTA' AND t.estado = 'COMPLETADA' GROUP BY td.producto.id, td.producto.nombre ORDER BY SUM(td.cantidad) DESC")
    List<Object[]> getProductosMasVendidos();
    
    /**
     * Obtiene ventas por hora del día (hoy)
     */
    @Query("SELECT EXTRACT(HOUR FROM t.fecha), COUNT(t), SUM(t.total) FROM Transaccion t WHERE t.tipo = 'VENTA' AND t.estado = 'COMPLETADA' AND t.fecha >= :inicioHoy AND t.fecha < :finHoy GROUP BY EXTRACT(HOUR FROM t.fecha) ORDER BY EXTRACT(HOUR FROM t.fecha)")
    List<Object[]> getVentasPorHora(@Param("inicioHoy") LocalDateTime inicioHoy, @Param("finHoy") LocalDateTime finHoy);
    
    /**
     * Obtiene el total de ventas para un rango de fechas específico
     */
    @Query("SELECT COALESCE(SUM(t.total), 0) FROM Transaccion t WHERE t.tipo = 'VENTA' AND t.estado = 'COMPLETADA' AND t.fecha >= :fechaInicio AND t.fecha < :fechaFin")
    BigDecimal getTotalVentasPorRango(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    // ==================== VALIDACIONES ====================
    
    /**
     * Verifica si existe una transacción con el mismo número de factura
     */
    boolean existsByNumeroFactura(String numeroFactura);
    
    /**
     * Verifica si existe una transacción con el mismo número excluyendo un ID
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Transaccion t WHERE t.numeroFactura = :numeroFactura AND t.id != :id")
    boolean existsByNumeroFacturaAndIdNot(@Param("numeroFactura") String numeroFactura, @Param("id") Long id);

    // ==================== BÚSQUEDAS PARA DASHBOARD ====================
    
    /**
     * Obtiene las últimas transacciones
     */
    @Query("SELECT t FROM Transaccion t ORDER BY t.fecha DESC")
    List<Transaccion> findUltimasTransacciones();
    
    /**
     * Obtiene transacciones de alto valor
     */
    @Query("SELECT t FROM Transaccion t WHERE t.total >= :montoMinimo ORDER BY t.total DESC")
    List<Transaccion> findTransaccionesAltoValor(@Param("montoMinimo") BigDecimal montoMinimo);
    
    /**
     * Obtiene estadísticas rápidas del día
     */
    @Query("SELECT " +
           "COUNT(CASE WHEN t.tipo = 'VENTA' THEN 1 END) as ventas, " +
           "COUNT(CASE WHEN t.tipo = 'COMPRA' THEN 1 END) as compras, " +
           "COUNT(CASE WHEN t.tipo = 'DEVOLUCION' THEN 1 END) as devoluciones, " +
           "COALESCE(SUM(CASE WHEN t.tipo = 'VENTA' AND t.estado = 'COMPLETADA' THEN t.total ELSE 0 END), 0) as totalVentas " +
           "FROM Transaccion t WHERE t.fecha >= :inicioHoy AND t.fecha < :finHoy")
    Object[] getEstadisticasDelDia(@Param("inicioHoy") LocalDateTime inicioHoy, @Param("finHoy") LocalDateTime finHoy);

    // ==================== BÚSQUEDAS PARA AUDITORÍA ====================
    
    /**
     * Busca transacciones modificadas recientemente
     */
    @Query("SELECT t FROM Transaccion t WHERE t.fechaActualizacion >= :fecha ORDER BY t.fechaActualizacion DESC")
    List<Transaccion> findTransaccionesModificadasDesde(@Param("fecha") LocalDateTime fecha);
    
    /**
     * Busca transacciones por usuario que las creó
     */
    List<Transaccion> findByCuentaOrderByFechaDesc(Cuenta cuenta);
    
    /**
     * Busca transacciones sospechosas (montos muy altos o muy bajos)
     */
    @Query("SELECT t FROM Transaccion t WHERE t.total > :montoAlto OR t.total < :montoBajo ORDER BY t.fecha DESC")
    List<Transaccion> findTransaccionesSospechosas(@Param("montoAlto") BigDecimal montoAlto, @Param("montoBajo") BigDecimal montoBajo);

    // ==================== ORDENAMIENTO PERSONALIZADO ====================
    
    /**
     * Busca todas las transacciones ordenadas por fecha descendente
     */
    List<Transaccion> findAllByOrderByFechaDesc();
    
    /**
     * Busca todas las transacciones ordenadas por monto descendente
     */
    List<Transaccion> findAllByOrderByTotalDesc();
    
    /**
     * Busca transacciones ordenadas por estado y fecha
     */
    List<Transaccion> findAllByOrderByEstadoAscFechaDesc();
}