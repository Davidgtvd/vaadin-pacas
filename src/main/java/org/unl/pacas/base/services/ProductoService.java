package org.unl.pacas.base.services;

import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.ProductoDao;
import org.unl.pacas.base.models.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoDao productoDao;

    public ProductoService(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }

    public List<Producto> findAllActivos() {
        return productoDao.findAllActivos();
    }

    public Optional<Producto> findById(Long id) {
        return productoDao.findById(id);
    }

    public Producto save(Producto producto) {
        return productoDao.save(producto);
    }

    public void deleteById(Long id) {
        productoDao.deleteById(id);
    }

    public List<String> getAllCategorias() {
        return productoDao.findAllActivos().stream()
                .map(Producto::getCategoria)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Producto> getProductosConStockBajo() {
        return productoDao.findAllActivos().stream()
                .filter(Producto::tieneStockBajo)
                .collect(Collectors.toList());
    }

    public List<Producto> getProductosSinStock() {
        return productoDao.findAllActivos().stream()
                .filter(Producto::estaSinStock)
                .collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return productoDao.findById(id).isPresent();
    }

    public long countProductosActivos() {
        return productoDao.findAllActivos().size();
    }

    public Double getValorInventarioCompra() {
        return productoDao.findAllActivos().stream()
                .map(p -> p.getPrecioCosto() != null && p.getStock() != null ? p.getPrecioCosto().doubleValue() * p.getStock() : 0)
                .reduce(0.0, Double::sum);
    }

    public Double getValorInventarioVenta() {
        return productoDao.findAllActivos().stream()
                .map(p -> p.getPrecio() != null && p.getStock() != null ? p.getPrecio().doubleValue() * p.getStock() : 0)
                .reduce(0.0, Double::sum);
    }

    public void incrementarStock(Long productoId, Integer cantidad) {
        productoDao.findById(productoId).ifPresent(p -> {
            p.aumentarStock(cantidad);
            productoDao.save(p);
        });
    }

    public void decrementarStock(Long productoId, Integer cantidad) {
        productoDao.findById(productoId).ifPresent(p -> {
            p.reducirStock(cantidad);
            productoDao.save(p);
        });
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoDao.buscarPorNombre(nombre);
    }

    public List<Producto> findByCategoria(String categoria) {
        return productoDao.findByCategoria(categoria);
    }
}