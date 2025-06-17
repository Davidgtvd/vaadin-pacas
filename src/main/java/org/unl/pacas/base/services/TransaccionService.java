package org.unl.pacas.base.services;

import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.TransaccionDao;
import org.unl.pacas.base.models.Transaccion;
import org.unl.pacas.base.models.TipoTransaccion;
import org.unl.pacas.base.models.MetodoPago;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransaccionService {

    private final TransaccionDao transaccionDao;

    public TransaccionService(TransaccionDao transaccionDao) {
        this.transaccionDao = transaccionDao;
    }

    public List<Transaccion> findAll() {
        return transaccionDao.findAll();
    }

    public Optional<Transaccion> findById(Long id) {
        return Optional.ofNullable(transaccionDao.findById(id));
    }

    public List<Transaccion> findByProductoId(Long productoId) {
        return transaccionDao.findByProducto(productoId);
    }

    public List<Transaccion> findByTipo(TipoTransaccion tipo) {
        return transaccionDao.findByTipo(tipo.name());
    }

    public Transaccion registrarCompra(Long productoId, Integer cantidad, Double precioUnitario, MetodoPago metodoPago, String observaciones) {
        // Implementa lógica de registrar compra
        Transaccion t = new Transaccion();
        t.setFecha(LocalDateTime.now());
        t.setEstado("COMPLETADA");
        t.setTipo(TipoTransaccion.COMPRA.name());
        t.setMetodoPago(metodoPago.name());
        t.setTotal(precioUnitario != null ? java.math.BigDecimal.valueOf(precioUnitario * cantidad) : java.math.BigDecimal.ZERO);
        // Aquí deberías setear producto, cantidad, observaciones, etc.
        return transaccionDao.save(t);
    }

    public Transaccion registrarVenta(Long productoId, Integer cantidad, Double precioUnitario, MetodoPago metodoPago, String observaciones) {
        // Implementa lógica de registrar venta
        Transaccion t = new Transaccion();
        t.setFecha(LocalDateTime.now());
        t.setEstado("COMPLETADA");
        t.setTipo(TipoTransaccion.VENTA.name());
        t.setMetodoPago(metodoPago.name());
        t.setTotal(precioUnitario != null ? java.math.BigDecimal.valueOf(precioUnitario * cantidad) : java.math.BigDecimal.ZERO);
        return transaccionDao.save(t);
    }

    public Transaccion registrarAjuste(Long productoId, Integer cantidad, TipoTransaccion tipoAjuste, String observaciones) {
        // Implementa lógica de registrar ajuste
        Transaccion t = new Transaccion();
        t.setFecha(LocalDateTime.now());
        t.setEstado("COMPLETADA");
        t.setTipo(tipoAjuste.name());
        t.setMetodoPago("N/A");
        t.setTotal(java.math.BigDecimal.ZERO);
        return transaccionDao.save(t);
    }

    public Double calcularVentasHoy() {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        return transaccionDao.findByFecha(inicioDia, LocalDateTime.now()).stream()
                .filter(t -> TipoTransaccion.VENTA.name().equals(t.getTipo()))
                .map(t -> t.getTotal() != null ? t.getTotal().doubleValue() : 0)
                .reduce(0.0, Double::sum);
    }

    public Long contarVentasHoy() {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        return transaccionDao.findByFecha(inicioDia, LocalDateTime.now()).stream()
                .filter(t -> TipoTransaccion.VENTA.name().equals(t.getTipo()))
                .count();
    }

    public Double calcularVentasEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.plusDays(1).atStartOfDay();
        return transaccionDao.findByFecha(inicio, fin).stream()
                .filter(t -> TipoTransaccion.VENTA.name().equals(t.getTipo()))
                .map(t -> t.getTotal() != null ? t.getTotal().doubleValue() : 0)
                .reduce(0.0, Double::sum);
    }

    public Double calcularComprasEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.plusDays(1).atStartOfDay();
        return transaccionDao.findByFecha(inicio, fin).stream()
                .filter(t -> TipoTransaccion.COMPRA.name().equals(t.getTipo()))
                .map(t -> t.getTotal() != null ? t.getTotal().doubleValue() : 0)
                .reduce(0.0, Double::sum);
    }
}