package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unl.pacas.base.dao.ProductoRepository;
import org.unl.pacas.base.models.Producto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public List<Producto> findAllActivos() {
        return productoRepository.findByActivoTrue();
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }

    public List<Producto> findByCategoria(String categoria) {
        return productoRepository.findByCategoriaAndActivoTrue(categoria);
    }

    public List<String> getAllCategorias() {
        return productoRepository.findAllCategorias();
    }

    public List<Producto> getProductosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }

    public List<Producto> getProductosSinStock() {
        return productoRepository.findProductosSinStock();
    }

    public Producto save(Producto producto) {
        if (producto.getId() == null) {
            producto.setFechaCreacion(LocalDateTime.now());
        }
        producto.setFechaActualizacion(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    public Producto update(Producto producto) {
        producto.setFechaActualizacion(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    public void deleteById(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            producto.get().setActivo(false);
            productoRepository.save(producto.get());
        }
    }

    public void activar(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            producto.get().setActivo(true);
            productoRepository.save(producto.get());
        }
    }

    public boolean existsById(Long id) {
        return productoRepository.existsById(id);
    }

    public Long countProductosActivos() {
        return productoRepository.countProductosActivos();
    }

    public Double getValorInventarioCompra() {
        java.math.BigDecimal valor = productoRepository.calcularValorInventarioCompra();
        return valor != null ? valor.doubleValue() : 0.0;
    }

    public Double getValorInventarioVenta() {
        java.math.BigDecimal valor = productoRepository.calcularValorInventarioVenta();
        return valor != null ? valor.doubleValue() : 0.0;
    }

    public void actualizarStock(Long productoId, Integer nuevaCantidad) {
        Optional<Producto> producto = productoRepository.findById(productoId);
        if (producto.isPresent()) {
            producto.get().setStock(nuevaCantidad);
            producto.get().setFechaActualizacion(LocalDateTime.now());
            productoRepository.save(producto.get());
        }
    }

    public void incrementarStock(Long productoId, Integer cantidad) {
        Optional<Producto> producto = productoRepository.findById(productoId);
        if (producto.isPresent()) {
            int nuevoStock = producto.get().getStock() + cantidad;
            producto.get().setStock(nuevoStock);
            producto.get().setFechaActualizacion(LocalDateTime.now());
            productoRepository.save(producto.get());
        }
    }

    public void decrementarStock(Long productoId, Integer cantidad) {
        Optional<Producto> producto = productoRepository.findById(productoId);
        if (producto.isPresent()) {
            int nuevoStock = Math.max(0, producto.get().getStock() - cantidad);
            producto.get().setStock(nuevoStock);
            producto.get().setFechaActualizacion(LocalDateTime.now());
            productoRepository.save(producto.get());
        }
    }
}