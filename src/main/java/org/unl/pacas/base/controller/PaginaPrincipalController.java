package org.unl.pacas.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaPrincipalController {

    // Mapea la ruta raíz "/" a la vista "pagina_principal.html"
    @GetMapping("/")
    public String paginaPrincipal() {
        return "pagina_principal"; // Nombre de la plantilla Thymeleaf sin extensión
    }
}