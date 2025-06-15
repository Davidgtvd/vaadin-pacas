package org.unl.pacas.base.controller;

import org.unl.pacas.base.models.Cuenta;
import org.unl.pacas.base.services.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public List<Cuenta> getAll() {
        return cuentaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> getById(@PathVariable Long id) {
        return cuentaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cuenta> save(@RequestBody Cuenta cuenta) {
        Cuenta saved = cuentaService.save(cuenta);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> update(@PathVariable Long id, @RequestBody Cuenta cuenta) {
        if (!cuentaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cuenta.setId(id);
        Cuenta updated = cuentaService.update(cuenta);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!cuentaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cuentaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}