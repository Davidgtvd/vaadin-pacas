package org.unl.pacas.base.endpoint;

import com.vaadin.hilla.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.unl.pacas.base.models.Cuenta;
import org.unl.pacas.base.services.CuentaService;

import java.util.List;
import java.util.Optional;

@Endpoint
@Transactional
public class CuentaServices {

    @Autowired
    private CuentaService cuentaService;

    public List<Cuenta> listAll() {
        return cuentaService.findAll();
    }

    public List<Cuenta> buscarPorTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listAll();
        }
        return cuentaService.buscarPorTexto(texto.trim());
    }

    public Optional<Cuenta> findById(Long id) {
        return cuentaService.findById(id);
    }

    public Cuenta create(String usuario, String contrasena, Long rolId, Long personaId) {
        return cuentaService.crearCuenta(usuario, contrasena, rolId, personaId);
    }

    public Cuenta update(Long id, String usuario, Long rolId, Boolean activo) {
        return cuentaService.actualizarCuenta(id, usuario, rolId, activo);
    }

    public void delete(Long id) {
        cuentaService.eliminarCuenta(id);
    }
}