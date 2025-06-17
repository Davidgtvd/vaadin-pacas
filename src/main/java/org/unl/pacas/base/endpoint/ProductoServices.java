package org.unl.pacas.base.endpoint;

import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.ProductoDao;
import org.unl.pacas.base.models.Producto;


import java.math.BigDecimal;
import java.util.List;


@Service
public class ProductoServices {

    private final ProductoDao productoDao;

    public ProductoServices(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }

    public List<Producto> findAllActivos() {
        return productoDao.findAllActivos();
    }

    public Producto save(Producto producto) {
        return productoDao.save(producto);
    }

    public void deleteById(Long id) {
        productoDao.deleteById(id);
    }

    public boolean existsById(Long id) {
        return productoDao.findById(id).isPresent();
    }

    public BigDecimal getValorInventarioCompra() {
        return productoDao.findAllActivos().stream()
                .map(p -> {
                    BigDecimal precioCosto = p.getPrecioCosto() != null ? p.getPrecioCosto() : BigDecimal.ZERO;
                    Integer stock = p.getStock() != null ? p.getStock() : 0;
                    return precioCosto.multiply(BigDecimal.valueOf(stock));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getValorInventarioVenta() {
        return productoDao.findAllActivos().stream()
                .map(p -> {
                    BigDecimal precio = p.getPrecio() != null ? p.getPrecio() : BigDecimal.ZERO;
                    Integer stock = p.getStock() != null ? p.getStock() : 0;
                    return precio.multiply(BigDecimal.valueOf(stock));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Otros métodos que uses en tu servicio...
}