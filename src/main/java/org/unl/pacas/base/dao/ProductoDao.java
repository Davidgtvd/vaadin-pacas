package org.unl.pacas.base.dao;

import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductoDao {

    private final List<Producto> productos = new ArrayList<>();

    public List<Producto> findAllActivos() {
        return productos.stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .collect(Collectors.toList());
    }

    public List<Producto> buscarPorNombre(String texto) {
        String lower = texto.toLowerCase();
        return productos.stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()) &&
                        p.getNombre() != null &&
                        p.getNombre().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Producto> findByCategoria(String categoria) {
        return productos.stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()) &&
                        p.getCategoria() != null &&
                        p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    public Optional<Producto> findById(Long id) {
        return productos.stream()
                .filter(p -> p.getId() != null && p.getId().equals(id))
                .findFirst();
    }

    public Producto save(Producto producto) {
        if (producto.getId() == null) {
            long maxId = productos.stream()
                    .mapToLong(p -> p.getId() != null ? p.getId() : 0)
                    .max()
                    .orElse(0);
            producto.setId(maxId + 1);
            productos.add(producto);
        } else {
            Optional<Producto> existingOpt = findById(producto.getId());
            if (existingOpt.isPresent()) {
                Producto existing = existingOpt.get();
                existing.setNombre(producto.getNombre());
                existing.setDescripcion(producto.getDescripcion());
                existing.setCodigo(producto.getCodigo());
                existing.setPrecio(producto.getPrecio());
                existing.setPrecioCosto(producto.getPrecioCosto());
                existing.setStock(producto.getStock());
                existing.setStockMinimo(producto.getStockMinimo());
                existing.setCategoria(producto.getCategoria());
                existing.setUnidadMedida(producto.getUnidadMedida());
                existing.setMarca(producto.getMarca());
                existing.setModelo(producto.getModelo());
                existing.setUbicacion(producto.getUbicacion());
                existing.setProveedor(producto.getProveedor());
                existing.setActivo(producto.getActivo());
                existing.setFechaCreacion(producto.getFechaCreacion());
                existing.setFechaActualizacion(producto.getFechaActualizacion());
            } else {
                productos.add(producto);
            }
        }
        return producto;
    }

    public void deleteById(Long id) {
        productos.removeIf(p -> p.getId() != null && p.getId().equals(id));
    }
}