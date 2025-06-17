package org.unl.pacas.base.services;

import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.PagoDao;
import org.unl.pacas.base.models.MetodoPago;
import org.unl.pacas.base.models.Pago;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoDao pagoDao;

    public PagoService(PagoDao pagoDao) {
        this.pagoDao = pagoDao;
    }

    public List<Pago> findAll() {
        return pagoDao.findAll();
    }

    public Optional<Pago> findById(Long id) {
        return Optional.ofNullable(pagoDao.findById(id));
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

    public List<Pago> findByMetodoPago(MetodoPago metodoPago) {
        if (metodoPago == null) {
            return new ArrayList<>();
        }
        return pagoDao.findByMetodoPago(metodoPago);
    }

    public String validarPago(Pago pago) {
        if (pago == null) {
            return "El pago no puede ser nulo";
        }
        if (pago.getMetodoPago() == null) {
            return "El método de pago es obligatorio";
        }
        if (pago.getCodigoSeguridad() == null || pago.getCodigoSeguridad().trim().isEmpty()) {
            return "El código de seguridad es obligatorio";
        }
        if (pago.getCuenta() == null) {
            return "La cuenta asociada al pago es obligatoria";
        }
        return null;
    }

    public Pago crearPago(Pago pago) {
        String error = validarPago(pago);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        return save(pago);
    }

    public Pago actualizarPago(Pago pago) {
        if (pago.getId() == null) {
            throw new IllegalArgumentException("El pago debe tener un ID para actualizar");
        }
        String error = validarPago(pago);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        return save(pago);
    }

    public boolean existsById(Long id) {
        return pagoDao.existsById(id);
    }
}