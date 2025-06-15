package org.unl.pacas.base.dao;

import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Cuenta;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class CuentaDao {

    private final LinkedList<Cuenta> cuentas = new LinkedList<>();

    public LinkedList<Cuenta> findAll() {
        return cuentas;
    }

    public Optional<Cuenta> findById(Long id) {
        for (Cuenta c : cuentas) {
            if (c.getId() != null && c.getId().equals(id)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Optional<Cuenta> findByUsuario(String usuario) {
        for (Cuenta c : cuentas) {
            if (c.getUsuario() != null && c.getUsuario().equalsIgnoreCase(usuario)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Optional<Cuenta> findByPersonaId(Long personaId) {
        for (Cuenta c : cuentas) {
            if (c.getPersona() != null && c.getPersona().getId() != null && c.getPersona().getId().equals(personaId)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Cuenta save(Cuenta cuenta) {
        if (cuenta.getId() == null) {
            // Asignar un id simple incremental
            long maxId = 0;
            for (Cuenta c : cuentas) {
                if (c.getId() != null && c.getId() > maxId) {
                    maxId = c.getId();
                }
            }
            cuenta.setId(maxId + 1);
            cuentas.add(cuenta);
        } else {
            // Actualizar cuenta existente
            Optional<Cuenta> existingOpt = findById(cuenta.getId());
            if (existingOpt.isPresent()) {
                Cuenta existing = existingOpt.get();
                existing.setUsuario(cuenta.getUsuario());
                existing.setClave(cuenta.getClave());
                existing.setActivo(cuenta.getActivo());
                existing.setRol(cuenta.getRol());
                existing.setFechaCreacion(cuenta.getFechaCreacion());
                existing.setUltimoAcceso(cuenta.getUltimoAcceso());
                existing.setIntentosFallidos(cuenta.getIntentosFallidos());
                existing.setFechaBloqueo(cuenta.getFechaBloqueo());
                existing.setPersona(cuenta.getPersona());
            } else {
                cuentas.add(cuenta);
            }
        }
        return cuenta;
    }

    public void deleteById(Long id) {
        cuentas.removeIf(c -> c.getId() != null && c.getId().equals(id));
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public boolean existsByUsuario(String usuario) {
        return findByUsuario(usuario).isPresent();
    }

    public boolean existsByPersonaId(Long personaId) {
        return findByPersonaId(personaId).isPresent();
    }

    public LinkedList<Cuenta> findByActivoAsLinkedList(Boolean activo) {
        LinkedList<Cuenta> result = new LinkedList<>();
        for (Cuenta c : cuentas) {
            if (c.getActivo() != null && c.getActivo().equals(activo)) {
                result.add(c);
            }
        }
        return result;
    }

    public LinkedList<Cuenta> findByRolAsLinkedList(Rol rol) {
        LinkedList<Cuenta> result = new LinkedList<>();
        for (Cuenta c : cuentas) {
            if (c.getRol() != null && c.getRol().equals(rol)) {
                result.add(c);
            }
        }
        return result;
    }

    public LinkedList<Cuenta> buscarPorTextoAsLinkedList(String texto) {
        LinkedList<Cuenta> result = new LinkedList<>();
        String lowerTexto = texto == null ? "" : texto.toLowerCase();
        for (Cuenta c : cuentas) {
            if ((c.getUsuario() != null && c.getUsuario().toLowerCase().contains(lowerTexto)) ||
                (c.getPersona() != null && (
                    (c.getPersona().getNombres() != null && c.getPersona().getNombres().toLowerCase().contains(lowerTexto)) ||
                    (c.getPersona().getApellidos() != null && c.getPersona().getApellidos().toLowerCase().contains(lowerTexto)) ||
                    (c.getPersona().getEmail() != null && c.getPersona().getEmail().toLowerCase().contains(lowerTexto))
                )) ||
                (c.getRol() != null && c.getRol().getNombre() != null && c.getRol().getNombre().toLowerCase().contains(lowerTexto))
            ) {
                result.add(c);
            }
        }
        return result;
    }

    public LinkedList<Cuenta> findAllOrderedAsLinkedList(String campo, boolean ascendente) {
        LinkedList<Cuenta> lista = new LinkedList<>();
        lista.toList(findAll().toArray()); // copia

        lista.ordenarPorAtributo(campo, ascendente);

        return lista;
    }

    public long contarTotalCuentas() {
        return cuentas.size();
    }

    public long contarCuentasActivas() {
        long count = 0;
        for (Cuenta c : cuentas) {
            if (Boolean.TRUE.equals(c.getActivo())) count++;
        }
        return count;
    }

    public long contarCuentasInactivas() {
        long count = 0;
        for (Cuenta c : cuentas) {
            if (Boolean.FALSE.equals(c.getActivo())) count++;
        }
        return count;
    }

    // Puedes agregar más métodos según necesites
}