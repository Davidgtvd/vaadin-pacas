package org.unl.pacas.base.endpoint;

import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.PagoDao;
import org.unl.pacas.base.models.MetodoPago;
import org.unl.pacas.base.models.Pago;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagoServices {

    private final PagoDao pagoDao;

    public PagoServices(PagoDao pagoDao) {
        this.pagoDao = pagoDao;
    }

    public List<Pago> findAll() {
        return pagoDao.findAll();
    }

    public Pago save(Pago pago) {
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }
        if (pago.getEstado() == null) {
            pago.setEstado(false);
        }
        return pagoDao.save(pago);
    }

    public void deleteById(Long id) {
        pagoDao.deleteById(id);
    }

    public boolean existsById(Long id) {
        return pagoDao.existsById(id);
    }

    public List<Pago> findByMetodoPago(MetodoPago metodoPago) {
        if (metodoPago == null) {
            return new ArrayList<>();
        }
        return pagoDao.findByMetodoPago(metodoPago);
    }

    // Ejemplo de método adicional: contar pagos activos
    public long countPagosActivos() {
        return pagoDao.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getEstado()))
                .count();
    }

    // Ejemplo de método adicional: buscar pagos por rango de fecha
    public List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin) {
        List<Pago> todos = pagoDao.findAll();
        List<Pago> filtrados = new ArrayList<>();
        for (Pago p : todos) {
            if (p.getFechaPago() != null &&
                !p.getFechaPago().isBefore(inicio) &&
                !p.getFechaPago().isAfter(fin)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    // Otros métodos que necesites para lógica de negocio o reportes...
}