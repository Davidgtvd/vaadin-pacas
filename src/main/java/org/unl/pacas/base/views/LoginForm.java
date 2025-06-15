package org.unl.pacas.base.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LoginForm extends VerticalLayout {
    public LoginForm() {
        TextField username = new TextField("Usuario");
        PasswordField password = new PasswordField("Contraseña");
        Button loginBtn = new Button("Iniciar sesión", e -> {
            // Aquí va la lógica de autenticación
        });
        add(username, password, loginBtn);
    }
}