package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.CuentaDao;
import org.unl.pacas.base.models.Cuenta;
import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CuentaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private RolService rolService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Pattern USUARIO_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");
    private static final int MAX_INTENTOS_FALLIDOS = 3;
    private static final int HORAS_BLOQUEO = 24;

    // Operaciones CRUD básicas

    public List<Cuenta> findAll() {
        return linkedListToList(cuentaDao.findAll());
    }

    public Optional<Cuenta> findById(Long id) {
        return cuentaDao.findById(id);
    }

    public boolean existsById(Long id) {
        return cuentaDao.existsById(id);
    }

    public Optional<Cuenta> findByUsuario(String usuario) {
        return cuentaDao.findByUsuario(usuario);
    }

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

    public boolean existsByUsuario(String usuario) {
        return cuentaDao.existsByUsuario(usuario);
    }

    public boolean existsByPersonaId(Long personaId) {
        return cuentaDao.existsByPersonaId(personaId);
    }

    public boolean esUsuarioUnico(String usuario, Long idExcluir) {
        Optional<Cuenta> cuentaOpt = cuentaDao.findByUsuario(usuario);
        if (cuentaOpt.isEmpty()) return true;
        if (idExcluir == null) return false;
        return cuentaOpt.get().getId().equals(idExcluir);
    }

    public boolean personaTieneCuenta(Long personaId, Long idExcluir) {
        Optional<Cuenta> cuentaOpt = cuentaDao.findByPersonaId(personaId);
        if (cuentaOpt.isEmpty()) return false;
        if (idExcluir == null) return true;
        return !cuentaOpt.get().getId().equals(idExcluir);
    }

    public boolean sePuedeEliminar(Long id) {
        return cuentaDao.existsById(id);
    }

    // Métodos de búsqueda y filtrado adaptados

    public List<Cuenta> findByActivo(Boolean activo) {
        return linkedListToList(cuentaDao.findByActivoAsLinkedList(activo));
    }

    public List<Cuenta> findCuentasActivas() {
        return findByActivo(true);
    }

    public List<Cuenta> findCuentasInactivas() {
        return findByActivo(false);
    }

    public List<Cuenta> findByRol(Rol rol) {
        return linkedListToList(cuentaDao.findByRolAsLinkedList(rol));
    }

    public List<Cuenta> findByRolId(Long rolId) {
        Rol rol = new Rol();
        rol.setId(rolId);
        return findByRol(rol);
    }

    public List<Cuenta> findByRolNombre(String nombreRol) {
        List<Cuenta> todas = findAll();
        List<Cuenta> filtradas = new ArrayList<>();
        for (Cuenta c : todas) {
            if (c.getRol() != null && c.getRol().getNombre() != null &&
                c.getRol().getNombre().equalsIgnoreCase(nombreRol)) {
                filtradas.add(c);
            }
        }
        return filtradas;
    }

    public List<Cuenta> buscarPorTexto(String texto) {
        return linkedListToList(cuentaDao.buscarPorTextoAsLinkedList(texto));
    }

    public List<Cuenta> findAllOrdenados(String campo, boolean ascendente) {
        List<Cuenta> lista = findAll();
        lista.sort((c1, c2) -> {
            int cmp = 0;
            switch (campo.toLowerCase()) {
                case "usuario":
                    cmp = c1.getUsuario().compareToIgnoreCase(c2.getUsuario());
                    break;
                case "fechacreacion":
                    cmp = c1.getFechaCreacion().compareTo(c2.getFechaCreacion());
                    break;
                case "ultimoacceso":
                    if (c1.getUltimoAcceso() == null && c2.getUltimoAcceso() == null) cmp = 0;
                    else if (c1.getUltimoAcceso() == null) cmp = 1;
                    else if (c2.getUltimoAcceso() == null) cmp = -1;
                    else cmp = c1.getUltimoAcceso().compareTo(c2.getUltimoAcceso());
                    break;
                case "persona":
                    cmp = c1.getPersona().getNombreCompleto().compareToIgnoreCase(c2.getPersona().getNombreCompleto());
                    break;
                case "rol":
                    cmp = c1.getRol().getNombre().compareToIgnoreCase(c2.getRol().getNombre());
                    break;
                default:
                    cmp = 0;
            }
            return ascendente ? cmp : -cmp;
        });
        return lista;
    }

    // Métodos para LinkedList

    public LinkedList<Cuenta> findAllAsLinkedList() {
        return cuentaDao.findAll();
    }

    public LinkedList<Cuenta> buscarPorTextoAsLinkedList(String texto) {
        return cuentaDao.buscarPorTextoAsLinkedList(texto);
    }

    public LinkedList<Cuenta> findByActivoAsLinkedList(Boolean activo) {
        return cuentaDao.findByActivoAsLinkedList(activo);
    }

    public LinkedList<Cuenta> findByRolAsLinkedList(Rol rol) {
        return cuentaDao.findByRolAsLinkedList(rol);
    }

    public LinkedList<Cuenta> findAllOrderedAsLinkedList(String campo, boolean ascendente) {
        return cuentaDao.findAllOrderedAsLinkedList(campo, ascendente);
    }

    // Métodos de autenticación y seguridad

    public Optional<Cuenta> findCuentaParaLogin(String usuario) {
        return cuentaDao.findByUsuario(usuario);
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

        if (passwordEncoder.matches(contrasena, cuenta.getClave())) {
            cuenta.reiniciarIntentosFallidos();
            cuenta.actualizarUltimoAcceso();
            save(cuenta);
            return Optional.of(cuenta);
        } else {
            cuenta.incrementarIntentosFallidos();
            save(cuenta);
            return Optional.empty();
        }
    }

    public void actualizarUltimoAcceso(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        cuentaOpt.ifPresent(cuenta -> {
            cuenta.actualizarUltimoAcceso();
            save(cuenta);
        });
    }

    public void bloquearCuenta(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        cuentaOpt.ifPresent(cuenta -> {
            cuenta.setFechaBloqueo(LocalDateTime.now());
            cuenta.setIntentosFallidos(MAX_INTENTOS_FALLIDOS);
            save(cuenta);
        });
    }

    public void desbloquearCuenta(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        cuentaOpt.ifPresent(cuenta -> {
            cuenta.reiniciarIntentosFallidos();
            save(cuenta);
        });
    }

    public void activarCuenta(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        cuentaOpt.ifPresent(cuenta -> {
            cuenta.setActivo(true);
            save(cuenta);
        });
    }

    public void desactivarCuenta(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        cuentaOpt.ifPresent(cuenta -> {
            cuenta.setActivo(false);
            save(cuenta);
        });
    }

    public void cambiarContrasena(Long cuentaId, String nuevaContrasena) {
        Optional<Cuenta> cuentaOpt = findById(cuentaId);
        cuentaOpt.ifPresent(cuenta -> {
            cuenta.setClave(passwordEncoder.encode(nuevaContrasena));
            save(cuenta);
        });
    }

    // Estadísticas

    public long contarTotal() {
        return cuentaDao.contarTotalCuentas();
    }

    public long contarCuentasActivas() {
        return cuentaDao.contarCuentasActivas();
    }

    public long contarCuentasInactivas() {
        return cuentaDao.contarCuentasInactivas();
    }

    // Métodos utilitarios para las vistas

    public Cuenta crearCuenta(String usuario, String contrasena, Long rolId, Long personaId) {
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

    public boolean validarUsuario(String usuario) {
        return usuario != null &&
                usuario.length() >= 3 &&
                usuario.length() <= 50 &&
                USUARIO_PATTERN.matcher(usuario).matches();
    }

    public boolean validarContrasena(String contrasena) {
        return contrasena != null && contrasena.length() >= 6;
    }

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

    // Método auxiliar para convertir LinkedList a List
    private <T> List<T> linkedListToList(LinkedList<T> linkedList) {
        List<T> list = new ArrayList<>();
        if (linkedList != null) {
            linkedList.forEach(list::add);
        }
        return list;
    }
}