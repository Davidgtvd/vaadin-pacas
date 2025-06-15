package org.unl.pacas.base.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

@AnonymousAllowed // Permite acceso público a todas las vistas hijas
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        // Logo o nombre de la app
        H2 logo = new H2("Pacas de Ropa");
        logo.getStyle().set("margin", "0 1rem 0 0").set("font-size", "1.5em");

        // Botones de acción
        Button loginButton = new Button("Iniciar sesión", e -> UI.getCurrent().navigate("login"));
        Button registerButton = new Button("Registrarse", e -> UI.getCurrent().navigate("register"));

        HorizontalLayout navBar = new HorizontalLayout(
                loginButton,
                registerButton
        );
        navBar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        navBar.setSpacing(true);

        // Header layout
        HorizontalLayout header = new HorizontalLayout(logo, navBar);
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.getStyle().set("padding", "0.5rem 2rem").set("background", "#f8f8f8");

        addToNavbar(header);
    }
}