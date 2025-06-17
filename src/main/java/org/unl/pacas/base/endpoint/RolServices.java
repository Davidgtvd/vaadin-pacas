package org.unl.pacas.base.endpoint;

import com.vaadin.hilla.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.services.RolService;

import java.util.List;
import java.util.Optional;

@Endpoint
@Transactional
public class RolServices {

    @Autowired
    private RolService rolService;

    public List<Rol> listAll() {
        return rolService.findAll();
    }

    public Rol create(String nombre, String descripcion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (!rolService.esNombreUnico(nombre, null)) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre");
        }
        Rol rol = new Rol();
        rol.setNombre(nombre.trim());
        rol.setDescripcion(descripcion != null ? descripcion.trim() : null);
        return rolService.save(rol);
    }

    public Rol update(Long id, String nombre, String descripcion) {
        if (id == null) {
            throw new IllegalArgumentException("El ID es obligatorio para actualizar");
        }
        Optional<Rol> existing = rolService.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el rol con ID: " + id);
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (!rolService.esNombreUnico(nombre, id)) {
            throw new IllegalArgumentException("Ya existe otro rol con ese nombre");
        }
        Rol rol = existing.get();
        rol.setNombre(nombre.trim());
        rol.setDescripcion(descripcion != null ? descripcion.trim() : null);
        return rolService.update(rol);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID es obligatorio para eliminar");
        }
        if (!rolService.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el rol con ID: " + id);
        }
        if (!rolService.sePuedeEliminar(id)) {
            throw new IllegalStateException("No se puede eliminar el rol porque tiene cuentas asociadas");
        }
        rolService.deleteById(id);
    }
}