package org.unl.pacas.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Cuenta;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.models.Sexo;
import org.unl.pacas.base.models.TipoIdentificacion;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaDao extends JpaRepository<Cuenta, Long> {

    // Búsquedas básicas
    Optional<Cuenta> findByUsuario(String usuario);
    
    Optional<Cuenta> findByPersonaId(Long personaId);
    
    boolean existsByUsuario(String usuario);
    
    boolean existsByPersonaId(Long personaId);

    // Búsquedas por estado
    List<Cuenta> findByActivo(Boolean activo);
    
    List<Cuenta> findByActivoTrue();
    
    List<Cuenta> findByActivoFalse();

    // Búsquedas por rol
    List<Cuenta> findByRol(Rol rol);
    
    List<Cuenta> findByRolId(Long rolId);
    
    List<Cuenta> findByRol_Nombre(String nombre);

    // Búsquedas por fechas
    List<Cuenta> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    
    List<Cuenta> findByUltimoAccesoBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT c FROM Cuenta c WHERE c.ultimoAcceso IS NULL")
    List<Cuenta> findCuentasSinAcceso();

    // Búsquedas por intentos fallidos y bloqueos
    List<Cuenta> findByIntentosFallidosGreaterThan(Integer intentos);
    
    @Query("SELECT c FROM Cuenta c WHERE c.fechaBloqueo IS NOT NULL AND c.fechaBloqueo > :fecha")
    List<Cuenta> findCuentasBloqueadas(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT c FROM Cuenta c WHERE c.fechaBloqueo IS NULL OR c.fechaBloqueo <= :fecha")
    List<Cuenta> findCuentasNoBloqueadas(@Param("fecha") LocalDateTime fecha);

    // Búsquedas por datos de persona
    @Query("SELECT c FROM Cuenta c WHERE " +
           "LOWER(c.persona.nombres) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
           "LOWER(c.persona.apellidos) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Cuenta> findByPersonaNombre(@Param("nombre") String nombre);

    @Query("SELECT c FROM Cuenta c WHERE LOWER(c.persona.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Cuenta> findByPersonaEmail(@Param("email") String email);

    @Query("SELECT c FROM Cuenta c WHERE c.persona.sexo = :sexo")
    List<Cuenta> findByPersonaSexo(@Param("sexo") Sexo sexo);

    @Query("SELECT c FROM Cuenta c WHERE c.persona.tipoIdentificacion = :tipo")
    List<Cuenta> findByPersonaTipoIdentificacion(@Param("tipo") TipoIdentificacion tipo);

    // Búsqueda general
    @Query("SELECT c FROM Cuenta c WHERE " +
           "LOWER(c.usuario) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(c.persona.nombres) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(c.persona.apellidos) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(c.persona.email) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(c.rol.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Cuenta> buscarPorTexto(@Param("texto") String texto);

    // Ordenamiento
    List<Cuenta> findAllByOrderByUsuarioAsc();
    
    List<Cuenta> findAllByOrderByFechaCreacionDesc();
    
    List<Cuenta> findAllByOrderByUltimoAccesoDesc();
    
    @Query("SELECT c FROM Cuenta c ORDER BY c.persona.nombres ASC, c.persona.apellidos ASC")
    List<Cuenta> findAllOrderByPersonaNombre();

    @Query("SELECT c FROM Cuenta c ORDER BY c.rol.nombre ASC")
    List<Cuenta> findAllOrderByRolNombre();

    // Autenticación y seguridad
    @Query("SELECT c FROM Cuenta c WHERE c.usuario = :usuario AND c.activo = true")
    Optional<Cuenta> findByUsuarioAndActivoTrue(@Param("usuario") String usuario);

    @Query("SELECT c FROM Cuenta c WHERE c.usuario = :usuario AND c.activo = true AND " +
           "(c.fechaBloqueo IS NULL OR c.fechaBloqueo <= :fechaLimite)")
    Optional<Cuenta> findCuentaParaLogin(@Param("usuario") String usuario, 
                                        @Param("fechaLimite") LocalDateTime fechaLimite);

    // Estadísticas
    @Query("SELECT COUNT(c) FROM Cuenta c WHERE c.activo = true")
    Long contarCuentasActivas();

    @Query("SELECT COUNT(c) FROM Cuenta c WHERE c.activo = false")
    Long contarCuentasInactivas();

    @Query("SELECT COUNT(c) FROM Cuenta c WHERE c.fechaBloqueo IS NOT NULL AND c.fechaBloqueo > :fecha")
    Long contarCuentasBloqueadas(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT r.nombre, COUNT(c) FROM Cuenta c JOIN c.rol r GROUP BY r.id, r.nombre")
    List<Object[]> obtenerEstadisticasPorRol();

    @Query("SELECT c.activo, COUNT(c) FROM Cuenta c GROUP BY c.activo")
    List<Object[]> obtenerEstadisticasPorEstado();

    @Query("SELECT p.sexo, COUNT(c) FROM Cuenta c JOIN c.persona p GROUP BY p.sexo")
    List<Object[]> obtenerEstadisticasPorSexo();

    // Métodos personalizados para LinkedList
    default LinkedList<Cuenta> findAllAsLinkedList() {
        LinkedList<Cuenta> linkedList = new LinkedList<>();
        List<Cuenta> cuentas = findAll();
        for (Cuenta cuenta : cuentas) {
            linkedList.add(cuenta);
        }
        return linkedList;
    }

    default LinkedList<Cuenta> buscarPorTextoAsLinkedList(String texto) {
        LinkedList<Cuenta> linkedList = new LinkedList<>();
        List<Cuenta> cuentas = buscarPorTexto(texto);
        for (Cuenta cuenta : cuentas) {
            linkedList.add(cuenta);
        }
        return linkedList;
    }

    default LinkedList<Cuenta> findByActivoAsLinkedList(Boolean activo) {
        LinkedList<Cuenta> linkedList = new LinkedList<>();
        List<Cuenta> cuentas = findByActivo(activo);
        for (Cuenta cuenta : cuentas) {
            linkedList.add(cuenta);
        }
        return linkedList;
    }

    default LinkedList<Cuenta> findByRolAsLinkedList(Rol rol) {
        LinkedList<Cuenta> linkedList = new LinkedList<>();
        List<Cuenta> cuentas = findByRol(rol);
        for (Cuenta cuenta : cuentas) {
            linkedList.add(cuenta);
        }
        return linkedList;
    }

    default LinkedList<Cuenta> findAllOrderedAsLinkedList(String campo, boolean ascendente) {
        LinkedList<Cuenta> linkedList = new LinkedList<>();
        List<Cuenta> cuentas;
        
        switch (campo.toLowerCase()) {
            case "usuario":
                cuentas = ascendente ? findAllByOrderByUsuarioAsc() : 
                          findAll().stream().sorted((c1, c2) -> c2.getUsuario().compareTo(c1.getUsuario())).toList();
                break;
            case "fechacreacion":
                cuentas = ascendente ? 
                          findAll().stream().sorted((c1, c2) -> c1.getFechaCreacion().compareTo(c2.getFechaCreacion())).toList() :
                          findAllByOrderByFechaCreacionDesc();
                break;
            case "ultimoacceso":
                cuentas = ascendente ? 
                          findAll().stream().sorted((c1, c2) -> {
                              if (c1.getUltimoAcceso() == null && c2.getUltimoAcceso() == null) return 0;
                              if (c1.getUltimoAcceso() == null) return 1;
                              if (c2.getUltimoAcceso() == null) return -1;
                              return c1.getUltimoAcceso().compareTo(c2.getUltimoAcceso());
                          }).toList() : findAllByOrderByUltimoAccesoDesc();
                break;
            case "persona":
                cuentas = ascendente ? findAllOrderByPersonaNombre() : 
                          findAllOrderByPersonaNombre().stream().sorted((c1, c2) -> 
                              c2.getPersona().getNombreCompleto().compareTo(c1.getPersona().getNombreCompleto())).toList();
                break;
            case "rol":
                cuentas = ascendente ? findAllOrderByRolNombre() : 
                          findAllOrderByRolNombre().stream().sorted((c1, c2) -> 
                              c2.getRol().getNombre().compareTo(c1.getRol().getNombre())).toList();
                break;
            default:
                cuentas = findAll();
        }
        
        for (Cuenta cuenta : cuentas) {
            linkedList.add(cuenta);
        }
        return linkedList;
    }

    // Validaciones para las vistas
    default boolean esUsuarioUnico(String usuario, Long idExcluir) {
        Optional<Cuenta> cuentaExistente = findByUsuario(usuario);
        return cuentaExistente.isEmpty() || 
               (idExcluir != null && cuentaExistente.get().getId().equals(idExcluir));
    }

    default boolean personaTieneCuenta(Long personaId, Long idExcluir) {
        Optional<Cuenta> cuentaExistente = findByPersonaId(personaId);
        return cuentaExistente.isPresent() && 
               (idExcluir == null || !cuentaExistente.get().getId().equals(idExcluir));
    }

    default boolean sePuedeEliminar(Long id) {
        // Aquí puedes agregar lógica adicional si hay otras entidades relacionadas
        return findById(id).isPresent();
    }

    // Búsquedas avanzadas para filtros en vistas
    @Query("SELECT c FROM Cuenta c WHERE " +
           "(:usuario IS NULL OR LOWER(c.usuario) LIKE LOWER(CONCAT('%', :usuario, '%'))) AND " +
           "(:activo IS NULL OR c.activo = :activo) AND " +
           "(:rolId IS NULL OR c.rol.id = :rolId) AND " +
           "(:personaNombre IS NULL OR LOWER(c.persona.nombres) LIKE LOWER(CONCAT('%', :personaNombre, '%')) OR LOWER(c.persona.apellidos) LIKE LOWER(CONCAT('%', :personaNombre, '%'))) AND " +
           "(:fechaInicio IS NULL OR c.fechaCreacion >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR c.fechaCreacion <= :fechaFin)")
    List<Cuenta> buscarConFiltros(@Param("usuario") String usuario,
                                  @Param("activo") Boolean activo,
                                  @Param("rolId") Long rolId,
                                  @Param("personaNombre") String personaNombre,
                                  @Param("fechaInicio") LocalDateTime fechaInicio,
                                  @Param("fechaFin") LocalDateTime fechaFin);

    // Estadísticas adicionales
    @Query("SELECT COUNT(c) FROM Cuenta c")
    Long contarTotalCuentas();

    @Query("SELECT COUNT(c) FROM Cuenta c WHERE c.ultimoAcceso IS NOT NULL")
    Long contarCuentasConAcceso();

    @Query("SELECT COUNT(c) FROM Cuenta c WHERE c.ultimoAcceso IS NULL")
    Long contarCuentasSinAcceso();

    @Query("SELECT COUNT(c) FROM Cuenta c WHERE c.ultimoAcceso >= :fecha")
    Long contarCuentasActivasDesde(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT AVG(c.intentosFallidos) FROM Cuenta c")
    Double obtenerPromedioIntentosFallidos();

    // Reportes para administradores
    @Query("SELECT c FROM Cuenta c WHERE c.ultimoAcceso < :fecha OR c.ultimoAcceso IS NULL")
    List<Cuenta> findCuentasInactivasDesde(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT c FROM Cuenta c WHERE c.intentosFallidos >= :intentos")
    List<Cuenta> findCuentasConMuchosIntentosFallidos(@Param("intentos") Integer intentos);
}