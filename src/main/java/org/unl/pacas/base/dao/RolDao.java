package org.unl.pacas.base.dao;

import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RolDao {

    private final LinkedList<Rol> roles = new LinkedList<>();

    public LinkedList<Rol> findAll() {
        return roles;
    }

    public Optional<Rol> findById(Long id) {
        for (Rol r : roles) {
            if (r.getId() != null && r.getId().equals(id)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public Optional<Rol> findByNombre(String nombre) {
        if (nombre == null) return Optional.empty();
        for (Rol r : roles) {
            if (nombre.equalsIgnoreCase(r.getNombre())) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public boolean existsByNombre(String nombre) {
        return findByNombre(nombre).isPresent();
    }

    public List<Rol> findByNombreContainingIgnoreCase(String nombre) {
        List<Rol> result = new ArrayList<>();
        if (nombre == null) return result;
        String lower = nombre.toLowerCase();
        for (Rol r : roles) {
            if (r.getNombre() != null && r.getNombre().toLowerCase().contains(lower)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Rol> findByDescripcionContainingIgnoreCase(String descripcion) {
        List<Rol> result = new ArrayList<>();
        if (descripcion == null) return result;
        String lower = descripcion.toLowerCase();
        for (Rol r : roles) {
            if (r.getDescripcion() != null && r.getDescripcion().toLowerCase().contains(lower)) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Rol> buscarPorTexto(String texto) {
        List<Rol> result = new ArrayList<>();
        if (texto == null) return result;
        String lower = texto.toLowerCase();
        for (Rol r : roles) {
            if ((r.getNombre() != null && r.getNombre().toLowerCase().contains(lower)) ||
                (r.getDescripcion() != null && r.getDescripcion().toLowerCase().contains(lower))) {
                result.add(r);
            }
        }
        return result;
    }

    public List<Rol> findAllByOrderByNombreAsc() {
        List<Rol> list = new ArrayList<>();
        roles.forEach(list::add);
        list.sort((r1, r2) -> {
            if (r1.getNombre() == null) return 1;
            if (r2.getNombre() == null) return -1;
            return r1.getNombre().compareToIgnoreCase(r2.getNombre());
        });
        return list;
    }

    public List<Rol> findAllByOrderByNombreDesc() {
        List<Rol> list = new ArrayList<>();
        roles.forEach(list::add);
        list.sort((r1, r2) -> {
            if (r1.getNombre() == null) return 1;
            if (r2.getNombre() == null) return -1;
            return r2.getNombre().compareToIgnoreCase(r1.getNombre());
        });
        return list;
    }

    public Rol save(Rol rol) {
        if (rol.getId() == null) {
            long maxId = 0;
            for (Rol r : roles) {
                if (r.getId() != null && r.getId() > maxId) {
                    maxId = r.getId();
                }
            }
            rol.setId(maxId + 1);
            roles.add(rol);
        } else {
            Optional<Rol> existing = findById(rol.getId());
            if (existing.isPresent()) {
                Rol r = existing.get();
                r.setNombre(rol.getNombre());
                r.setDescripcion(rol.getDescripcion());
                // Actualiza otros campos si tienes
            } else {
                roles.add(rol);
            }
        }
        return rol;
    }

    public void deleteById(Long id) {
        roles.removeIf(r -> r.getId() != null && r.getId().equals(id));
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public boolean sePuedeEliminar(Long id) {
        // Aquí debes implementar la lógica para saber si un rol tiene cuentas asociadas
        // Como no usas BD, deberías inyectar CuentaDao y verificar si hay cuentas con este rol
        // Por ahora, retornamos true para permitir eliminar
        return true;
    }

    public long contarTotalRoles() {
        return roles.size();
    }

    // Métodos para LinkedList

    public LinkedList<Rol> findAllAsLinkedList() {
        LinkedList<Rol> linkedList = new LinkedList<>();
        roles.forEach(linkedList::add);
        return linkedList;
    }

    public LinkedList<Rol> buscarPorTextoAsLinkedList(String texto) {
        LinkedList<Rol> linkedList = new LinkedList<>();
        buscarPorTexto(texto).forEach(linkedList::add);
        return linkedList;
    }

    public LinkedList<Rol> findAllOrderedAsLinkedList(boolean ascendente) {
        LinkedList<Rol> linkedList = new LinkedList<>();
        List<Rol> list = ascendente ? findAllByOrderByNombreAsc() : findAllByOrderByNombreDesc();
        list.forEach(linkedList::add);
        return linkedList;
    }

    public boolean esNombreUnico(String nombre, Long idExcluir) {
        Optional<Rol> rolExistente = findByNombre(nombre);
        return rolExistente.isEmpty() || (idExcluir != null && rolExistente.get().getId().equals(idExcluir));
    }

    // Métodos simulados para cuentas por rol y estadísticas (debes implementar según tu lógica)

    public long contarCuentasPorRol(Long rolId) {
        // Implementa la lógica para contar cuentas asociadas a un rol
        return 0;
    }

    public List<Object[]> obtenerEstadisticasRoles() {
        // Implementa la lógica para obtener estadísticas de roles
        return new ArrayList<>();
    }
}