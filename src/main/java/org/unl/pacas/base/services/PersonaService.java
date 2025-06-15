package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.PersonaDao;
import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.models.Sexo;
import org.unl.pacas.base.models.TipoIdentificacion;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
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

    // CRUD básicos

    public List<Persona> findAll() {
        return linkedListToList(personaDao.findAll());
    }

    public Optional<Persona> findById(Long id) {
        return personaDao.findById(id);
    }

    public boolean existsById(Long id) {
        return personaDao.existsById(id);
    }

    public Optional<Persona> findByEmail(String email) {
        return personaDao.findByEmail(email);
    }

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

    public boolean existsByEmail(String email) {
        return personaDao.existsByEmail(email);
    }

    public boolean existsByIdentificacion(String identificacion) {
        return personaDao.existsByIdentificacion(identificacion);
    }

    public boolean esEmailUnico(String email, Long idExcluir) {
        Optional<Persona> personaOpt = personaDao.findByEmail(email);
        if (personaOpt.isEmpty()) return true;
        if (idExcluir == null) return false;
        return personaOpt.get().getId().equals(idExcluir);
    }

    public boolean esIdentificacionUnica(String identificacion, Long idExcluir) {
        Optional<Persona> personaOpt = personaDao.findByIdentificacion(identificacion);
        if (personaOpt.isEmpty()) return true;
        if (idExcluir == null) return false;
        return personaOpt.get().getId().equals(idExcluir);
    }

    public boolean sePuedeEliminar(Long id) {
        return personaDao.sePuedeEliminar(id);
    }

    // Búsquedas avanzadas adaptadas

    public List<Persona> buscarPorNombres(String nombres) {
        List<Persona> todas = findAll();
        List<Persona> filtradas = new ArrayList<>();
        if (nombres == null || nombres.trim().isEmpty()) return filtradas;
        String lower = nombres.toLowerCase();
        for (Persona p : todas) {
            if (p.getNombres() != null && p.getNombres().toLowerCase().contains(lower)) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public List<Persona> buscarPorApellidos(String apellidos) {
        List<Persona> todas = findAll();
        List<Persona> filtradas = new ArrayList<>();
        if (apellidos == null || apellidos.trim().isEmpty()) return filtradas;
        String lower = apellidos.toLowerCase();
        for (Persona p : todas) {
            if (p.getApellidos() != null && p.getApellidos().toLowerCase().contains(lower)) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public List<Persona> buscarPorNombreCompleto(String nombre) {
        List<Persona> todas = findAll();
        List<Persona> filtradas = new ArrayList<>();
        if (nombre == null || nombre.trim().isEmpty()) return filtradas;
        String lower = nombre.toLowerCase();
        for (Persona p : todas) {
            String completo = (p.getNombres() + " " + p.getApellidos()).toLowerCase();
            if (completo.contains(lower)) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public List<Persona> buscarPorTexto(String texto) {
        List<Persona> todas = findAll();
        List<Persona> filtradas = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) return filtradas;
        String lower = texto.toLowerCase();
        for (Persona p : todas) {
            if ((p.getNombres() != null && p.getNombres().toLowerCase().contains(lower)) ||
                (p.getApellidos() != null && p.getApellidos().toLowerCase().contains(lower)) ||
                (p.getEmail() != null && p.getEmail().toLowerCase().contains(lower)) ||
                (p.getIdentificacion() != null && p.getIdentificacion().toLowerCase().contains(lower))) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public List<Persona> findBySexo(Sexo sexo) {
        return linkedListToList(personaDao.findBySexoAsLinkedList(sexo));
    }

    public List<Persona> findByTipoIdentificacion(TipoIdentificacion tipo) {
        return linkedListToList(personaDao.findByTipoIdentificacionAsLinkedList(tipo));
    }

    public List<Persona> buscarPorTelefono(String telefono) {
        List<Persona> todas = findAll();
        List<Persona> filtradas = new ArrayList<>();
        if (telefono == null || telefono.trim().isEmpty()) return filtradas;
        String lower = telefono.toLowerCase();
        for (Persona p : todas) {
            if (p.getTelefono() != null && p.getTelefono().toLowerCase().contains(lower)) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public List<Persona> buscarPorDireccion(String direccion) {
        List<Persona> todas = findAll();
        List<Persona> filtradas = new ArrayList<>();
        if (direccion == null || direccion.trim().isEmpty()) return filtradas;
        String lower = direccion.toLowerCase();
        for (Persona p : todas) {
            if (p.getDireccion() != null && p.getDireccion().toLowerCase().contains(lower)) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public List<Persona> findByFechaNacimientoBetween(LocalDate inicio, LocalDate fin) {
        List<Persona> todas = findAll();
        List<Persona> filtradas = new ArrayList<>();
        if (inicio == null || fin == null) return filtradas;
        for (Persona p : todas) {
            if (p.getFechaNacimiento() != null &&
                ( !p.getFechaNacimiento().isBefore(inicio) && !p.getFechaNacimiento().isAfter(fin) )) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public List<Persona> findByAñoNacimiento(int año) {
        List<Persona> todas = findAll();
        List<Persona> filtradas = new ArrayList<>();
        for (Persona p : todas) {
            if (p.getFechaNacimiento() != null && p.getFechaNacimiento().getYear() == año) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public List<Persona> findAllOrdenados(String campo, boolean ascendente) {
        List<Persona> lista = findAll();
        lista.sort((p1, p2) -> {
            int cmp = 0;
            switch (campo.toLowerCase()) {
                case "nombres":
                    cmp = p1.getNombres().compareToIgnoreCase(p2.getNombres());
                    break;
                case "apellidos":
                    cmp = p1.getApellidos().compareToIgnoreCase(p2.getApellidos());
                    break;
                case "email":
                    cmp = p1.getEmail().compareToIgnoreCase(p2.getEmail());
                    break;
                case "fechanacimiento":
                    if (p1.getFechaNacimiento() == null && p2.getFechaNacimiento() == null) cmp = 0;
                    else if (p1.getFechaNacimiento() == null) cmp = 1;
                    else if (p2.getFechaNacimiento() == null) cmp = -1;
                    else cmp = p1.getFechaNacimiento().compareTo(p2.getFechaNacimiento());
                    break;
                default:
                    cmp = 0;
            }
            return ascendente ? cmp : -cmp;
        });
        return lista;
    }

    public List<Persona> findPersonasConCuenta() {
        List<Persona> todas = findAll();
        List<Persona> conCuenta = new ArrayList<>();
        for (Persona p : todas) {
            if (p.getCuenta() != null) {
                conCuenta.add(p);
            }
        }
        return conCuenta;
    }

    public List<Persona> findPersonasSinCuenta() {
        List<Persona> todas = findAll();
        List<Persona> sinCuenta = new ArrayList<>();
        for (Persona p : todas) {
            if (p.getCuenta() == null) {
                sinCuenta.add(p);
            }
        }
        return sinCuenta;
    }

    // Métodos utilitarios para las vistas

    public Persona crearPersona(String nombres, String apellidos, String email,
                               TipoIdentificacion tipoIdentificacion, String identificacion,
                               Sexo sexo, String telefono, String direccion, LocalDate fechaNacimiento) {

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

    public boolean validarEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean validarTelefono(String telefono) {
        return telefono == null || telefono.trim().isEmpty() || TELEFONO_PATTERN.matcher(telefono).matches();
    }

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

    public boolean validarFechaNacimiento(LocalDate fecha) {
        return fecha == null || fecha.isBefore(LocalDate.now());
    }

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

        if (!ruc.endsWith("001")) return false;

        String cedula = ruc.substring(0, 10);
        return validarCedulaEcuatoriana(cedula);
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
    public boolean existe(Long id) {
        return personaDao.existsById(id);
    }
}