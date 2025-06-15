package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unl.pacas.base.dao.dao_models.PersonaDao;
import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.models.Sexo;
import org.unl.pacas.base.models.TipoIdentificacion;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class PersonaService {
    
    @Autowired
    private PersonaDao personaDao;
    
    // Patrones de validación
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^[0-9+\\-\\s()]*$");
    private static final Pattern CEDULA_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern PASAPORTE_PATTERN = Pattern.compile("^[A-Z0-9]{6,9}$");
    private static final Pattern RUC_PATTERN = Pattern.compile("^[0-9]{13}$");
    
    // Operaciones CRUD básicas
    @Transactional(readOnly = true)
    public List<Persona> findAll() {
        return personaDao.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Persona> findById(Long id) {
        return personaDao.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Persona> findByEmail(String email) {
        return personaDao.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<Persona> findByIdentificacion(String identificacion) {
        return personaDao.findByIdentificacion(identificacion);
    }
    
    public Persona save(Persona persona) {
        return personaDao.save(persona);
    }
    
    public Persona update(Persona persona) {
        if (persona.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar una persona sin ID");
        }
        return personaDao.save(persona);
    }
    
    public void deleteById(Long id) {
        if (!sePuedeEliminar(id)) {
            throw new IllegalStateException("No se puede eliminar la persona porque tiene una cuenta asociada");
        }
        personaDao.deleteById(id);
    }
    
    public void delete(Persona persona) {
        deleteById(persona.getId());
    }
    
    // Validaciones
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return personaDao.existsByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByIdentificacion(String identificacion) {
        return personaDao.existsByIdentificacion(identificacion);
    }
    
    @Transactional(readOnly = true)
    public boolean esEmailUnico(String email, Long idExcluir) {
        return personaDao.esEmailUnico(email, idExcluir);
    }
    
    @Transactional(readOnly = true)
    public boolean esIdentificacionUnica(String identificacion, Long idExcluir) {
        return personaDao.esIdentificacionUnica(identificacion, idExcluir);
    }
    
    @Transactional(readOnly = true)
    public boolean sePuedeEliminar(Long id) {
        return personaDao.sePuedeEliminar(id);
    }
    
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return personaDao.existsById(id);
    }
    
    // Búsquedas avanzadas
    @Transactional(readOnly = true)
    public List<Persona> buscarPorNombres(String nombres) {
        return personaDao.findByNombresContainingIgnoreCase(nombres);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> buscarPorApellidos(String apellidos) {
        return personaDao.findByApellidosContainingIgnoreCase(apellidos);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> buscarPorNombreCompleto(String nombre) {
        return personaDao.buscarPorNombreCompleto(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> buscarPorTexto(String texto) {
        return personaDao.buscarPorTexto(texto);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> findBySexo(Sexo sexo) {
        return personaDao.findBySexo(sexo);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> findByTipoIdentificacion(TipoIdentificacion tipo) {
        return personaDao.findByTipoIdentificacion(tipo);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> buscarPorTelefono(String telefono) {
        return personaDao.findByTelefonoContaining(telefono);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> buscarPorDireccion(String direccion) {
        return personaDao.findByDireccionContainingIgnoreCase(direccion);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> findByFechaNacimientoBetween(LocalDate inicio, LocalDate fin) {
        return personaDao.findByFechaNacimientoBetween(inicio, fin);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> findByAñoNacimiento(int año) {
        return personaDao.findByAñoNacimiento(año);
    }
    
    @Transactional(readOnly = true)
    public List<Persona> findAllOrdenados(String campo, boolean ascendente) {
        switch (campo.toLowerCase()) {
            case "nombres":
                return ascendente ? personaDao.findAllByOrderByNombresAsc() : 
                       personaDao.findAll().stream().sorted((p1, p2) -> p2.getNombres().compareTo(p1.getNombres())).toList();
            case "apellidos":
                return ascendente ? personaDao.findAllByOrderByApellidosAsc() : 
                       personaDao.findAll().stream().sorted((p1, p2) -> p2.getApellidos().compareTo(p1.getApellidos())).toList();
            case "email":
                return ascendente ? personaDao.findAllByOrderByEmailAsc() : 
                       personaDao.findAll().stream().sorted((p1, p2) -> p2.getEmail().compareTo(p1.getEmail())).toList();
            case "fechanacimiento":
                return ascendente ? 
                       personaDao.findAll().stream().sorted((p1, p2) -> {
                           if (p1.getFechaNacimiento() == null && p2.getFechaNacimiento() == null) return 0;
                           if (p1.getFechaNacimiento() == null) return 1;
                           if (p2.getFechaNacimiento() == null) return -1;
                           return p1.getFechaNacimiento().compareTo(p2.getFechaNacimiento());
                       }).toList() : personaDao.findAllByOrderByFechaNacimientoDesc();
            default:
                return personaDao.findAll();
        }
    }
    
    @Transactional(readOnly = true)
    public List<Persona> findPersonasConCuenta() {
        return personaDao.findPersonasConCuenta();
    }
    
    @Transactional(readOnly = true)
    public List<Persona> findPersonasSinCuenta() {
        return personaDao.findPersonasSinCuenta();
    }
    
    // Métodos para LinkedList
    @Transactional(readOnly = true)
    public LinkedList<Persona> findAllAsLinkedList() {
        return personaDao.findAllAsLinkedList();
    }
    
    @Transactional(readOnly = true)
    public LinkedList<Persona> buscarPorTextoAsLinkedList(String texto) {
        return personaDao.buscarPorTextoAsLinkedList(texto);
    }
    
    @Transactional(readOnly = true)
    public LinkedList<Persona> findBySexoAsLinkedList(Sexo sexo) {
        return personaDao.findBySexoAsLinkedList(sexo);
    }
    
    @Transactional(readOnly = true)
    public LinkedList<Persona> findByTipoIdentificacionAsLinkedList(TipoIdentificacion tipo) {
        return personaDao.findByTipoIdentificacionAsLinkedList(tipo);
    }
    
    @Transactional(readOnly = true)
    public LinkedList<Persona> findAllOrderedAsLinkedList(String campo, boolean ascendente) {
        return personaDao.findAllOrderedAsLinkedList(campo, ascendente);
    }
    
    // Búsquedas con filtros
    @Transactional(readOnly = true)
    public List<Persona> buscarConFiltros(String nombres, String apellidos, String email, 
                                         Sexo sexo, TipoIdentificacion tipoIdentificacion) {
        return personaDao.buscarConFiltros(nombres, apellidos, email, sexo, tipoIdentificacion);
    }
    
    // Estadísticas
    @Transactional(readOnly = true)
    public Long contarTotal() {
        return personaDao.contarTotalPersonas();
    }
    
    @Transactional(readOnly = true)
    public Long contarPorSexo(Sexo sexo) {
        return personaDao.contarPorSexo(sexo);
    }
    
    @Transactional(readOnly = true)
    public Long contarPorTipoIdentificacion(TipoIdentificacion tipo) {
        return personaDao.contarPorTipoIdentificacion(tipo);
    }
    
    @Transactional(readOnly = true)
    public Long contarPersonasConCuenta() {
        return personaDao.contarPersonasConCuenta();
    }
    
    @Transactional(readOnly = true)
    public Long contarPersonasSinCuenta() {
        return personaDao.contarPersonasSinCuenta();
    }
    
    @Transactional(readOnly = true)
    public Double obtenerEdadPromedio() {
        return personaDao.obtenerEdadPromedio();
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorSexo() {
        return personaDao.obtenerEstadisticasPorSexo();
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorTipoIdentificacion() {
        return personaDao.obtenerEstadisticasPorTipoIdentificacion();
    }
    
    // Métodos utilitarios para las vistas
    public Persona crearPersona(String nombres, String apellidos, String email, 
                               TipoIdentificacion tipoIdentificacion, String identificacion, 
                               Sexo sexo, String telefono, String direccion, LocalDate fechaNacimiento) {
        
        // Validar datos
        String error = validarPersona(nombres, apellidos, email, tipoIdentificacion, identificacion, sexo, telefono, fechaNacimiento, null);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        
        Persona persona = new Persona(nombres, apellidos, email, tipoIdentificacion, identificacion, sexo);
        persona.setTelefono(telefono);
        persona.setDireccion(direccion);
        persona.setFechaNacimiento(fechaNacimiento);
        
        return save(persona);
    }
    
    public Persona actualizarPersona(Long id, String nombres, String apellidos, String email, 
                                   TipoIdentificacion tipoIdentificacion, String identificacion, 
                                   Sexo sexo, String telefono, String direccion, LocalDate fechaNacimiento) {
        
        Optional<Persona> personaExistente = findById(id);
        if (personaExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la persona con ID: " + id);
        }
        
        // Validar datos
        String error = validarPersona(nombres, apellidos, email, tipoIdentificacion, identificacion, sexo, telefono, fechaNacimiento, id);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        
        Persona persona = personaExistente.get();
        persona.setNombres(nombres);
        persona.setApellidos(apellidos);
        persona.setEmail(email);
        persona.setTipoIdentificacion(tipoIdentificacion);
        persona.setIdentificacion(identificacion);
        persona.setSexo(sexo);
        persona.setTelefono(telefono);
        persona.setDireccion(direccion);
        persona.setFechaNacimiento(fechaNacimiento);
        
        return update(persona);
    }
    
    public void eliminarPersona(Long id) {
        Optional<Persona> persona = findById(id);
        if (persona.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la persona con ID: " + id);
        }
        
        if (!sePuedeEliminar(id)) {
            throw new IllegalStateException("No se puede eliminar la persona '" + persona.get().getNombreCompleto() + 
                                          "' porque tiene una cuenta asociada");
        }
        
        deleteById(id);
    }
    
    // Validaciones específicas
    @Transactional(readOnly = true)
    public boolean validarEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    @Transactional(readOnly = true)
    public boolean validarTelefono(String telefono) {
        return telefono == null || telefono.trim().isEmpty() || TELEFONO_PATTERN.matcher(telefono).matches();
    }
    
    @Transactional(readOnly = true)
    public boolean validarIdentificacion(String identificacion, TipoIdentificacion tipo) {
        if (identificacion == null || tipo == null) return false;
        
        switch (tipo) {
            case CEDULA:
                return CEDULA_PATTERN.matcher(identificacion).matches() && validarCedulaEcuatoriana(identificacion);
            case PASAPORTE:
                return PASAPORTE_PATTERN.matcher(identificacion).matches();
            case RUC:
                return RUC_PATTERN.matcher(identificacion).matches() && validarRucEcuatoriano(identificacion);
            default:
                return false;
        }
    }
    
    @Transactional(readOnly = true)
    public boolean validarFechaNacimiento(LocalDate fecha) {
        return fecha == null || fecha.isBefore(LocalDate.now());
    }
    
    @Transactional(readOnly = true)
    public String validarPersona(String nombres, String apellidos, String email, 
                                TipoIdentificacion tipoIdentificacion, String identificacion, 
                                Sexo sexo, String telefono, LocalDate fechaNacimiento, Long idExcluir) {
        
        if (nombres == null || nombres.trim().isEmpty()) {
            return "Los nombres son obligatorios";
        }
        if (nombres.length() > 100) {
            return "Los nombres no pueden tener más de 100 caracteres";
        }
        
        if (apellidos == null || apellidos.trim().isEmpty()) {
            return "Los apellidos son obligatorios";
        }
        if (apellidos.length() > 100) {
            return "Los apellidos no pueden tener más de 100 caracteres";
        }
        
        if (!validarEmail(email)) {
            return "El email no tiene un formato válido";
        }
        if (!esEmailUnico(email, idExcluir)) {
            return "Ya existe una persona con el email: " + email;
        }
        
        if (tipoIdentificacion == null) {
            return "El tipo de identificación es obligatorio";
        }
        
        if (!validarIdentificacion(identificacion, tipoIdentificacion)) {
            return "La identificación no es válida para el tipo seleccionado";
        }
        if (!esIdentificacionUnica(identificacion, idExcluir)) {
            return "Ya existe una persona con la identificación: " + identificacion;
        }
        
        if (sexo == null) {
            return "El sexo es obligatorio";
        }
        
        if (!validarTelefono(telefono)) {
            return "El teléfono solo puede contener números, espacios y símbolos +, -, (, )";
        }
        
        if (!validarFechaNacimiento(fechaNacimiento)) {
            return "La fecha de nacimiento debe ser anterior a hoy";
        }
        
        return null; // Sin errores
    }
    
    // Validaciones específicas de Ecuador
    private boolean validarCedulaEcuatoriana(String cedula) {
        if (cedula.length() != 10) return false;
        
        try {
            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int suma = 0;
            int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));
            
            for (int i = 0; i < 9; i++) {
                int valor = Integer.parseInt(cedula.substring(i, i + 1)) * coeficientes[i];
                suma += valor > 9 ? valor - 9 : valor;
            }
            
            int resultado = suma % 10 == 0 ? 0 : 10 - suma % 10;
            return resultado == digitoVerificador;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean validarRucEcuatoriano(String ruc) {
        if (ruc.length() != 13) return false;
        
        // Validar que los últimos 3 dígitos sean 001
        if (!ruc.endsWith("001")) return false;
        
        // Validar los primeros 10 dígitos como cédula
        String cedula = ruc.substring(0, 10);
        return validarCedulaEcuatoriana(cedula);
    }
    
    // Métodos para reportes
    @Transactional(readOnly = true)
    public List<Persona> generarReportePersonas() {
        return findAllOrdenados("apellidos", true);
    }
    
    @Transactional(readOnly = true)
    public String generarResumenEstadisticas() {
        Long total = contarTotal();
        Long conCuenta = contarPersonasConCuenta();
        Long sinCuenta = contarPersonasSinCuenta();
        Double edadPromedio = obtenerEdadPromedio();
        
        return String.format("Total de personas: %d | Con cuenta: %d | Sin cuenta: %d | Edad promedio: %.1f años", 
                           total, conCuenta, sinCuenta, edadPromedio != null ? edadPromedio : 0.0);
    }
    
    // Métodos de utilidad
    @Transactional(readOnly = true)
    public List<Persona> buscarCandidatosParaCuenta() {
        return findPersonasSinCuenta();
    }
    
    @Transactional(readOnly = true)
    public boolean puedeCrearCuenta(Long personaId) {
        Optional<Persona> persona = findById(personaId);
        return persona.isPresent() && persona.get().getCuenta() == null;
    }
}