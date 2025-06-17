package org.unl.pacas.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unl.pacas.base.models.MetodoPago;
import org.unl.pacas.base.models.Pago;
import org.unl.pacas.base.services.PagoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public List<Pago> getAllPagos() {
        return pagoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getPagoById(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.findById(id);
        return pago.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/metodo/{metodoPago}")
    public ResponseEntity<List<Pago>> getPagosByMetodo(@PathVariable MetodoPago metodoPago) {
        List<Pago> pagos = pagoService.findByMetodoPago(metodoPago);
        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pagos);
    }

    @PostMapping
    public ResponseEntity<?> crearPago(@RequestBody Pago pago) {
        try {
            Pago nuevoPago = pagoService.crearPago(pago);
            return ResponseEntity.ok(nuevoPago);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno al crear el pago");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPago(@PathVariable Long id, @RequestBody Pago pago) {
        try {
            if (!id.equals(pago.getId())) {
                return ResponseEntity.badRequest().body("El ID del pago no coincide con el parámetro");
            }
            if (!pagoService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            Pago pagoActualizado = pagoService.actualizarPago(pago);
            return ResponseEntity.ok(pagoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno al actualizar el pago");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPago(@PathVariable Long id) {
        if (!pagoService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            pagoService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno al eliminar el pago");
        }
    }
}