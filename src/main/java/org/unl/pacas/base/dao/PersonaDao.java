package org.unl.pacas.base.dao;

import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.models.Sexo;
import org.unl.pacas.base.models.TipoIdentificacion;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonaDao {

    private final LinkedList<Persona> personas = new LinkedList<>();

    public LinkedList<Persona> findAll() {
        return personas;
    }

    public Optional<Persona> findById(Long id) {
        for (Persona p : personas) {
            if (p.getId() != null && p.getId().equals(id)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public Optional<Persona> findByEmail(String email) {
        if (email == null) return Optional.empty();
        for (Persona p : personas) {
            if (email.equalsIgnoreCase(p.getEmail())) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public Optional<Persona> findByIdentificacion(String identificacion) {
        if (identificacion == null) return Optional.empty();
        for (Persona p : personas) {
            if (identificacion.equalsIgnoreCase(p.getIdentificacion())) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public boolean existsByIdentificacion(String identificacion) {
        return findByIdentificacion(identificacion).isPresent();
    }

    public List<Persona> findByNombresContainingIgnoreCase(String nombres) {
        List<Persona> result = new ArrayList<>();
        if (nombres == null) return result;
        String lower = nombres.toLowerCase();
        for (Persona p : personas) {
            if (p.getNombres() != null && p.getNombres().toLowerCase().contains(lower)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> findByApellidosContainingIgnoreCase(String apellidos) {
        List<Persona> result = new ArrayList<>();
        if (apellidos == null) return result;
        String lower = apellidos.toLowerCase();
        for (Persona p : personas) {
            if (p.getApellidos() != null && p.getApellidos().toLowerCase().contains(lower)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> buscarPorNombreCompleto(String nombre) {
        List<Persona> result = new ArrayList<>();
        if (nombre == null) return result;
        String lower = nombre.toLowerCase();
        for (Persona p : personas) {
            if ((p.getNombres() != null && p.getNombres().toLowerCase().contains(lower)) ||
                (p.getApellidos() != null && p.getApellidos().toLowerCase().contains(lower))) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> findBySexo(Sexo sexo) {
        List<Persona> result = new ArrayList<>();
        if (sexo == null) return result;
        for (Persona p : personas) {
            if (sexo.equals(p.getSexo())) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> findByTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        List<Persona> result = new ArrayList<>();
        if (tipoIdentificacion == null) return result;
        for (Persona p : personas) {
            if (tipoIdentificacion.equals(p.getTipoIdentificacion())) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> findByTelefonoContaining(String telefono) {
        List<Persona> result = new ArrayList<>();
        if (telefono == null) return result;
        String lower = telefono.toLowerCase();
        for (Persona p : personas) {
            if (p.getTelefono() != null && p.getTelefono().toLowerCase().contains(lower)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> findByDireccionContainingIgnoreCase(String direccion) {
        List<Persona> result = new ArrayList<>();
        if (direccion == null) return result;
        String lower = direccion.toLowerCase();
        for (Persona p : personas) {
            if (p.getDireccion() != null && p.getDireccion().toLowerCase().contains(lower)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> findByFechaNacimientoBetween(LocalDate inicio, LocalDate fin) {
        List<Persona> result = new ArrayList<>();
        if (inicio == null || fin == null) return result;
        for (Persona p : personas) {
            if (p.getFechaNacimiento() != null &&
                ( !p.getFechaNacimiento().isBefore(inicio) && !p.getFechaNacimiento().isAfter(fin) )) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> findByAñoNacimiento(int año) {
        List<Persona> result = new ArrayList<>();
        for (Persona p : personas) {
            if (p.getFechaNacimiento() != null && p.getFechaNacimiento().getYear() == año) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> buscarPorTexto(String texto) {
        List<Persona> result = new ArrayList<>();
        if (texto == null) return result;
        String lower = texto.toLowerCase();
        for (Persona p : personas) {
            if ((p.getNombres() != null && p.getNombres().toLowerCase().contains(lower)) ||
                (p.getApellidos() != null && p.getApellidos().toLowerCase().contains(lower)) ||
                (p.getEmail() != null && p.getEmail().toLowerCase().contains(lower)) ||
                (p.getIdentificacion() != null && p.getIdentificacion().toLowerCase().contains(lower)) ||
                (p.getTelefono() != null && p.getTelefono().toLowerCase().contains(lower)) ||
                (p.getDireccion() != null && p.getDireccion().toLowerCase().contains(lower))) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> findAllByOrderByNombresAsc() {
        List<Persona> list = new ArrayList<>();
        personas.forEach(list::add);
        list.sort((p1, p2) -> {
            if (p1.getNombres() == null) return 1;
            if (p2.getNombres() == null) return -1;
            return p1.getNombres().compareToIgnoreCase(p2.getNombres());
        });
        return list;
    }

    public List<Persona> findAllByOrderByApellidosAsc() {
        List<Persona> list = new ArrayList<>();
        personas.forEach(list::add);
        list.sort((p1, p2) -> {
            if (p1.getApellidos() == null) return 1;
            if (p2.getApellidos() == null) return -1;
            return p1.getApellidos().compareToIgnoreCase(p2.getApellidos());
        });
        return list;
    }

    public List<Persona> findAllByOrderByEmailAsc() {
        List<Persona> list = new ArrayList<>();
        personas.forEach(list::add);
        list.sort((p1, p2) -> {
            if (p1.getEmail() == null) return 1;
            if (p2.getEmail() == null) return -1;
            return p1.getEmail().compareToIgnoreCase(p2.getEmail());
        });
        return list;
    }

    public List<Persona> findAllByOrderByFechaNacimientoDesc() {
        List<Persona> list = new ArrayList<>();
        personas.forEach(list::add);
        list.sort((p1, p2) -> {
            if (p1.getFechaNacimiento() == null && p2.getFechaNacimiento() == null) return 0;
            if (p1.getFechaNacimiento() == null) return 1;
            if (p2.getFechaNacimiento() == null) return -1;
            return p2.getFechaNacimiento().compareTo(p1.getFechaNacimiento());
        });
        return list;
    }

    public List<Persona> findPersonasConCuenta() {
        List<Persona> result = new ArrayList<>();
        for (Persona p : personas) {
            if (p.getCuenta() != null) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Persona> findPersonasSinCuenta() {
        List<Persona> result = new ArrayList<>();
        for (Persona p : personas) {
            if (p.getCuenta() == null) {
                result.add(p);
            }
        }
        return result;
    }

    public long contarPorSexo(Sexo sexo) {
        long count = 0;
        for (Persona p : personas) {
            if (sexo.equals(p.getSexo())) count++;
        }
        return count;
    }

    public long contarPorTipoIdentificacion(TipoIdentificacion tipo) {
        long count = 0;
        for (Persona p : personas) {
            if (tipo.equals(p.getTipoIdentificacion())) count++;
        }
        return count;
    }

    public long contarTotalPersonas() {
        return personas.size();
    }

    public long contarPersonasConCuenta() {
        long count = 0;
        for (Persona p : personas) {
            if (p.getCuenta() != null) count++;
        }
        return count;
    }

    public long contarPersonasSinCuenta() {
        long count = 0;
        for (Persona p : personas) {
            if (p.getCuenta() == null) count++;
        }
        return count;
    }

    public double obtenerEdadPromedio() {
        int totalEdad = 0;
        int count = 0;
        for (Persona p : personas) {
            if (p.getFechaNacimiento() != null) {
                int edad = java.time.LocalDate.now().getYear() - p.getFechaNacimiento().getYear();
                totalEdad += edad;
                count++;
            }
        }
        return count == 0 ? 0 : (double) totalEdad / count;
    }

    public Persona save(Persona persona) {
        if (persona.getId() == null) {
            long maxId = 0;
            for (Persona p : personas) {
                if (p.getId() != null && p.getId() > maxId) {
                    maxId = p.getId();
                }
            }
            persona.setId(maxId + 1);
            personas.add(persona);
        } else {
            Optional<Persona> existing = findById(persona.getId());
            if (existing.isPresent()) {
                Persona p = existing.get();
                p.setNombres(persona.getNombres());
                p.setApellidos(persona.getApellidos());
                p.setEmail(persona.getEmail());
                p.setIdentificacion(persona.getIdentificacion());
                p.setSexo(persona.getSexo());
                p.setTipoIdentificacion(persona.getTipoIdentificacion());
                p.setTelefono(persona.getTelefono());
                p.setDireccion(persona.getDireccion());
                p.setFechaNacimiento(persona.getFechaNacimiento());
                p.setCuenta(persona.getCuenta());
            } else {
                personas.add(persona);
            }
        }
        return persona;
    }

    public void deleteById(Long id) {
        personas.removeIf(p -> p.getId() != null && p.getId().equals(id));
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public boolean esEmailUnico(String email, Long idExcluir) {
        Optional<Persona> p = findByEmail(email);
        return p.isEmpty() || (idExcluir != null && p.get().getId().equals(idExcluir));
    }

    public boolean esIdentificacionUnica(String identificacion, Long idExcluir) {
        Optional<Persona> p = findByIdentificacion(identificacion);
        return p.isEmpty() || (idExcluir != null && p.get().getId().equals(idExcluir));
    }

    public boolean sePuedeEliminar(Long id) {
        Optional<Persona> p = findById(id);
        return p.isPresent() && p.get().getCuenta() == null;
    }

    public List<Persona> buscarConFiltros(String nombres, String apellidos, String email, Sexo sexo, TipoIdentificacion tipoIdentificacion) {
        List<Persona> result = new ArrayList<>();
        for (Persona p : personas) {
            if ((nombres == null || (p.getNombres() != null && p.getNombres().toLowerCase().contains(nombres.toLowerCase()))) &&
                (apellidos == null || (p.getApellidos() != null && p.getApellidos().toLowerCase().contains(apellidos.toLowerCase()))) &&
                (email == null || (p.getEmail() != null && p.getEmail().toLowerCase().contains(email.toLowerCase()))) &&
                (sexo == null || sexo.equals(p.getSexo())) &&
                (tipoIdentificacion == null || tipoIdentificacion.equals(p.getTipoIdentificacion()))
            ) {
                result.add(p);
            }
        }
        return result;
    }

    // Métodos para LinkedList (añadidos para evitar errores en servicio)

    public LinkedList<Persona> findBySexoAsLinkedList(Sexo sexo) {
        LinkedList<Persona> result = new LinkedList<>();
        if (sexo == null) return result;
        for (Persona p : personas) {
            if (sexo.equals(p.getSexo())) {
                result.add(p);
            }
        }
        return result;
    }

    public LinkedList<Persona> findByTipoIdentificacionAsLinkedList(TipoIdentificacion tipoIdentificacion) {
        LinkedList<Persona> result = new LinkedList<>();
        if (tipoIdentificacion == null) return result;
        for (Persona p : personas) {
            if (tipoIdentificacion.equals(p.getTipoIdentificacion())) {
                result.add(p);
            }
        }
        return result;
    }

    public LinkedList<Persona> buscarPorTextoAsLinkedList(String texto) {
        LinkedList<Persona> result = new LinkedList<>();
        if (texto == null) return result;
        String lower = texto.toLowerCase();
        for (Persona p : personas) {
            if ((p.getNombres() != null && p.getNombres().toLowerCase().contains(lower)) ||
                (p.getApellidos() != null && p.getApellidos().toLowerCase().contains(lower)) ||
                (p.getEmail() != null && p.getEmail().toLowerCase().contains(lower)) ||
                (p.getIdentificacion() != null && p.getIdentificacion().toLowerCase().contains(lower))) {
                result.add(p);
            }
        }
        return result;
    }

    public LinkedList<Persona> findAllOrderedAsLinkedList(String campo, boolean ascendente) {
        List<Persona> list = findAllOrdenados(campo, ascendente);
        LinkedList<Persona> linkedList = new LinkedList<>();
        for (Persona p : list) {
            linkedList.add(p);
        }
        return linkedList;
    }

    // Nuevo método findAllOrdenados
    public List<Persona> findAllOrdenados(String campo, boolean ascendente) {
        List<Persona> list = new ArrayList<>();
        personas.forEach(list::add);
        list.sort((p1, p2) -> {
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
        return list;
    }
}