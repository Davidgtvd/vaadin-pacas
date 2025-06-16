package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.DetalleFacturaDao;
import org.unl.pacas.base.models.DetalleFactura;
import org.unl.pacas.base.models.Producto;
import org.unl.pacas.base.models.Compra;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleFacturaService {

    @Autowired
    private DetalleFacturaDao detalleFacturaDao;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CompraService compraService;

    // CRUD básicos

    public List<DetalleFactura> findAll() {
        return linkedListToList(detalleFacturaDao.findAll());
    }

    public Optional<DetalleFactura> findById(Long id) {
        return detalleFacturaDao.findById(id);
    }

    public boolean existsById(Long id) {
        return detalleFacturaDao.existsById(id);
    }

    public DetalleFactura save(DetalleFactura detalle) {
        validarDetalleFactura(detalle, null);
        return detalleFacturaDao.save(detalle);
    }

    public DetalleFactura update(DetalleFactura detalle) {
        if (detalle.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar un detalle sin ID");
        }
        validarDetalleFactura(detalle, detalle.getId());
        return detalleFacturaDao.save(detalle);
    }

    public void deleteById(Long id) {
        detalleFacturaDao.deleteById(id);
    }

    public void delete(DetalleFactura detalle) {
        deleteById(detalle.getId());
    }

    // Validaciones

    public void validarDetalleFactura(DetalleFactura detalle, Long idExcluir) {
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle de factura no puede ser nulo");
        }
        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (detalle.getPrecioUnitario() <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }
        if (detalle.getTotal() <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor a 0");
        }
        if (detalle.getProducto() == null || detalle.getProducto().getId() == null ||
                !productoService.existsById(detalle.getProducto().getId())) {
            throw new IllegalArgumentException("Debe seleccionar un producto válido");
        }
        if (detalle.getCompra() == null || detalle.getCompra().getId() == null ||
                !compraService.existsById(detalle.getCompra().getId())) {
            throw new IllegalArgumentException("Debe seleccionar una compra válida");
        }
    }

    // Búsquedas avanzadas

    public List<DetalleFactura> buscarPorProducto(Long productoId) {
        List<DetalleFactura> todas = findAll();
        List<DetalleFactura> filtradas = new ArrayList<>();
        for (DetalleFactura d : todas) {
            if (d.getProducto() != null && d.getProducto().getId() != null && d.getProducto().getId().equals(productoId)) {
                filtradas.add(d);
            }
        }
        return filtradas;
    }

    public List<DetalleFactura> buscarPorCompra(Long compraId) {
        List<DetalleFactura> todas = findAll();
        List<DetalleFactura> filtradas = new ArrayList<>();
        for (DetalleFactura d : todas) {
            if (d.getCompra() != null && d.getCompra().getId() != null && d.getCompra().getId().equals(compraId)) {
                filtradas.add(d);
            }
        }
        return filtradas;
    }

   /*  public List<DetalleFactura> buscarPorTexto(String texto) {
        List<DetalleFactura> todas = findAll();
        List<DetalleFactura> filtradas = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) return filtradas;
        String lower = texto.toLowerCase();
        for (DetalleFactura d : todas) {
            if ((d.getProducto() != null && d.getProducto().toString().toLowerCase().contains(lower)) ||
                (d.getCompra() != null && d.getCompra().toString().toLowerCase().contains(lower))) {
                filtradas.add(d);
            }
        }
        return filtradas;
    }
*/
    public List<DetalleFactura> findAllOrdenados(String campo, boolean ascendente) {
        List<DetalleFactura> lista = findAll();
        lista.sort((d1, d2) -> {
            int cmp = 0;
            switch (campo.toLowerCase()) {
                case "cantidad":
                    cmp = Integer.compare(d1.getCantidad(), d2.getCantidad());
                    break;
                case "preciounitario":
                    cmp = Float.compare(d1.getPrecioUnitario(), d2.getPrecioUnitario());
                    break;
                case "total":
                    cmp = Float.compare(d1.getTotal(), d2.getTotal());
                    break;
                case "producto":
                    cmp = d1.getProducto().toString().compareToIgnoreCase(d2.getProducto().toString());
                    break;
                case "compra":
                    cmp = d1.getCompra().toString().compareToIgnoreCase(d2.getCompra().toString());
                    break;
                default:
                    cmp = 0;
            }
            return ascendente ? cmp : -cmp;
        });
        return lista;
    }

    // Estadísticas

    public long contarTotal() {
        return detalleFacturaDao.contarTotalDetalles();
    }

    // Métodos utilitarios para las vistas

    public DetalleFactura crearDetalleFactura(float total, int cantidad, float precioUnitario, Long productoId, Long compraId) {
        Optional<Producto> productoOpt = productoService.findById(productoId);
        if (productoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el producto con ID: " + productoId);
        }
        Optional<Compra> compraOpt = compraService.findById(compraId);
        if (compraOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la compra con ID: " + compraId);
        }
        DetalleFactura detalle = new DetalleFactura(total, cantidad, precioUnitario, productoOpt.get(), compraOpt.get());
        return save(detalle);
    }

    public DetalleFactura actualizarDetalleFactura(Long id, float total, int cantidad, float precioUnitario, Long productoId, Long compraId) {
        Optional<DetalleFactura> detalleExistente = findById(id);
        if (detalleExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el detalle de factura con ID: " + id);
        }
        Optional<Producto> productoOpt = productoService.findById(productoId);
        if (productoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el producto con ID: " + productoId);
        }
        Optional<Compra> compraOpt = compraService.findById(compraId);
        if (compraOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la compra con ID: " + compraId);
        }
        DetalleFactura detalle = detalleExistente.get();
        detalle.setTotal(total);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setProducto(productoOpt.get());
        detalle.setCompra(compraOpt.get());
        return update(detalle);
    }

    public void eliminarDetalleFactura(Long id) {
        Optional<DetalleFactura> detalle = findById(id);
        if (detalle.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el detalle de factura con ID: " + id);
        }
        deleteById(id);
    }

    // Método auxiliar para convertir LinkedList a List
    private <T> List<T> linkedListToList(LinkedList<T> linkedList) {
        List<T> list = new ArrayList<>();
        if (linkedList != null) {
            linkedList.forEach(list::add);
        }
        return list;
    }
}