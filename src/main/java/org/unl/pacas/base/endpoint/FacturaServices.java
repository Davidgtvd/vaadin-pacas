package org.unl.pacas.base.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unl.pacas.base.models.Factura;
import org.unl.pacas.base.services.FacturaService;

import java.util.List;

@RestController
@RequestMapping("/api/hilla/facturas")
public class FacturaServices {

    private final FacturaService facturaService;

    public FacturaServices(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public ResponseEntity<List<Factura>> findAll() {
        return ResponseEntity.ok(facturaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> findById(@PathVariable Long id) {
        return facturaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Factura> save(@RequestBody Factura factura) {
        Factura saved = facturaService.save(factura);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Factura> update(@PathVariable Long id, @RequestBody Factura factura) {
        if (factura.getId() == null || !factura.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        Factura updated = facturaService.update(factura);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!facturaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        facturaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Factura>> buscarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(facturaService.buscarPorProducto(productoId));
    }

    @GetMapping("/compra/{compraId}")
    public ResponseEntity<List<Factura>> buscarPorCompra(@PathVariable Long compraId) {
        return ResponseEntity.ok(facturaService.buscarPorCompra(compraId));
    }

    @GetMapping("/ordenados")
    public ResponseEntity<List<Factura>> findAllOrdenados(
            @RequestParam String campo,
            @RequestParam boolean ascendente) {
        return ResponseEntity.ok(facturaService.findAllOrdenados(campo, ascendente));
    }

    @GetMapping("/contar")
    public ResponseEntity<Long> contarTotal() {
        return ResponseEntity.ok(facturaService.contarTotal());
    }
}