package org.unl.pacas.base.dao.dao_models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.models.Sexo;
import org.unl.pacas.base.models.TipoIdentificacion;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaDao extends JpaRepository<Persona, Long> {

    // Búsquedas básicas
    Optional<Persona> findByEmail(String email);
    
    Optional<Persona> findByIdentificacion(String identificacion);
    
    boolean existsByEmail(String email);
    
    boolean existsByIdentificacion(String identificacion);

    // Búsquedas por nombre
    List<Persona> findByNombresContainingIgnoreCase(String nombres);
    
    List<Persona> findByApellidosContainingIgnoreCase(String apellidos);
    
    @Query("SELECT p FROM Persona p WHERE " +
           "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
           "LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Persona> buscarPorNombreCompleto(@Param("nombre") String nombre);

    // Búsquedas por atributos específicos
    List<Persona> findBySexo(Sexo sexo);
    
    List<Persona> findByTipoIdentificacion(TipoIdentificacion tipoIdentificacion);
    
    List<Persona> findByTelefonoContaining(String telefono);
    
    List<Persona> findByDireccionContainingIgnoreCase(String direccion);

    // Búsquedas por fecha
    List<Persona> findByFechaNacimientoBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    @Query("SELECT p FROM Persona p WHERE YEAR(p.fechaNacimiento) = :año")
    List<Persona> findByAñoNacimiento(@Param("año") int año);

    // Búsqueda general
    @Query("SELECT p FROM Persona p WHERE " +
           "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.identificacion) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.telefono) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.direccion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Persona> buscarPorTexto(@Param("texto") String texto);

    // Ordenamiento
    List<Persona> findAllByOrderByNombresAsc();
    
    List<Persona> findAllByOrderByApellidosAsc();
    
    List<Persona> findAllByOrderByEmailAsc();
    
    List<Persona> findAllByOrderByFechaNacimientoDesc();

    // Personas con y sin cuenta
    @Query("SELECT p FROM Persona p WHERE p.cuenta IS NOT NULL")
    List<Persona> findPersonasConCuenta();

    @Query("SELECT p FROM Persona p WHERE p.cuenta IS NULL")
    List<Persona> findPersonasSinCuenta();

    // Estadísticas
    @Query("SELECT COUNT(p) FROM Persona p WHERE p.sexo = :sexo")
    Long contarPorSexo(@Param("sexo") Sexo sexo);

    @Query("SELECT COUNT(p) FROM Persona p WHERE p.tipoIdentificacion = :tipo")
    Long contarPorTipoIdentificacion(@Param("tipo") TipoIdentificacion tipo);

    @Query("SELECT p.sexo, COUNT(p) FROM Persona p GROUP BY p.sexo")
    List<Object[]> obtenerEstadisticasPorSexo();

    @Query("SELECT p.tipoIdentificacion, COUNT(p) FROM Persona p GROUP BY p.tipoIdentificacion")
    List<Object[]> obtenerEstadisticasPorTipoIdentificacion();

    // Métodos personalizados para LinkedList
    default LinkedList<Persona> findAllAsLinkedList() {
        LinkedList<Persona> linkedList = new LinkedList<>();
        List<Persona> personas = findAll();
        for (Persona persona : personas) {
            linkedList.add(persona);
        }
        return linkedList;
    }

    default LinkedList<Persona> buscarPorTextoAsLinkedList(String texto) {
        LinkedList<Persona> linkedList = new LinkedList<>();
        List<Persona> personas = buscarPorTexto(texto);
        for (Persona persona : personas) {
            linkedList.add(persona);
        }
        return linkedList;
    }

    default LinkedList<Persona> findBySexoAsLinkedList(Sexo sexo) {
        LinkedList<Persona> linkedList = new LinkedList<>();
        List<Persona> personas = findBySexo(sexo);
        for (Persona persona : personas) {
            linkedList.add(persona);
        }
        return linkedList;
    }

    default LinkedList<Persona> findByTipoIdentificacionAsLinkedList(TipoIdentificacion tipo) {
        LinkedList<Persona> linkedList = new LinkedList<>();
        List<Persona> personas = findByTipoIdentificacion(tipo);
        for (Persona persona : personas) {
            linkedList.add(persona);
        }
        return linkedList;
    }

    default LinkedList<Persona> findAllOrderedAsLinkedList(String campo, boolean ascendente) {
        LinkedList<Persona> linkedList = new LinkedList<>();
        List<Persona> personas;
        
        switch (campo.toLowerCase()) {
            case "nombres":
                personas = ascendente ? findAllByOrderByNombresAsc() : 
                          findAll().stream().sorted((p1, p2) -> p2.getNombres().compareTo(p1.getNombres())).toList();
                break;
            case "apellidos":
                personas = ascendente ? findAllByOrderByApellidosAsc() : 
                          findAll().stream().sorted((p1, p2) -> p2.getApellidos().compareTo(p1.getApellidos())).toList();
                break;
            case "email":
                personas = ascendente ? findAllByOrderByEmailAsc() : 
                          findAll().stream().sorted((p1, p2) -> p2.getEmail().compareTo(p1.getEmail())).toList();
                break;
            case "fechaNacimiento":
                personas = ascendente ? 
                          findAll().stream().sorted((p1, p2) -> {
                              if (p1.getFechaNacimiento() == null && p2.getFechaNacimiento() == null) return 0;
                              if (p1.getFechaNacimiento() == null) return 1;
                              if (p2.getFechaNacimiento() == null) return -1;
                              return p1.getFechaNacimiento().compareTo(p2.getFechaNacimiento());
                          }).toList() : findAllByOrderByFechaNacimientoDesc();
                break;
            default:
                personas = findAll();
        }
        
        for (Persona persona : personas) {
            linkedList.add(persona);
        }
        return linkedList;
    }

    // Validaciones para las vistas
    default boolean esEmailUnico(String email, Long idExcluir) {
        Optional<Persona> personaExistente = findByEmail(email);
        return personaExistente.isEmpty() || 
               (idExcluir != null && personaExistente.get().getId().equals(idExcluir));
    }

    default boolean esIdentificacionUnica(String identificacion, Long idExcluir) {
        Optional<Persona> personaExistente = findByIdentificacion(identificacion);
        return personaExistente.isEmpty() || 
               (idExcluir != null && personaExistente.get().getId().equals(idExcluir));
    }

    default boolean sePuedeEliminar(Long id) {
        Optional<Persona> persona = findById(id);
        return persona.isPresent() && persona.get().getCuenta() == null;
    }

    // Búsquedas avanzadas para filtros en vistas
    @Query("SELECT p FROM Persona p WHERE " +
           "(:nombres IS NULL OR LOWER(p.nombres) LIKE LOWER(CONCAT('%', :nombres, '%'))) AND " +
           "(:apellidos IS NULL OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :apellidos, '%'))) AND " +
           "(:email IS NULL OR LOWER(p.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:sexo IS NULL OR p.sexo = :sexo) AND " +
           "(:tipoIdentificacion IS NULL OR p.tipoIdentificacion = :tipoIdentificacion)")
    List<Persona> buscarConFiltros(@Param("nombres") String nombres,
                                   @Param("apellidos") String apellidos,
                                   @Param("email") String email,
                                   @Param("sexo") Sexo sexo,
                                   @Param("tipoIdentificacion") TipoIdentificacion tipoIdentificacion);

    // Estadísticas adicionales
    @Query("SELECT COUNT(p) FROM Persona p")
    Long contarTotalPersonas();

    @Query("SELECT COUNT(p) FROM Persona p WHERE p.cuenta IS NOT NULL")
    Long contarPersonasConCuenta();

    @Query("SELECT COUNT(p) FROM Persona p WHERE p.cuenta IS NULL")
    Long contarPersonasSinCuenta();

    @Query("SELECT AVG(YEAR(CURRENT_DATE) - YEAR(p.fechaNacimiento)) FROM Persona p WHERE p.fechaNacimiento IS NOT NULL")
    Double obtenerEdadPromedio();
}