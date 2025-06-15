package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.RolDao;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private RolDao rolDao;

    // Operaciones CRUD básicas

    public List<Rol> findAll() {
        return linkedListToList(rolDao.findAll());
    }

    public Optional<Rol> findById(Long id) {
        return rolDao.findById(id);
    }

    public Optional<Rol> findByNombre(String nombre) {
        return rolDao.findByNombre(nombre);
    }

    public Rol save(Rol rol) {
        return rolDao.save(rol);
    }

    public Rol update(Rol rol) {
        if (rol.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar un rol sin ID");
        }
        return rolDao.save(rol);
    }

    public void deleteById(Long id) {
        if (!sePuedeEliminar(id)) {
            throw new IllegalStateException("No se puede eliminar el rol porque tiene cuentas asociadas");
        }
        rolDao.deleteById(id);
    }

    public void delete(Rol rol) {
        deleteById(rol.getId());
    }

    // Validaciones

    public boolean existsByNombre(String nombre) {
        return rolDao.existsByNombre(nombre);
    }

    public boolean esNombreUnico(String nombre, Long idExcluir) {
        Optional<Rol> rolOpt = rolDao.findByNombre(nombre);
        if (rolOpt.isEmpty()) return true;
        if (idExcluir == null) return false;
        return rolOpt.get().getId().equals(idExcluir);
    }

    public boolean sePuedeEliminar(Long id) {
        return rolDao.sePuedeEliminar(id);
    }

    public boolean existe(Long id) {
        return rolDao.existsById(id);
    }

    // Búsquedas avanzadas adaptadas

    public List<Rol> buscarPorNombre(String nombre) {
        List<Rol> todas = findAll();
        List<Rol> filtradas = new ArrayList<>();
        if (nombre == null || nombre.trim().isEmpty()) return filtradas;
        String lower = nombre.toLowerCase();
        for (Rol r : todas) {
            if (r.getNombre() != null && r.getNombre().toLowerCase().contains(lower)) {
                filtradas.add(r);
            }
        }
        return filtradas;
    }

    public List<Rol> buscarPorDescripcion(String descripcion) {
        List<Rol> todas = findAll();
        List<Rol> filtradas = new ArrayList<>();
        if (descripcion == null || descripcion.trim().isEmpty()) return filtradas;
        String lower = descripcion.toLowerCase();
        for (Rol r : todas) {
            if (r.getDescripcion() != null && r.getDescripcion().toLowerCase().contains(lower)) {
                filtradas.add(r);
            }
        }
        return filtradas;
    }

    public List<Rol> buscarPorTexto(String texto) {
        List<Rol> todas = findAll();
        List<Rol> filtradas = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) return filtradas;
        String lower = texto.toLowerCase();
        for (Rol r : todas) {
            if ((r.getNombre() != null && r.getNombre().toLowerCase().contains(lower)) ||
                (r.getDescripcion() != null && r.getDescripcion().toLowerCase().contains(lower))) {
                filtradas.add(r);
            }
        }
        return filtradas;
    }

    public List<Rol> findAllOrdenados(boolean ascendente) {
        List<Rol> lista = findAll();
        lista.sort((r1, r2) -> {
            int cmp = r1.getNombre().compareToIgnoreCase(r2.getNombre());
            return ascendente ? cmp : -cmp;
        });
        return lista;
    }

    public List<Rol> findRolesConCuentas() {
        List<Rol> todas = findAll();
        List<Rol> conCuentas = new ArrayList<>();
        for (Rol r : todas) {
            if (rolDao.contarCuentasPorRol(r.getId()) > 0) {
                conCuentas.add(r);
            }
        }
        return conCuentas;
    }

    public List<Rol> findRolesSinCuentas() {
        List<Rol> todas = findAll();
        List<Rol> sinCuentas = new ArrayList<>();
        for (Rol r : todas) {
            if (rolDao.contarCuentasPorRol(r.getId()) == 0) {
                sinCuentas.add(r);
            }
        }
        return sinCuentas;
    }

    // Métodos para LinkedList

    public LinkedList<Rol> findAllAsLinkedList() {
        return rolDao.findAll();
    }

    public LinkedList<Rol> buscarPorTextoAsLinkedList(String texto) {
        return rolDao.buscarPorTextoAsLinkedList(texto);
    }

    public LinkedList<Rol> findAllOrderedAsLinkedList(boolean ascendente) {
        return rolDao.findAllOrderedAsLinkedList(ascendente);
    }

    // Estadísticas

    public long contarTotal() {
        return rolDao.contarTotalRoles();
    }

    public long contarCuentasPorRol(Long rolId) {
        return rolDao.contarCuentasPorRol(rolId);
    }

    public List<Object[]> obtenerEstadisticas() {
        return rolDao.obtenerEstadisticasRoles();
    }

    // Inicialización de datos

    public void createDefaultRoles() {
        if (!existsByNombre("ADMIN")) {
            Rol admin = new Rol("ADMIN", "Administrador del sistema con acceso completo");
            save(admin);
        }
        if (!existsByNombre("CLIENTE")) {
            Rol cliente = new Rol("CLIENTE", "Cliente del sistema para realizar compras");
            save(cliente);
        }
        if (!existsByNombre("VENDEDOR")) {
            Rol vendedor = new Rol("VENDEDOR", "Vendedor del sistema para gestionar productos");
            save(vendedor);
        }
    }

    public Rol getRolAdmin() {
        return findByNombre("ADMIN").orElse(null);
    }

    public Rol getRolCliente() {
        return findByNombre("CLIENTE").orElse(null);
    }

    public Rol getRolVendedor() {
        return findByNombre("VENDEDOR").orElse(null);
    }

    // Métodos utilitarios para las vistas

    public Rol crearRol(String nombre, String descripcion) {
        if (!esNombreUnico(nombre, null)) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + nombre);
        }

        Rol rol = new Rol(nombre, descripcion);
        return save(rol);
    }

    public Rol actualizarRol(Long id, String nombre, String descripcion) {
        Optional<Rol> rolExistente = findById(id);
        if (rolExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el rol con ID: " + id);
        }

        if (!esNombreUnico(nombre, id)) {
            throw new IllegalArgumentException("Ya existe otro rol con el nombre: " + nombre);
        }

        Rol rol = rolExistente.get();
        rol.setNombre(nombre);
        rol.setDescripcion(descripcion);
        return update(rol);
    }

    public void eliminarRol(Long id) {
        Optional<Rol> rol = findById(id);
        if (rol.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el rol con ID: " + id);
        }

        if (!sePuedeEliminar(id)) {
            throw new IllegalStateException("No se puede eliminar el rol '" + rol.get().getNombre() +
                    "' porque tiene " + contarCuentasPorRol(id) + " cuentas asociadas");
        }

        deleteById(id);
    }

    // Validaciones para formularios y vistas

    public boolean validarNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() <= 50;
    }

    public boolean validarDescripcion(String descripcion) {
        return descripcion == null || descripcion.length() <= 200;
    }

    public String validarRol(Rol rol) {
        if (rol == null) {
            return "El rol no puede ser nulo";
        }

        if (!validarNombre(rol.getNombre())) {
            return "El nombre es obligatorio y no puede tener más de 50 caracteres";
        }

        if (!validarDescripcion(rol.getDescripcion())) {
            return "La descripción no puede tener más de 200 caracteres";
        }

        if (!esNombreUnico(rol.getNombre(), rol.getId())) {
            return "Ya existe un rol con el nombre: " + rol.getNombre();
        }

        return null; // Sin errores
    }

    // Reportes

    public List<Rol> generarReporteRoles() {
        return findAllOrdenados(true);
    }

    public String generarResumenEstadisticas() {
        long total = contarTotal();
        long conCuentas = findRolesConCuentas().size();
        long sinCuentas = findRolesSinCuentas().size();

        return String.format("Total de roles: %d | Con cuentas: %d | Sin cuentas: %d",
                total, conCuentas, sinCuentas);
    }

    // Método auxiliar para convertir LinkedList a List
    private <T> List<T> linkedListToList(LinkedList<T> linkedList) {
        List<T> list = new ArrayList<>();
        if (linkedList != null) {
            linkedList.forEach(list::add);
        }
        return list;
    }

    // Método para verificar existencia por id
    public boolean existsById(Long id) {
        return rolDao.existsById(id);
    }
}