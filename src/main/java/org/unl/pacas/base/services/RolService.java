package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unl.pacas.base.dao.dao_models.RolDao;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolDao rolDao;

    // Operaciones CRUD básicas
    @Transactional(readOnly = true)
    public List<Rol> findAll() {
        return rolDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Rol> findById(Long id) {
        return rolDao.findById(id);
    }

    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return rolDao.existsByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public boolean esNombreUnico(String nombre, Long idExcluir) {
        return rolDao.esNombreUnico(nombre, idExcluir);
    }

    @Transactional(readOnly = true)
    public boolean sePuedeEliminar(Long id) {
        return rolDao.sePuedeEliminar(id);
    }

    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return rolDao.existsById(id);
    }

    // Búsquedas avanzadas
    @Transactional(readOnly = true)
    public List<Rol> buscarPorNombre(String nombre) {
        return rolDao.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional(readOnly = true)
    public List<Rol> buscarPorDescripcion(String descripcion) {
        return rolDao.findByDescripcionContainingIgnoreCase(descripcion);
    }

    @Transactional(readOnly = true)
    public List<Rol> buscarPorTexto(String texto) {
        return rolDao.buscarPorTexto(texto);
    }

    @Transactional(readOnly = true)
    public List<Rol> findAllOrdenados(boolean ascendente) {
        return ascendente ? rolDao.findAllByOrderByNombreAsc() : rolDao.findAllByOrderByNombreDesc();
    }

    @Transactional(readOnly = true)
    public List<Rol> findRolesConCuentas() {
        return rolDao.findRolesConCuentas();
    }

    @Transactional(readOnly = true)
    public List<Rol> findRolesSinCuentas() {
        return rolDao.findRolesSinCuentas();
    }

    // Métodos para LinkedList
    @Transactional(readOnly = true)
    public LinkedList<Rol> findAllAsLinkedList() {
        return rolDao.findAllAsLinkedList();
    }

    @Transactional(readOnly = true)
    public LinkedList<Rol> buscarPorTextoAsLinkedList(String texto) {
        return rolDao.buscarPorTextoAsLinkedList(texto);
    }

    @Transactional(readOnly = true)
    public LinkedList<Rol> findAllOrderedAsLinkedList(boolean ascendente) {
        return rolDao.findAllOrderedAsLinkedList(ascendente);
    }

    // Estadísticas
    @Transactional(readOnly = true)
    public Long contarTotal() {
        return rolDao.contarTotalRoles();
    }

    @Transactional(readOnly = true)
    public Long contarCuentasPorRol(Long rolId) {
        return rolDao.contarCuentasPorRol(rolId);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public Rol getRolAdmin() {
        return findByNombre("ADMIN").orElse(null);
    }

    @Transactional(readOnly = true)
    public Rol getRolCliente() {
        return findByNombre("CLIENTE").orElse(null);
    }

    @Transactional(readOnly = true)
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

    // Métodos para formularios y validaciones en vistas
    @Transactional(readOnly = true)
    public boolean validarNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() <= 50;
    }

    @Transactional(readOnly = true)
    public boolean validarDescripcion(String descripcion) {
        return descripcion == null || descripcion.length() <= 200;
    }

    @Transactional(readOnly = true)
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

    // Métodos para reportes
    @Transactional(readOnly = true)
    public List<Rol> generarReporteRoles() {
        return findAllOrdenados(true);
    }

    @Transactional(readOnly = true)
    public String generarResumenEstadisticas() {
        Long total = contarTotal();
        Long conCuentas = (long) findRolesConCuentas().size();
        Long sinCuentas = (long) findRolesSinCuentas().size();

        return String.format("Total de roles: %d | Con cuentas: %d | Sin cuentas: %d",
                total, conCuentas, sinCuentas);
    }
}