package org.unl.pacas.base.controller;

import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.services.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public List<Persona> getAll() {
        return personaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getById(@PathVariable Long id) {
        return personaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Persona> save(@RequestBody Persona persona) {
        Persona saved = personaService.save(persona);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> update(@PathVariable Long id, @RequestBody Persona persona) {
        if (!personaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        persona.setId(id);
        Persona updated = personaService.update(persona);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!personaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        personaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}