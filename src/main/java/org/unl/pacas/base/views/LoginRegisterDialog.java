package org.unl.pacas.base.views;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.html.Div;

public class LoginRegisterDialog extends Dialog {

    public LoginRegisterDialog() {
        Tabs tabs = new Tabs();
        Tab loginTab = new Tab("Iniciar sesión");
        Tab registerTab = new Tab("Registrarse");
        tabs.add(loginTab, registerTab);

        LoginForm loginForm = new LoginForm();
        RegisterForm registerForm = new RegisterForm();

        Div content = new Div(loginForm);
        content.setWidth("300px");

        tabs.addSelectedChangeListener(event -> {
            content.removeAll();
            if (tabs.getSelectedTab() == loginTab) {
                content.add(loginForm);
            } else {
                content.add(registerForm);
            }
        });

        add(tabs, content);
    }
}