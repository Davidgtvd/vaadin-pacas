package org.unl.pacas.base.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.unl.pacas.base.models.Producto;
import org.unl.pacas.base.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("productos")
public class MainView extends VerticalLayout {

    @Autowired
    public MainView(ProductoService productoService) {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // Botón para iniciar sesión o registrarse
        Button loginButton = new Button("Iniciar sesión / Registrarse", e -> openLoginDialog());
        add(loginButton);

        // Grid de productos
        Grid<Producto> grid = new Grid<>(Producto.class, false);
        grid.setSizeFull();
        grid.addColumn(Producto::getCodigo).setHeader("Código");
        grid.addColumn(Producto::getNombre).setHeader("Nombre");
        grid.addColumn(Producto::getCategoria).setHeader("Categoría");
        grid.addColumn(Producto::getPrecioVenta).setHeader("Precio Venta");
        grid.addColumn(Producto::getStock).setHeader("Stock");

        // Muestra solo productos activos
        grid.setItems(productoService.findAllActivos());

        add(grid);
        expand(grid);
    }

    private void openLoginDialog() {
        LoginRegisterDialog dialog = new LoginRegisterDialog();
        dialog.open();
    }
}