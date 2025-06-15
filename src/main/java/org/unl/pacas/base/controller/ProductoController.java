package org.unl.pacas.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unl.pacas.base.services.ProductoService;
import org.unl.pacas.base.models.Producto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.findAllActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.findById(id);
        return producto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<Producto> buscarProductos(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/categoria/{categoria}")
    public List<Producto> getProductosByCategoria(@PathVariable String categoria) {
        return productoService.findByCategoria(categoria);
    }

    @GetMapping("/categorias")
    public List<String> getAllCategorias() {
        return productoService.getAllCategorias();
    }

    @GetMapping("/stock-bajo")
    public List<Producto> getProductosConStockBajo() {
        return productoService.getProductosConStockBajo();
    }

    @GetMapping("/sin-stock")
    public List<Producto> getProductosSinStock() {
        return productoService.getProductosSinStock();
    }

    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        if (!productoService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        producto.setId(id);
        return ResponseEntity.ok(productoService.update(producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (!productoService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Object> getEstadisticas() {
        return ResponseEntity.ok(new Object() {
            public final Long totalProductos = productoService.countProductosActivos();
            public final Double valorInventarioCompra = productoService.getValorInventarioCompra();
            public final Double valorInventarioVenta = productoService.getValorInventarioVenta();
            public final Integer productosStockBajo = productoService.getProductosConStockBajo().size();
            public final Integer productosSinStock = productoService.getProductosSinStock().size();
        });
    }
}