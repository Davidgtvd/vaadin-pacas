package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.FacturaDao;
import org.unl.pacas.base.models.Factura;
import org.unl.pacas.base.models.DetalleFactura;
import org.unl.pacas.base.models.Producto;
import org.unl.pacas.base.models.Compra;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaDao facturaDao;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CompraService compraService;

    // --- CRUD básicos ---

    public List<Factura> findAll() {
        return facturaDao.findAll();
    }

    public Optional<Factura> findById(Long id) {
        return facturaDao.findById(id);
    }

    public boolean existsById(Long id) {
        return facturaDao.existsById(id);
    }

    public Factura save(Factura factura) {
        validarFactura(factura, null);
        return facturaDao.save(factura);
    }

    public Factura update(Factura factura) {
        if (factura.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar una factura sin ID");
        }
        validarFactura(factura, factura.getId());
        return facturaDao.save(factura);
    }

    public void deleteById(Long id) {
        facturaDao.deleteById(id);
    }

    public void delete(Factura factura) {
        if (factura == null || factura.getId() == null) {
            throw new IllegalArgumentException("No se puede eliminar una factura nula o sin ID");
        }
        deleteById(factura.getId());
    }

    // --- Validaciones ---

    public void validarFactura(Factura factura, Long idExcluir) {
        if (factura == null) {
            throw new IllegalArgumentException("La factura no puede ser nula");
        }
        if (factura.getNroFactura() == null || factura.getNroFactura().isBlank()) {
            throw new IllegalArgumentException("El número de factura es obligatorio");
        }
        if (factura.getPersona() == null || factura.getPersona().getId() == null) {
            throw new IllegalArgumentException("La persona es obligatoria");
        }
        if (factura.getDetalles() == null || factura.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La factura debe tener al menos un detalle");
        }
        for (DetalleFactura detalle : factura.getDetalles()) {
            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
            if (detalle.getPrecioUnitario() <= 0) {
                throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
            }
            if (detalle.getTotal() < 0) {
                throw new IllegalArgumentException("El total del detalle no puede ser negativo");
            }
            if (detalle.getProducto() == null || detalle.getProducto().getId() == null ||
                    !productoService.existsById(detalle.getProducto().getId())) {
                throw new IllegalArgumentException("Debe seleccionar un producto válido en los detalles");
            }
        }
    }

    // --- Búsquedas avanzadas ---

    public List<Factura> buscarPorProducto(Long productoId) {
        List<Factura> todas = findAll();
        List<Factura> filtradas = new ArrayList<>();
        for (Factura f : todas) {
            if (f.getDetalles() != null) {
                boolean tieneProducto = f.getDetalles().stream()
                        .anyMatch(d -> d.getProducto() != null && d.getProducto().getId().equals(productoId));
                if (tieneProducto) {
                    filtradas.add(f);
                }
            }
        }
        return filtradas;
    }

    public List<Factura> buscarPorCompra(Long compraId) {
        List<Factura> todas = findAll();
        List<Factura> filtradas = new ArrayList<>();
        for (Factura f : todas) {
            if (f.getDetalles() != null) {
                boolean tieneCompra = f.getDetalles().stream()
                        .anyMatch(d -> d.getCompra() != null && d.getCompra().getId().equals(compraId));
                if (tieneCompra) {
                    filtradas.add(f);
                }
            }
        }
        return filtradas;
    }

    public List<Factura> findAllOrdenados(String campo, boolean ascendente) {
        List<Factura> lista = findAll();
        lista.sort((f1, f2) -> {
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
        return lista;
    }

    // --- Estadísticas ---

    public long contarTotal() {
        return facturaDao.contarTotal();
    }
}