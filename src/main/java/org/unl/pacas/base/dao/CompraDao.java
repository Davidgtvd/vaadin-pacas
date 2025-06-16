package org.unl.pacas.base.dao;

import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Compra;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.util.Optional;

@Repository
public class CompraDao {

    private final LinkedList<Compra> compras = new LinkedList<>();

    public LinkedList<Compra> findAll() {
        return compras;
    }

    public Optional<Compra> findById(Long id) {
        for (Compra c : compras) {
            if (c.getId() != null && c.getId().equals(id)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Compra save(Compra compra) {
        if (compra.getId() == null) {
            long maxId = 0;
            for (Compra c : compras) {
                if (c.getId() != null && c.getId() > maxId) {
                    maxId = c.getId();
                }
            }
            compra.setId(maxId + 1);
            compras.add(compra);
        } else {
            Optional<Compra> existingOpt = findById(compra.getId());
            if (existingOpt.isPresent()) {
                Compra existing = existingOpt.get();
                existing.setSubtotal(compra.getSubtotal());
                existing.setNroFactura(compra.getNroFactura());
                existing.setIva(compra.getIva());
                existing.setTotal(compra.getTotal());
                existing.setPersona(compra.getPersona());
            } else {
                compras.add(compra);
            }
        }
        return compra;
    }

    public void deleteById(Long id) {
        compras.removeIf(c -> c.getId() != null && c.getId().equals(id));
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public long contarTotalCompras() {
        return compras.size();
    }
}
