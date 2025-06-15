package org.unl.pacas.base.dao.dao_models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolDao extends JpaRepository<Rol, Long> {

    // Búsquedas básicas
    Optional<Rol> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    List<Rol> findByNombreContainingIgnoreCase(String nombre);
    
    List<Rol> findByDescripcionContainingIgnoreCase(String descripcion);

    // Búsquedas combinadas
    @Query("SELECT r FROM Rol r WHERE " +
           "LOWER(r.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Rol> buscarPorTexto(@Param("texto") String texto);

    // Ordenamiento
    List<Rol> findAllByOrderByNombreAsc();
    
    List<Rol> findAllByOrderByNombreDesc();

    // Roles con cuentas asociadas
    @Query("SELECT r FROM Rol r WHERE SIZE(r.cuentas) > 0")
    List<Rol> findRolesConCuentas();

    @Query("SELECT r FROM Rol r WHERE SIZE(r.cuentas) = 0")
    List<Rol> findRolesSinCuentas();

    // Contar cuentas por rol
    @Query("SELECT COUNT(c) FROM Cuenta c WHERE c.rol.id = :rolId")
    Long contarCuentasPorRol(@Param("rolId") Long rolId);

    // Métodos personalizados para LinkedList
    default LinkedList<Rol> findAllAsLinkedList() {
        LinkedList<Rol> linkedList = new LinkedList<>();
        List<Rol> roles = findAll();
        for (Rol rol : roles) {
            linkedList.add(rol);
        }
        return linkedList;
    }

    default LinkedList<Rol> buscarPorTextoAsLinkedList(String texto) {
        LinkedList<Rol> linkedList = new LinkedList<>();
        List<Rol> roles = buscarPorTexto(texto);
        for (Rol rol : roles) {
            linkedList.add(rol);
        }
        return linkedList;
    }

    default LinkedList<Rol> findAllOrderedAsLinkedList(boolean ascendente) {
        LinkedList<Rol> linkedList = new LinkedList<>();
        List<Rol> roles = ascendente ? findAllByOrderByNombreAsc() : findAllByOrderByNombreDesc();
        for (Rol rol : roles) {
            linkedList.add(rol);
        }
        return linkedList;
    }

    // Validaciones para las vistas
    default boolean esNombreUnico(String nombre, Long idExcluir) {
        Optional<Rol> rolExistente = findByNombre(nombre);
        return rolExistente.isEmpty() || 
               (idExcluir != null && rolExistente.get().getId().equals(idExcluir));
    }

    default boolean sePuedeEliminar(Long id) {
        return contarCuentasPorRol(id) == 0;
    }

    // Estadísticas para dashboards
    @Query("SELECT COUNT(r) FROM Rol r")
    Long contarTotalRoles();

    @Query("SELECT r.nombre, COUNT(c) FROM Rol r LEFT JOIN r.cuentas c GROUP BY r.id, r.nombre")
    List<Object[]> obtenerEstadisticasRoles();
}