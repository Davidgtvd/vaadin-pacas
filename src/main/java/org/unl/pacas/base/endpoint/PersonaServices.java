package org.unl.pacas.base.endpoint;

import com.vaadin.hilla.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.models.Sexo;
import org.unl.pacas.base.models.TipoIdentificacion;
import org.unl.pacas.base.services.PersonaService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Endpoint
@Transactional
public class PersonaServices {

    @Autowired
    private PersonaService personaService;

    public List<Persona> listAll() {
        return personaService.findAll();
    }

    public List<Persona> getPersonasSinCuenta() {
        return personaService.findPersonasSinCuenta();
    }

    public Optional<Persona> findById(Long id) {
        return personaService.findById(id);
    }

    public Persona create(String nombres, String apellidos, String email,
                          String tipoIdentificacionStr, String identificacion,
                          String sexoStr, String telefono, String direccion,
                          String fechaNacimientoStr) {

        TipoIdentificacion tipoIdentificacion = TipoIdentificacion.valueOf(tipoIdentificacionStr);
        Sexo sexo = Sexo.valueOf(sexoStr);
        LocalDate fechaNacimiento = (fechaNacimientoStr == null || fechaNacimientoStr.isEmpty()) ? null : LocalDate.parse(fechaNacimientoStr);

        return personaService.crearPersona(nombres, apellidos, email, tipoIdentificacion, identificacion, sexo, telefono, direccion, fechaNacimiento);
    }

    public Persona update(Long id, String nombres, String apellidos, String email,
                          String tipoIdentificacionStr, String identificacion,
                          String sexoStr, String telefono, String direccion,
                          String fechaNacimientoStr) {

        TipoIdentificacion tipoIdentificacion = TipoIdentificacion.valueOf(tipoIdentificacionStr);
        Sexo sexo = Sexo.valueOf(sexoStr);
        LocalDate fechaNacimiento = (fechaNacimientoStr == null || fechaNacimientoStr.isEmpty()) ? null : LocalDate.parse(fechaNacimientoStr);

        return personaService.actualizarPersona(id, nombres, apellidos, email, tipoIdentificacion, identificacion, sexo, telefono, direccion, fechaNacimiento);
    }

    public void delete(Long id) {
        personaService.eliminarPersona(id);
    }
}