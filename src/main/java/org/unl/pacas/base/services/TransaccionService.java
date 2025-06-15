package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unl.pacas.base.dao.TransaccionRepository;
import org.unl.pacas.base.models.Transaccion;
import org.unl.pacas.base.models.Producto;
import org.unl.pacas.base.models.TipoTransaccion;
import org.unl.pacas.base.models.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ProductoService productoService;

    // ==================== MÉTODOS BÁSICOS CRUD ====================

    public List<Transaccion> findAll() {
        return transaccionRepository.findAllByOrderByFechaDesc();
    }

    public Optional<Transaccion> findById(Long id) {
        return transaccionRepository.findById(id);
    }

    public Transaccion save(Transaccion transaccion) {
        if (transaccion.getFecha() == null) {
            transaccion.setFecha(LocalDateTime.now());
        }
        if (transaccion.getEstado() == null) {
            transaccion.setEstado("PENDIENTE");
        }
        return transaccionRepository.save(transaccion);
    }

    public Transaccion update(Transaccion transaccion) {
        if (transaccion.getId() == null) {
            throw new RuntimeException("ID de transacción requerido para actualizar");
        }
        if (!existeTransaccion(transaccion.getId())) {
            throw new RuntimeException("Transacción no encontrada");
        }
        transaccion.setFechaActualizacion(LocalDateTime.now());
        return transaccionRepository.save(transaccion);
    }

    public void deleteById(Long id) {
        transaccionRepository.deleteById(id);
    }

    public boolean existeTransaccion(Long id) {
        return transaccionRepository.existsById(id);
    }

    // ==================== BÚSQUEDAS POR PRODUCTO ====================

    public List<Transaccion> findByProductoId(Long productoId) {
        Optional<Producto> productoOpt = productoService.findById(productoId);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }
        Producto producto = productoOpt.get();
        return transaccionRepository.findByProductoOrderByFechaDesc(producto);
    }

    // ==================== BÚSQUEDAS POR TIPO Y ESTADO ====================

    public List<Transaccion> findByTipo(TipoTransaccion tipo) {
        return transaccionRepository.findByTipoOrderByFechaDesc(tipo.name());
    }

    public List<Transaccion> findByMetodoPago(MetodoPago metodoPago) {
        return transaccionRepository.findByMetodoPagoOrderByFechaDesc(metodoPago.name());
    }

    public List<Transaccion> findByEstado(String estado) {
        return transaccionRepository.findByEstadoOrderByFechaDesc(estado);
    }

    public List<Transaccion> findVentas() {
        return transaccionRepository.findVentas();
    }

    public List<Transaccion> findCompras() {
        return transaccionRepository.findCompras();
    }

    public List<Transaccion> findDevoluciones() {
        return transaccionRepository.findDevoluciones();
    }

    // ==================== BÚSQUEDAS POR FECHA ====================

    public List<Transaccion> findByFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return transaccionRepository.findByFechaBetweenOrderByFechaDesc(fechaInicio, fechaFin);
    }

    public List<Transaccion> findTransaccionesDelDia(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(23, 59, 59, 999999999);
        return transaccionRepository.findByFecha(inicio, fin);
    }

    public List<Transaccion> findTransaccionesHoy() {
        LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finHoy = inicioHoy.plusDays(1);
        return transaccionRepository.findTransaccionesHoy(inicioHoy, finHoy);
    }

    public List<Transaccion> findTransaccionesSemanaActual() {
        LocalDateTime inicioSemana = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finSemana = inicioSemana.plusWeeks(1);
        return transaccionRepository.findTransaccionesSemanaActual(inicioSemana, finSemana);
    }

    public List<Transaccion> findTransaccionesMesActual() {
        LocalDateTime inicioMes = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finMes = inicioMes.plusMonths(1);
        return transaccionRepository.findTransaccionesMesActual(inicioMes, finMes);
    }

    public List<Transaccion> findTransaccionesAnioActual() {
        LocalDateTime inicioAnio = LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finAnio = inicioAnio.plusYears(1);
        return transaccionRepository.findTransaccionesAnioActual(inicioAnio, finAnio);
    }

    // ==================== BÚSQUEDAS POR MONTO ====================

    public List<Transaccion> findByRangoTotal(BigDecimal montoMinimo, BigDecimal montoMaximo) {
        return transaccionRepository.findByTotalBetweenOrderByFechaDesc(montoMinimo, montoMaximo);
    }

    public List<Transaccion> findTransaccionesAltoValor(BigDecimal montoMinimo) {
        return transaccionRepository.findTransaccionesAltoValor(montoMinimo);
    }

    // ==================== OPERACIONES DE NEGOCIO ====================

    @Transactional
    public Transaccion registrarCompra(Long productoId, Integer cantidad, Double precioUnitario, 
                                      MetodoPago metodoPago, String observaciones) {
        Optional<Producto> productoOpt = productoService.findById(productoId);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }
        Producto producto = productoOpt.get();

        Transaccion transaccion = new Transaccion();
        transaccion.setProducto(producto);
        transaccion.setTipo(TipoTransaccion.COMPRA.name());
        transaccion.setMetodoPago(metodoPago.name());
        transaccion.setTotal(BigDecimal.valueOf(cantidad * precioUnitario));
        transaccion.setObservaciones("COMPRA - Producto: " + producto.getNombre() + 
                                   " - Cantidad: " + cantidad + 
                                   " - Precio: $" + precioUnitario + 
                                   (observaciones != null ? " - " + observaciones : ""));
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setEstado("COMPLETADA");

        // Actualizar stock del producto
        productoService.incrementarStock(productoId, cantidad);

        return transaccionRepository.save(transaccion);
    }

    @Transactional
    public Transaccion registrarVenta(Long productoId, Integer cantidad, Double precioUnitario, 
                                     MetodoPago metodoPago, String observaciones) {
        Optional<Producto> productoOpt = productoService.findById(productoId);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }
        Producto producto = productoOpt.get();

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setProducto(producto);
        transaccion.setTipo(TipoTransaccion.VENTA.name());
        transaccion.setMetodoPago(metodoPago.name());
        transaccion.setTotal(BigDecimal.valueOf(cantidad * precioUnitario));
        transaccion.setObservaciones("VENTA - Producto: " + producto.getNombre() + 
                                   " - Cantidad: " + cantidad + 
                                   " - Precio: $" + precioUnitario + 
                                   " - Método: " + metodoPago.name() +
                                   (observaciones != null ? " - " + observaciones : ""));
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setEstado("COMPLETADA");

        // Actualizar stock del producto
        productoService.decrementarStock(productoId, cantidad);

        return transaccionRepository.save(transaccion);
    }

    @Transactional
    public Transaccion registrarAjuste(Long productoId, Integer cantidad, TipoTransaccion tipoAjuste, 
                                      String observaciones) {
        if (tipoAjuste != TipoTransaccion.AJUSTE_INVENTARIO) {
            throw new RuntimeException("Tipo de ajuste inválido. Use AJUSTE_INVENTARIO");
        }

        Optional<Producto> productoOpt = productoService.findById(productoId);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }
        Producto producto = productoOpt.get();

        Transaccion transaccion = new Transaccion();
        transaccion.setProducto(producto);
        transaccion.setTipo(tipoAjuste.name());
        transaccion.setTotal(BigDecimal.ZERO);
        transaccion.setObservaciones("AJUSTE - Producto: " + producto.getNombre() + 
                                   " - Cantidad: " + cantidad + 
                                   (observaciones != null ? " - " + observaciones : ""));
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setEstado("COMPLETADA");

        // Ajustar stock
        if (cantidad > 0) {
            productoService.incrementarStock(productoId, cantidad);
        } else {
            productoService.decrementarStock(productoId, Math.abs(cantidad));
        }

        return transaccionRepository.save(transaccion);
    }

    @Transactional
    public Transaccion completarTransaccion(Long transaccionId) {
        Optional<Transaccion> transaccionOpt = findById(transaccionId);
        if (transaccionOpt.isEmpty()) {
            throw new RuntimeException("Transacción no encontrada");
        }
        Transaccion transaccion = transaccionOpt.get();
        transaccion.setEstado("COMPLETADA");
        transaccion.setFechaActualizacion(LocalDateTime.now());
        return transaccionRepository.save(transaccion);
    }

    @Transactional
    public Transaccion cancelarTransaccion(Long transaccionId, String motivo) {
        Optional<Transaccion> transaccionOpt = findById(transaccionId);
        if (transaccionOpt.isEmpty()) {
            throw new RuntimeException("Transacción no encontrada");
        }
        Transaccion transaccion = transaccionOpt.get();
        transaccion.setEstado("CANCELADA");
        transaccion.setObservaciones(transaccion.getObservaciones() + " - CANCELADA: " + motivo);
        transaccion.setFechaActualizacion(LocalDateTime.now());
        return transaccionRepository.save(transaccion);
    }

    // ==================== CÁLCULOS Y ESTADÍSTICAS ====================

    public Double calcularVentasHoy() {
        try {
            LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime finHoy = inicioHoy.plusDays(1);
            BigDecimal ventas = transaccionRepository.calcularVentasHoy(inicioHoy, finHoy);
            return ventas != null ? ventas.doubleValue() : 0.0;
        } catch (Exception e) {
            return calcularVentasDelDia(LocalDate.now());
        }
    }

    public Long contarVentasHoy() {
        try {
            LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime finHoy = inicioHoy.plusDays(1);
            return transaccionRepository.contarVentasHoy(inicioHoy, finHoy);
        } catch (Exception e) {
            return (long) findTransaccionesDelDia(LocalDate.now()).stream()
                    .filter(t -> TipoTransaccion.VENTA.name().equals(t.getTipo()))
                    .count();
        }
    }

    public Double calcularVentasSemana() {
        LocalDateTime inicioSemana = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finSemana = inicioSemana.plusWeeks(1);
        BigDecimal ventas = transaccionRepository.calcularVentasSemana(inicioSemana, finSemana);
        return ventas != null ? ventas.doubleValue() : 0.0;
    }

    public Double calcularVentasMes() {
        LocalDateTime inicioMes = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finMes = inicioMes.plusMonths(1);
        BigDecimal ventas = transaccionRepository.calcularVentasMes(inicioMes, finMes);
        return ventas != null ? ventas.doubleValue() : 0.0;
    }

    public Double calcularVentasAnio() {
        LocalDateTime inicioAnio = LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finAnio = inicioAnio.plusYears(1);
        BigDecimal ventas = transaccionRepository.calcularVentasAnio(inicioAnio, finAnio);
        return ventas != null ? ventas.doubleValue() : 0.0;
    }

    public Double calcularVentasEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);
        try {
            BigDecimal ventas = transaccionRepository.calcularTotalPorTipoYFecha(TipoTransaccion.VENTA.name(), inicio, fin);
            return ventas != null ? ventas.doubleValue() : 0.0;
        } catch (Exception e) {
            return transaccionRepository.findByFechaBetweenOrderByFechaDesc(inicio, fin).stream()
                    .filter(t -> TipoTransaccion.VENTA.name().equals(t.getTipo()) && "COMPLETADA".equals(t.getEstado()))
                    .mapToDouble(t -> t.getTotal().doubleValue())
                    .sum();
        }
    }

    public Double calcularComprasEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);
        try {
            BigDecimal compras = transaccionRepository.calcularTotalPorTipoYFecha(TipoTransaccion.COMPRA.name(), inicio, fin);
            return compras != null ? compras.doubleValue() : 0.0;
        } catch (Exception e) {
            return transaccionRepository.findByFechaBetweenOrderByFechaDesc(inicio, fin).stream()
                    .filter(t -> TipoTransaccion.COMPRA.name().equals(t.getTipo()) && "COMPLETADA".equals(t.getEstado()))
                    .mapToDouble(t -> t.getTotal().doubleValue())
                    .sum();
        }
    }

    public Double calcularPromedioVentas() {
        try {
            BigDecimal promedio = transaccionRepository.calcularTicketPromedio();
            return promedio != null ? promedio.doubleValue() : 0.0;
        } catch (Exception e) {
            List<Transaccion> ventas = transaccionRepository.findVentas();
            if (ventas.isEmpty()) return 0.0;
            double suma = ventas.stream()
                    .filter(t -> "COMPLETADA".equals(t.getEstado()))
                    .mapToDouble(t -> t.getTotal().doubleValue())
                    .sum();
            return suma / ventas.size();
        }
    }

    // ==================== CONTEOS Y ESTADÍSTICAS ====================

    public Long contarTransaccionesPorTipo(TipoTransaccion tipo) {
        return transaccionRepository.countByTipo(tipo.name());
    }

    public Long contarTransaccionesPorEstado(String estado) {
        return transaccionRepository.countByEstado(estado);
    }

    public Long contarTransaccionesHoy() {
        LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finHoy = inicioHoy.plusDays(1);
        return transaccionRepository.countTransaccionesHoy(inicioHoy, finHoy);
    }

    public Long contarTransaccionesPendientes() {
        return transaccionRepository.countTransaccionesPendientes();
    }

    // ==================== REPORTES Y ANÁLISIS ====================

    public List<Object[]> getResumenVentasPorDia(int ultimosDias) {
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(ultimosDias);
        return transaccionRepository.getResumenVentasPorDia(fechaInicio);
    }

    public List<Object[]> getResumenPorMetodoPago() {
        return transaccionRepository.getResumenPorMetodoPago();
    }

    public List<Object[]> getMejoresClientes() {
        return transaccionRepository.getMejoresClientes();
    }

    public List<Object[]> getProductosMasVendidos() {
        return transaccionRepository.getProductosMasVendidos();
    }

    public Object[] getEstadisticasDelDia() {
        LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finHoy = inicioHoy.plusDays(1);
        return transaccionRepository.getEstadisticasDelDia(inicioHoy, finHoy);
    }

    // ==================== BÚSQUEDAS AVANZADAS ====================

    public List<Transaccion> buscarTransaccionesConFiltros(String tipo, String estado, String metodoPago, 
                                                          Long cuentaId, LocalDateTime fechaInicio, 
                                                          LocalDateTime fechaFin, BigDecimal montoMin, 
                                                          BigDecimal montoMax) {
        return transaccionRepository.buscarTransaccionesConFiltros(tipo, estado, metodoPago, cuentaId, 
                                                                  fechaInicio, fechaFin, montoMin, montoMax);
    }

    public List<Transaccion> findByCliente(String cliente) {
        return transaccionRepository.findByCliente(cliente);
    }

    public List<Transaccion> buscarPorObservaciones(String texto) {
        return transaccionRepository.findAll().stream()
                .filter(t -> t.getObservaciones() != null && 
                           t.getObservaciones().toLowerCase().contains(texto.toLowerCase()))
                .toList();
    }

    public List<Transaccion> findTransaccionesRecientes(int limite) {
        List<Transaccion> todas = transaccionRepository.findUltimasTransacciones();
        if (todas.size() <= limite) {
            return todas;
        }
        return todas.subList(0, limite);
    }

    // ==================== VALIDACIONES ====================

    public boolean validarTransaccion(Transaccion transaccion) {
        if (transaccion == null) return false;
        if (transaccion.getTipo() == null || transaccion.getTipo().trim().isEmpty()) return false;
        if (transaccion.getTotal() == null || transaccion.getTotal().compareTo(BigDecimal.ZERO) < 0) return false;
        return true;
    }

    public boolean existeNumeroFactura(String numeroFactura) {
        return transaccionRepository.existsByNumeroFactura(numeroFactura);
    }

    public boolean existeNumeroFacturaExcluyendoId(String numeroFactura, Long id) {
        return transaccionRepository.existsByNumeroFacturaAndIdNot(numeroFactura, id);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    public Transaccion crearTransaccionBasica(TipoTransaccion tipo, BigDecimal total, String observaciones) {
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo(tipo.name());
        transaccion.setTotal(total);
        transaccion.setObservaciones(observaciones);
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setEstado("PENDIENTE");
        return transaccionRepository.save(transaccion);
    }

    private Double calcularVentasDelDia(LocalDate fecha) {
        return findTransaccionesDelDia(fecha).stream()
                .filter(t -> TipoTransaccion.VENTA.name().equals(t.getTipo()) && "COMPLETADA".equals(t.getEstado()))
                .mapToDouble(t -> t.getTotal().doubleValue())
                .sum();
    }
}