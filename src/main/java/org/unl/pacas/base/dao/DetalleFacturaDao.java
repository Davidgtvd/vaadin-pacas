package org.unl.pacas.base.dao;

import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.DetalleFactura;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.util.Optional;

@Repository
public class DetalleFacturaDao {

    private final LinkedList<DetalleFactura> detalles = new LinkedList<>();

    public LinkedList<DetalleFactura> findAll() {
        return detalles;
    }

    public Optional<DetalleFactura> findById(Long id) {
        for (DetalleFactura d : detalles) {
            if (d.getId() != null && d.getId().equals(id)) {
                return Optional.of(d);
            }
        }
        return Optional.empty();
    }

    public DetalleFactura save(DetalleFactura detalle) {
        if (detalle.getId() == null) {
            long maxId = 0;
            for (DetalleFactura d : detalles) {
                if (d.getId() != null && d.getId() > maxId) {
                    maxId = d.getId();
                }
            }
            detalle.setId(maxId + 1);
            detalles.add(detalle);
        } else {
            Optional<DetalleFactura> existingOpt = findById(detalle.getId());
            if (existingOpt.isPresent()) {
                DetalleFactura existing = existingOpt.get();
                existing.setTotal(detalle.getTotal());
                existing.setCantidad(detalle.getCantidad());
                existing.setPrecioUnitario(detalle.getPrecioUnitario());
                existing.setProducto(detalle.getProducto());
                existing.setCompra(detalle.getCompra());
            } else {
                detalles.add(detalle);
            }
        }
        return detalle;
    }

    public void deleteById(Long id) {
        detalles.removeIf(d -> d.getId() != null && d.getId().equals(id));
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public long contarTotalDetalles() {
        return detalles.size();
    }

}
