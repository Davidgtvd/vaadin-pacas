package org.unl.pacas.base.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.unl.pacas.base.models.Cuenta;
import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.models.Rol;
import org.unl.pacas.base.services.CuentaService;
import org.unl.pacas.base.services.PersonaService;
import org.unl.pacas.base.services.RolService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("cuentas")
@PageTitle("Gestión de Cuentas | Pacas de Ropa")
public class CuentaList extends VerticalLayout {

    @Autowired
    private CuentaService cuentaService;
    
    @Autowired
    private PersonaService personaService;
    
    @Autowired
    private RolService rolService;

    private Grid<Cuenta> grid;
    private ListDataProvider<Cuenta> dataProvider;
    private TextField searchField;
    private ComboBox<String> estadoFilter;
    private ComboBox<Rol> rolFilter;
    private Button addButton;
    private Dialog formDialog;
    private Binder<Cuenta> binder;
    private Cuenta currentCuenta;
    private boolean isEditing = false;

    // Componentes del formulario
    private TextField usuarioField;
    private PasswordField contrasenaField;
    private ComboBox<Rol> rolCombo;
    private ComboBox<Persona> personaCombo;
    private Checkbox activoCheckbox;

    public CuentaList() {
        setSizeFull();
        addClassName("cuenta-list-view");
        
        createHeader();
        createFilters();
        createGrid();
        createFormDialog();
        
        loadData();
    }

    private void createHeader() {
        H2 title = new H2("Gestión de Cuentas de Usuario");
        title.addClassName("view-title");

        addButton = new Button("Nueva Cuenta", new Icon(VaadinIcon.PLUS));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openForm(null));

        HorizontalLayout header = new HorizontalLayout(title, addButton);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.addClassName("view-header");

        add(header);
    }

    private void createFilters() {
        searchField = new TextField();
        searchField.setPlaceholder("Buscar por usuario, nombre o email...");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> applyFilters());
        searchField.setWidth("300px");

        estadoFilter = new ComboBox<>("Estado");
        estadoFilter.setItems("Todos", "Activos", "Inactivos", "Bloqueados");
        estadoFilter.setValue("Todos");
        estadoFilter.addValueChangeListener(e -> applyFilters());
        estadoFilter.setWidth("150px");

        rolFilter = new ComboBox<>("Rol");
        rolFilter.setItemLabelGenerator(Rol::getNombre);
        rolFilter.addValueChangeListener(e -> applyFilters());
        rolFilter.setWidth("200px");

        Button clearFiltersButton = new Button("Limpiar Filtros", new Icon(VaadinIcon.REFRESH));
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearFiltersButton.addClickListener(e -> clearFilters());

        HorizontalLayout filters = new HorizontalLayout(
            searchField, estadoFilter, rolFilter, clearFiltersButton
        );
        filters.setAlignItems(FlexComponent.Alignment.END);
        filters.addClassName("filters-layout");

        add(filters);
    }

    private void createGrid() {
        grid = new Grid<>(Cuenta.class, false);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSizeFull();

        // Columna Usuario
        grid.addColumn(Cuenta::getUsuario)
            .setHeader("Usuario")
            .setSortable(true)
            .setWidth("150px")
            .setFlexGrow(0);

        // Columna Persona
        grid.addColumn(cuenta -> cuenta.getPersona() != null ? 
                      cuenta.getPersona().getNombreCompleto() : "Sin asignar")
            .setHeader("Persona")
            .setSortable(true)
            .setWidth("200px");

        // Columna Email
        grid.addColumn(cuenta -> cuenta.getPersona() != null ? 
                      cuenta.getPersona().getEmail() : "")
            .setHeader("Email")
            .setWidth("200px");

        // Columna Rol
        grid.addColumn(cuenta -> cuenta.getRol() != null ? 
                      cuenta.getRol().getNombre() : "Sin rol")
            .setHeader("Rol")
            .setSortable(true)
            .setWidth("120px");

        // Columna Estado
        grid.addColumn(new ComponentRenderer<>(this::createStatusBadge))
            .setHeader("Estado")
            .setWidth("100px")
            .setFlexGrow(0);

        // Columna Último Acceso
        grid.addColumn(cuenta -> cuenta.getUltimoAcceso() != null ? 
                      cuenta.getUltimoAcceso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : 
                      "Nunca")
            .setHeader("Último Acceso")
            .setWidth("150px");

        // Columna Fecha Creación
        grid.addColumn(cuenta -> cuenta.getFechaCreacion() != null ? 
                      cuenta.getFechaCreacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : 
                      "")
            .setHeader("Creado")
            .setWidth("100px");

        // Columna Acciones
        grid.addColumn(new ComponentRenderer<>(this::createActionButtons))
            .setHeader("Acciones")
            .setWidth("200px")
            .setFlexGrow(0);

        dataProvider = new ListDataProvider<>(List.of());
        grid.setDataProvider(dataProvider);

        add(grid);
    }

    private Span createStatusBadge(Cuenta cuenta) {
        Span badge = new Span();
        
        if (!cuenta.getActivo()) {
            badge.setText("Inactivo");
            badge.getElement().getThemeList().add("badge error");
        } else if (!cuenta.puedeIniciarSesion()) {
            badge.setText("Bloqueado");
            badge.getElement().getThemeList().add("badge contrast");
        } else {
            badge.setText("Activo");
            badge.getElement().getThemeList().add("badge success");
        }
        
        return badge;
    }

    private HorizontalLayout createActionButtons(Cuenta cuenta) {
        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        editButton.getElement().setAttribute("title", "Editar cuenta");
        editButton.addClickListener(e -> openForm(cuenta));

        Button toggleButton = new Button(new Icon(cuenta.getActivo() ? VaadinIcon.EYE_SLASH : VaadinIcon.EYE));
        toggleButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        toggleButton.getElement().setAttribute("title", cuenta.getActivo() ? "Desactivar" : "Activar");
        toggleButton.addClickListener(e -> toggleAccountStatus(cuenta));

        Button unlockButton = new Button(new Icon(VaadinIcon.UNLOCK));
        unlockButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        unlockButton.getElement().setAttribute("title", "Desbloquear cuenta");
        unlockButton.addClickListener(e -> unlockAccount(cuenta));
        unlockButton.setVisible(!cuenta.puedeIniciarSesion() && cuenta.getActivo());

        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
        deleteButton.getElement().setAttribute("title", "Eliminar cuenta");
        deleteButton.addClickListener(e -> confirmDelete(cuenta));

        HorizontalLayout actions = new HorizontalLayout(editButton, toggleButton, unlockButton, deleteButton);
        actions.setSpacing(false);
        return actions;
    }

    private void createFormDialog() {
        formDialog = new Dialog();
        formDialog.setWidth("500px");
        formDialog.setCloseOnEsc(true);
        formDialog.setCloseOnOutsideClick(false);

        // Título del diálogo
        H3 dialogTitle = new H3();
        dialogTitle.setId("dialog-title");

        // Formulario
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        usuarioField = new TextField("Usuario");
        usuarioField.setRequired(true);
        usuarioField.setMaxLength(50);

        contrasenaField = new PasswordField("Contraseña");
        contrasenaField.setRequired(true);

        rolCombo = new ComboBox<>("Rol");
        rolCombo.setItemLabelGenerator(Rol::getNombre);
        rolCombo.setRequired(true);

        personaCombo = new ComboBox<>("Persona");
        personaCombo.setItemLabelGenerator(Persona::getNombreCompleto);
        personaCombo.setRequired(true);

        activoCheckbox = new Checkbox("Cuenta activa");
        activoCheckbox.setValue(true);

        formLayout.add(usuarioField, contrasenaField, rolCombo, personaCombo, activoCheckbox);

        // Botones
        Button saveButton = new Button("Guardar", new Icon(VaadinIcon.CHECK));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveAccount());

        Button cancelButton = new Button("Cancelar", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(e -> formDialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(dialogTitle, formLayout, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(true);

        formDialog.add(dialogLayout);

        // Configurar binder
        binder = new Binder<>(Cuenta.class);
        setupBinder();
    }

    private void setupBinder() {
        binder.forField(usuarioField)
            .withValidator(usuario -> cuentaService.validarUsuario(usuario), 
                          "Usuario inválido (3-50 caracteres, solo letras, números, puntos, guiones)")
            .withValidator(usuario -> isEditing || !cuentaService.existsByUsuario(usuario), 
                          "Ya existe una cuenta con este usuario")
            .bind(Cuenta::getUsuario, Cuenta::setUsuario);

        binder.forField(contrasenaField)
            .withValidator(contrasena -> isEditing || cuentaService.validarContrasena(contrasena), 
                          "La contraseña debe tener al menos 6 caracteres")
            .bind(cuenta -> "", (cuenta, contrasena) -> {
                if (!isEditing && contrasena != null && !contrasena.isEmpty()) {
                    // La contraseña se encripta en el service
                }
            });

        binder.forField(rolCombo)
            .withValidator(rol -> rol != null, "Debe seleccionar un rol")
            .bind(Cuenta::getRol, Cuenta::setRol);

        binder.forField(personaCombo)
            .withValidator(persona -> persona != null, "Debe seleccionar una persona")
            .withValidator(persona -> isEditing || !cuentaService.existsByPersonaId(persona.getId()), 
                          "Esta persona ya tiene una cuenta asociada")
            .bind(Cuenta::getPersona, Cuenta::setPersona);

        binder.forField(activoCheckbox)
            .bind(Cuenta::getActivo, Cuenta::setActivo);
    }

    private void openForm(Cuenta cuenta) {
        currentCuenta = cuenta;
        isEditing = cuenta != null;

        H3 title = (H3) formDialog.getElement().getChildren()
            .filter(element -> "dialog-title".equals(element.getAttribute("id")))
            .findFirst().map(element -> (H3) element.getComponent().orElse(null))
            .orElse(null);

        if (title != null) {
            title.setText(isEditing ? "Editar Cuenta" : "Nueva Cuenta");
        }

        // Cargar datos en los combos
        loadFormData();

        if (isEditing) {
            binder.readBean(cuenta);
            contrasenaField.setVisible(false);
            contrasenaField.setRequired(false);
        } else {
            binder.readBean(new Cuenta());
            contrasenaField.setVisible(true);
            contrasenaField.setRequired(true);
        }

        formDialog.open();
    }

    private void loadFormData() {
        try {
            List<Rol> roles = rolService.findAll();
            rolCombo.setItems(roles);

            List<Persona> personas = isEditing ? 
                personaService.findAll() : 
                personaService.findPersonasSinCuenta();
            personaCombo.setItems(personas);
        } catch (Exception e) {
            showErrorNotification("Error al cargar datos del formulario: " + e.getMessage());
        }
    }

    private void saveAccount() {
        try {
            if (currentCuenta == null) {
                currentCuenta = new Cuenta();
            }

            if (binder.validate().isOk()) {
                binder.writeBean(currentCuenta);

                if (isEditing) {
                    cuentaService.actualizarCuenta(
                        currentCuenta.getId(),
                        currentCuenta.getUsuario(),
                        currentCuenta.getRol().getId(),
                        currentCuenta.getActivo()
                    );
                    showSuccessNotification("Cuenta actualizada correctamente");
                } else {
                    cuentaService.crearCuenta(
                        currentCuenta.getUsuario(),
                        contrasenaField.getValue(),
                        currentCuenta.getRol().getId(),
                        currentCuenta.getPersona().getId()
                    );
                    showSuccessNotification("Cuenta creada correctamente");
                }

                formDialog.close();
                loadData();
            }
        } catch (ValidationException e) {
            showErrorNotification("Por favor, corrija los errores en el formulario");
        } catch (Exception e) {
            showErrorNotification("Error al guardar la cuenta: " + e.getMessage());
        }
    }

    private void toggleAccountStatus(Cuenta cuenta) {
        try {
            if (cuenta.getActivo()) {
                cuentaService.desactivarCuenta(cuenta.getId());
                showSuccessNotification("Cuenta desactivada");
            } else {
                cuentaService.activarCuenta(cuenta.getId());
                showSuccessNotification("Cuenta activada");
            }
            loadData();
        } catch (Exception e) {
            showErrorNotification("Error al cambiar el estado: " + e.getMessage());
        }
    }

    private void unlockAccount(Cuenta cuenta) {
        try {
            cuentaService.desbloquearCuenta(cuenta.getId());
            showSuccessNotification("Cuenta desbloqueada correctamente");
            loadData();
        } catch (Exception e) {
            showErrorNotification("Error al desbloquear la cuenta: " + e.getMessage());
        }
    }

    private void confirmDelete(Cuenta cuenta) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirmar eliminación");
        dialog.setText("¿Está seguro de que desea eliminar la cuenta '" + cuenta.getUsuario() + "'?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Eliminar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> deleteAccount(cuenta));
        dialog.open();
    }

    private void deleteAccount(Cuenta cuenta) {
        try {
            cuentaService.eliminarCuenta(cuenta.getId());
            showSuccessNotification("Cuenta eliminada correctamente");
            loadData();
        } catch (Exception e) {
            showErrorNotification("Error al eliminar la cuenta: " + e.getMessage());
        }
    }

    private void applyFilters() {
        String searchText = searchField.getValue().toLowerCase().trim();
        String estadoValue = estadoFilter.getValue();
        Rol rolValue = rolFilter.getValue();

        dataProvider.setFilter(cuenta -> {
            // Filtro de búsqueda
            boolean matchesSearch = searchText.isEmpty() || 
                cuenta.getUsuario().toLowerCase().contains(searchText) ||
                (cuenta.getPersona() != null && 
                 cuenta.getPersona().getNombreCompleto().toLowerCase().contains(searchText)) ||
                (cuenta.getPersona() != null && cuenta.getPersona().getEmail() != null &&
                 cuenta.getPersona().getEmail().toLowerCase().contains(searchText));

            // Filtro de estado
            boolean matchesEstado = "Todos".equals(estadoValue) ||
                ("Activos".equals(estadoValue) && cuenta.getActivo() && cuenta.puedeIniciarSesion()) ||
                ("Inactivos".equals(estadoValue) && !cuenta.getActivo()) ||
                ("Bloqueados".equals(estadoValue) && cuenta.getActivo() && !cuenta.puedeIniciarSesion());

            // Filtro de rol
            boolean matchesRol = rolValue == null || 
                (cuenta.getRol() != null && cuenta.getRol().getId().equals(rolValue.getId()));

            return matchesSearch && matchesEstado && matchesRol;
        });
    }

    private void clearFilters() {
        searchField.clear();
        estadoFilter.setValue("Todos");
        rolFilter.clear();
    }

    private void loadData() {
        try {
            List<Cuenta> cuentas = cuentaService.findAll();
            dataProvider.getItems().clear();
            dataProvider.getItems().addAll(cuentas);
            dataProvider.refreshAll();

            // Cargar roles para el filtro
            List<Rol> roles = rolService.findAll();
            rolFilter.setItems(roles);

        } catch (Exception e) {
            showErrorNotification("Error al cargar las cuentas: " + e.getMessage());
        }
    }

    private void showSuccessNotification(String message) {
        Notification notification = Notification.show(message, 3000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showErrorNotification(String message) {
        Notification notification = Notification.show(message, 5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}