package org.unl.pacas.base.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RegisterForm extends VerticalLayout {
    public RegisterForm() {
        TextField username = new TextField("Usuario");
        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Contraseña");
        Button registerBtn = new Button("Registrarse", e -> {
            // Aquí va la lógica de registro
        });
        add(username, email, password, registerBtn);
    }
}