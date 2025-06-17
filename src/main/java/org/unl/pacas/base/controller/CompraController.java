package org.unl.pacas.base.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unl.pacas.base.models.Compra;
import org.unl.pacas.base.services.CompraService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ResponseEntity<List<Compra>> listAll() {
        List<Compra> compras = compraService.findAll();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> getById(@PathVariable Long id) {
        Optional<Compra> compra = compraService.findById(id);
        return compra.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/sin-pago")
    public ResponseEntity<List<Compra>> getComprasSinPago() {
        List<Compra> comprasSinPago = compraService.findComprasSinPago();
        return ResponseEntity.ok(comprasSinPago);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Compra>> buscarPorFactura(@RequestParam String factura) {
        List<Compra> resultados = compraService.findByNroFacturaContainingIgnoreCase(factura);
        return ResponseEntity.ok(resultados);
    }

    @PostMapping
    public ResponseEntity<Compra> crearCompra(@RequestBody Compra compra) {
        Compra creada = compraService.save(compra);
        return ResponseEntity.ok(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compra> actualizarCompra(@PathVariable Long id, @RequestBody Compra compra) {
        if (!id.equals(compra.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Compra actualizada = compraService.update(compra);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        if (!compraService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        compraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}