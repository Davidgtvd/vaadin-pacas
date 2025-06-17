package org.unl.pacas.base.dao;

import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Factura;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FacturaDao {

    private final List<Factura> facturas = new ArrayList<>();

    public List<Factura> findAll() {
        return facturas;
    }

    public Optional<Factura> findById(Long id) {
        return facturas.stream()
                .filter(f -> f.getId() != null && f.getId().equals(id))
                .findFirst();
    }

    public Factura save(Factura factura) {
        if (factura.getId() == null) {
            long maxId = facturas.stream()
                    .mapToLong(f -> f.getId() != null ? f.getId() : 0)
                    .max()
                    .orElse(0);
            factura.setId(maxId + 1);
            facturas.add(factura);
        } else {
            Optional<Factura> existingOpt = findById(factura.getId());
            if (existingOpt.isPresent()) {
                Factura existing = existingOpt.get();
                // Actualiza campos básicos
                existing.setNroFactura(factura.getNroFactura());
                existing.setPersona(factura.getPersona());
                existing.setTotal(factura.getTotal());
                // Actualiza detalles: reemplaza lista completa
                existing.getDetalles().clear();
                if (factura.getDetalles() != null) {
                    factura.getDetalles().forEach(d -> {
                        d.setFactura(existing);
                        existing.getDetalles().add(d);
                    });
                }
            } else {
                facturas.add(factura);
            }
        }
        return factura;
    }

    public void deleteById(Long id) {
        facturas.removeIf(f -> f.getId() != null && f.getId().equals(id));
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public long contarTotal() {
        return facturas.size();
    }

    // Búsqueda por producto en detalles
    public List<Factura> buscarPorProducto(Long productoId) {
        List<Factura> resultado = new ArrayList<>();
        for (Factura f : facturas) {
            if (f.getDetalles() != null) {
                boolean tieneProducto = f.getDetalles().stream()
                        .anyMatch(d -> d.getProducto() != null && d.getProducto().getId().equals(productoId));
                if (tieneProducto) {
                    resultado.add(f);
                }
            }
        }
        return resultado;
    }

    // Búsqueda por compra en detalles
    public List<Factura> buscarPorCompra(Long compraId) {
        List<Factura> resultado = new ArrayList<>();
        for (Factura f : facturas) {
            if (f.getDetalles() != null) {
                boolean tieneCompra = f.getDetalles().stream()
                        .anyMatch(d -> d.getCompra() != null && d.getCompra().getId().equals(compraId));
                if (tieneCompra) {
                    resultado.add(f);
                }
            }
        }
        return resultado;
    }

    // Ordenamiento básico por campo y dirección
    public List<Factura> findAllOrdenados(String campo, boolean ascendente) {
        List<Factura> copia = new ArrayList<>(facturas);
        copia.sort((f1, f2) -> {
            int cmp = 0;
            switch (campo.toLowerCase()) {
                case "nrofactura":
                    cmp = f1.getNroFactura().compareToIgnoreCase(f2.getNroFactura());
                    break;
                case "total":
                    cmp = Float.compare(f1.getTotal(), f2.getTotal());
                    break;
                case "persona":
                    cmp = f1.getPersona().getNombreCompleto().compareToIgnoreCase(f2.getPersona().getNombreCompleto());
                    break;
                default:
                    cmp = 0;
            }
            return ascendente ? cmp : -cmp;
        });
        return copia;
    }
}