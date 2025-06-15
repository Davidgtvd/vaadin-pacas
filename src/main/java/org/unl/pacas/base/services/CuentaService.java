package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unl.pacas.base.dao.CuentaDao;
import org.unl.pacas.base.models.Cuenta;
import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.models.Sexo;
import org.unl.pacas.base.models.TipoIdentificacion;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class CuentaService {
    
    @Autowired
    private CuentaDao cuentaDao;
    
    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private RolService rolService;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Patrones de validación
    private static final Pattern USUARIO_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");
    private static final int MAX_INTENTOS_FALLIDOS = 3;
    private static final int HORAS_BLOQUEO = 24;
    
    // Operaciones CRUD básicas
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaDao.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Cuenta> findById(Long id) {
        return cuentaDao.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cuenta> findByUsuario(String usuario) {
        return cuentaDao.findByUsuario(usuario);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cuenta> findByPersonaId(Long personaId) {
        return cuentaDao.findByPersonaId(personaId);
    }
    
    public Cuenta save(Cuenta cuenta) {
        return cuentaDao.save(cuenta);
    }
    
    public Cuenta update(Cuenta cuenta) {
        if (cuenta.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar una cuenta sin ID");
        }
        return cuentaDao.save(cuenta);
    }
    
    public void deleteById(Long id) {
        if (!sePuedeEliminar(id)) {
            throw new IllegalStateException("No se puede eliminar la cuenta");
        }
        cuentaDao.deleteById(id);
    }
    
    public void delete(Cuenta cuenta) {
        deleteById(cuenta.getId());
    }
    
    // Validaciones
    @Transactional(readOnly = true)
    public boolean existsByUsuario(String usuario) {
        return cuentaDao.existsByUsuario(usuario);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByPersonaId(Long personaId) {
        return cuentaDao.existsByPersonaId(personaId);
    }
    
    @Transactional(readOnly = true)
    public boolean esUsuarioUnico(String usuario, Long idExcluir) {
        return cuentaDao.esUsuarioUnico(usuario, idExcluir);
    }
    
    @Transactional(readOnly = true)
    public boolean personaTieneCuenta(Long personaId, Long idExcluir) {
        return cuentaDao.personaTieneCuenta(personaId, idExcluir);
    }
    
    @Transactional(readOnly = true)
    public boolean sePuedeEliminar(Long id) {
        return cuentaDao.sePuedeEliminar(id);
    }
    
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return cuentaDao.existsById(id);
    }
    
    // Búsquedas por estado
    @Transactional(readOnly = true)
    public List<Cuenta> findByActivo(Boolean activo) {
        return cuentaDao.findByActivo(activo);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findCuentasActivas() {
        return cuentaDao.findByActivoTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findCuentasInactivas() {
        return cuentaDao.findByActivoFalse();
    }
    
    // Búsquedas por rol
    @Transactional(readOnly = true)
    public List<Cuenta> findByRol(Rol rol) {
        return cuentaDao.findByRol(rol);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findByRolId(Long rolId) {
        return cuentaDao.findByRolId(rolId);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findByRolNombre(String nombreRol) {
        return cuentaDao.findByRol_Nombre(nombreRol);
    }
    
    // Búsquedas por fechas
    @Transactional(readOnly = true)
    public List<Cuenta> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin) {
        return cuentaDao.findByFechaCreacionBetween(inicio, fin);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findByUltimoAccesoBetween(LocalDateTime inicio, LocalDateTime fin) {
        return cuentaDao.findByUltimoAccesoBetween(inicio, fin);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findCuentasSinAcceso() {
        return cuentaDao.findCuentasSinAcceso();
    }
    
    // Búsquedas por seguridad
    @Transactional(readOnly = true)
    public List<Cuenta> findCuentasBloqueadas() {
        return cuentaDao.findCuentasBloqueadas(LocalDateTime.now().minusHours(HORAS_BLOQUEO));
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findCuentasNoBloqueadas() {
        return cuentaDao.findCuentasNoBloqueadas(LocalDateTime.now().minusHours(HORAS_BLOQUEO));
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findByIntentosFallidosGreaterThan(Integer intentos) {
        return cuentaDao.findByIntentosFallidosGreaterThan(intentos);
    }
    
    // Búsquedas por datos de persona
    @Transactional(readOnly = true)
    public List<Cuenta> findByPersonaNombre(String nombre) {
        return cuentaDao.findByPersonaNombre(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findByPersonaEmail(String email) {
        return cuentaDao.findByPersonaEmail(email);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findByPersonaSexo(Sexo sexo) {
        return cuentaDao.findByPersonaSexo(sexo);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findByPersonaTipoIdentificacion(TipoIdentificacion tipo) {
        return cuentaDao.findByPersonaTipoIdentificacion(tipo);
    }
    
    // Búsqueda general
    @Transactional(readOnly = true)
    public List<Cuenta> buscarPorTexto(String texto) {
        return cuentaDao.buscarPorTexto(texto);
    }
    
    // Ordenamiento
    @Transactional(readOnly = true)
    public List<Cuenta> findAllOrdenados(String campo, boolean ascendente) {
        switch (campo.toLowerCase()) {
            case "usuario":
                return ascendente ? cuentaDao.findAllByOrderByUsuarioAsc() : 
                       cuentaDao.findAll().stream().sorted((c1, c2) -> c2.getUsuario().compareTo(c1.getUsuario())).toList();
            case "fechacreacion":
                return ascendente ? 
                       cuentaDao.findAll().stream().sorted((c1, c2) -> c1.getFechaCreacion().compareTo(c2.getFechaCreacion())).toList() :
                       cuentaDao.findAllByOrderByFechaCreacionDesc();
            case "ultimoacceso":
                return ascendente ? 
                       cuentaDao.findAll().stream().sorted((c1, c2) -> {
                           if (c1.getUltimoAcceso() == null && c2.getUltimoAcceso() == null) return 0;
                           if (c1.getUltimoAcceso() == null) return 1;
                           if (c2.getUltimoAcceso() == null) return -1;
                           return c1.getUltimoAcceso().compareTo(c2.getUltimoAcceso());
                       }).toList() : cuentaDao.findAllByOrderByUltimoAccesoDesc();
            case "persona":
                return ascendente ? cuentaDao.findAllOrderByPersonaNombre() : 
                       cuentaDao.findAllOrderByPersonaNombre().stream().sorted((c1, c2) -> 
                           c2.getPersona().getNombreCompleto().compareTo(c1.getPersona().getNombreCompleto())).toList();
            case "rol":
                return ascendente ? cuentaDao.findAllOrderByRolNombre() : 
                       cuentaDao.findAllOrderByRolNombre().stream().sorted((c1, c2) -> 
                           c2.getRol().getNombre().compareTo(c1.getRol().getNombre())).toList();
            default:
                return cuentaDao.findAll();
        }
    }
    
    // Métodos para LinkedList
    @Transactional(readOnly = true)
    public LinkedList<Cuenta> findAllAsLinkedList() {
        return cuentaDao.findAllAsLinkedList();
    }
    
    @Transactional(readOnly = true)
    public LinkedList<Cuenta> buscarPorTextoAsLinkedList(String texto) {
        return cuentaDao.buscarPorTextoAsLinkedList(texto);
    }
    
    @Transactional(readOnly = true)
    public LinkedList<Cuenta> findByActivoAsLinkedList(Boolean activo) {
        return cuentaDao.findByActivoAsLinkedList(activo);
    }
    
    @Transactional(readOnly = true)
    public LinkedList<Cuenta> findByRolAsLinkedList(Rol rol) {
        return cuentaDao.findByRolAsLinkedList(rol);
    }
    
    @Transactional(readOnly = true)
    public LinkedList<Cuenta> findAllOrderedAsLinkedList(String campo, boolean ascendente) {
        return cuentaDao.findAllOrderedAsLinkedList(campo, ascendente);
    }
    
    // Búsquedas con filtros
    @Transactional(readOnly = true)
    public List<Cuenta> buscarConFiltros(String usuario, Boolean activo, Long rolId, 
                                        String personaNombre, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return cuentaDao.buscarConFiltros(usuario, activo, rolId, personaNombre, fechaInicio, fechaFin);
    }
    
    // Autenticación y seguridad
    @Transactional(readOnly = true)
    public Optional<Cuenta> findCuentaParaLogin(String usuario) {
        return cuentaDao.findCuentaParaLogin(usuario, LocalDateTime.now().minusHours(HORAS_BLOQUEO));
    }
    
    public Optional<Cuenta> login(String usuario, String contrasena) {
        Optional<Cuenta> cuentaOpt = findCuentaParaLogin(usuario);
        
        if (cuentaOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Cuenta cuenta = cuentaOpt.get();
        
        if (!cuenta.puedeIniciarSesion()) {
            return Optional.empty();
        }
        
        if (passwordEncoder.matches(contrasena, cuenta.getContrasena())) {
            // Login exitoso
            cuenta.reiniciarIntentosFallidos();
            cuenta.actualizarUltimoAcceso();
            save(cuenta);
            return Optional.of(cuenta);
        } else {
            // Login fallido
            cuenta.incrementarIntentosFallidos();
            save(cuenta);
            return Optional.empty();
        }
    }
    
    public void actualizarUltimoAcceso(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.actualizarUltimoAcceso();
            save(cuenta);
        }
    }
    
    public void bloquearCuenta(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setFechaBloqueo(LocalDateTime.now());
            cuenta.setIntentosFallidos(MAX_INTENTOS_FALLIDOS);
            save(cuenta);
        }
    }
    
    public void desbloquearCuenta(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.reiniciarIntentosFallidos();
            save(cuenta);
        }
    }
    
    public void activarCuenta(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setActivo(true);
            save(cuenta);
        }
    }
    
    public void desactivarCuenta(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setActivo(false);
            save(cuenta);
        }
    }
    
    public void cambiarContrasena(Long cuentaId, String nuevaContrasena) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setContrasena(passwordEncoder.encode(nuevaContrasena));
            save(cuenta);
        }
    }
    
    // Estadísticas
    @Transactional(readOnly = true)
    public Long contarTotal() {
        return cuentaDao.contarTotalCuentas();
    }
    
    @Transactional(readOnly = true)
    public Long contarCuentasActivas() {
        return cuentaDao.contarCuentasActivas();
    }
    
    @Transactional(readOnly = true)
    public Long contarCuentasInactivas() {
        return cuentaDao.contarCuentasInactivas();
    }
    
    @Transactional(readOnly = true)
    public Long contarCuentasBloqueadas() {
        return cuentaDao.contarCuentasBloqueadas(LocalDateTime.now().minusHours(HORAS_BLOQUEO));
    }
    
    @Transactional(readOnly = true)
    public Long contarCuentasConAcceso() {
        return cuentaDao.contarCuentasConAcceso();
    }
    
    @Transactional(readOnly = true)
    public Long contarCuentasSinAcceso() {
        return cuentaDao.contarCuentasSinAcceso();
    }
    
    @Transactional(readOnly = true)
    public Long contarCuentasActivasDesde(LocalDateTime fecha) {
        return cuentaDao.contarCuentasActivasDesde(fecha);
    }
    
    @Transactional(readOnly = true)
    public Double obtenerPromedioIntentosFallidos() {
        return cuentaDao.obtenerPromedioIntentosFallidos();
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorRol() {
        return cuentaDao.obtenerEstadisticasPorRol();
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorEstado() {
        return cuentaDao.obtenerEstadisticasPorEstado();
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorSexo() {
        return cuentaDao.obtenerEstadisticasPorSexo();
    }
    
    // Métodos utilitarios para las vistas
    public Cuenta crearCuenta(String usuario, String contrasena, Long rolId, Long personaId) {
        // Validar datos
        String error = validarCuenta(usuario, contrasena, rolId, personaId, null);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        
        Optional<Rol> rolOpt = rolService.findById(rolId);
        Optional<Persona> personaOpt = personaService.findById(personaId);
        
        if (rolOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el rol con ID: " + rolId);
        }
        
        if (personaOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la persona con ID: " + personaId);
        }
        
        Cuenta cuenta = new Cuenta(usuario, passwordEncoder.encode(contrasena), rolOpt.get(), personaOpt.get());
        return save(cuenta);
    }
    
    public Cuenta actualizarCuenta(Long id, String usuario, Long rolId, Boolean activo) {
        Optional<Cuenta> cuentaExistente = findById(id);
        if (cuentaExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la cuenta con ID: " + id);
        }
        
        // Validar datos (sin contraseña ni persona, ya que no se cambian en actualización)
        String error = validarActualizacionCuenta(usuario, rolId, id);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        
        Optional<Rol> rolOpt = rolService.findById(rolId);
        if (rolOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el rol con ID: " + rolId);
        }
        
        Cuenta cuenta = cuentaExistente.get();
        cuenta.setUsuario(usuario);
        cuenta.setRol(rolOpt.get());
        cuenta.setActivo(activo);
        
        return update(cuenta);
    }
    
    public void eliminarCuenta(Long id) {
        Optional<Cuenta> cuenta = findById(id);
        if (cuenta.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la cuenta con ID: " + id);
        }
        
        if (!sePuedeEliminar(id)) {
            throw new IllegalStateException("No se puede eliminar la cuenta '" + cuenta.get().getUsuario() + "'");
        }
        
        deleteById(id);
    }
    
    // Validaciones específicas
    @Transactional(readOnly = true)
    public boolean validarUsuario(String usuario) {
        return usuario != null && 
               usuario.length() >= 3 && 
               usuario.length() <= 50 && 
               USUARIO_PATTERN.matcher(usuario).matches();
    }
    
    @Transactional(readOnly = true)
    public boolean validarContrasena(String contrasena) {
        return contrasena != null && contrasena.length() >= 6;
    }
    
    @Transactional(readOnly = true)
    public String validarCuenta(String usuario, String contrasena, Long rolId, Long personaId, Long idExcluir) {
        if (!validarUsuario(usuario)) {
            return "El usuario debe tener entre 3 y 50 caracteres y solo puede contener letras, números, puntos, guiones y guiones bajos";
        }
        
        if (!esUsuarioUnico(usuario, idExcluir)) {
            return "Ya existe una cuenta con el usuario: " + usuario;
        }
        
        if (!validarContrasena(contrasena)) {
            return "La contraseña debe tener al menos 6 caracteres";
        }
        
        if (rolId == null || !rolService.existe(rolId)) {
            return "Debe seleccionar un rol válido";
        }
        
        if (personaId == null || !personaService.existe(personaId)) {
            return "Debe seleccionar una persona válida";
        }
        
        if (personaTieneCuenta(personaId, idExcluir)) {
            return "La persona seleccionada ya tiene una cuenta asociada";
        }
        
        return null; // Sin errores
    }
    
    @Transactional(readOnly = true)
    public String validarActualizacionCuenta(String usuario, Long rolId, Long idExcluir) {
        if (!validarUsuario(usuario)) {
            return "El usuario debe tener entre 3 y 50 caracteres y solo puede contener letras, números, puntos, guiones y guiones bajos";
        }
        
        if (!esUsuarioUnico(usuario, idExcluir)) {
            return "Ya existe otra cuenta con el usuario: " + usuario;
        }
        
        if (rolId == null || !rolService.existe(rolId)) {
            return "Debe seleccionar un rol válido";
        }
        
        return null; // Sin errores
    }
    
    // Reportes para administradores
    @Transactional(readOnly = true)
    public List<Cuenta> findCuentasInactivasDesde(LocalDateTime fecha) {
        return cuentaDao.findCuentasInactivasDesde(fecha);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> findCuentasConMuchosIntentosFallidos(Integer intentos) {
        return cuentaDao.findCuentasConMuchosIntentosFallidos(intentos);
    }
    
    @Transactional(readOnly = true)
    public List<Cuenta> generarReporteCuentas() {
        return findAllOrdenados("usuario", true);
    }
    
    @Transactional(readOnly = true)
    public String generarResumenEstadisticas() {
        Long total = contarTotal();
        Long activas = contarCuentasActivas();
        Long inactivas = contarCuentasInactivas();
        Long bloqueadas = contarCuentasBloqueadas();
        Long sinAcceso = contarCuentasSinAcceso();
        
        return String.format("Total: %d | Activas: %d | Inactivas: %d | Bloqueadas: %d | Sin acceso: %d", 
                           total, activas, inactivas, bloqueadas, sinAcceso);
    }
    
    // Métodos de utilidad para administración
    @Transactional(readOnly = true)
    public List<Persona> obtenerPersonasSinCuenta() {
        return personaService.findPersonasSinCuenta();
    }
    
    @Transactional(readOnly = true)
    public List<Rol> obtenerRolesDisponibles() {
        return rolService.findAll();
    }
    
    @Transactional(readOnly = true)
    public boolean puedeAsignarPersona(Long personaId, Long cuentaIdExcluir) {
        return !personaTieneCuenta(personaId, cuentaIdExcluir);
    }
}