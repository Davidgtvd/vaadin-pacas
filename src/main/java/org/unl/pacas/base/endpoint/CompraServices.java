package org.unl.pacas.base.endpoint;

import jakarta.annotation.security.PermitAll;
import org.springframework.web.bind.annotation.*;
import org.unl.pacas.base.models.Compra;
import org.unl.pacas.base.services.CompraService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hilla/compras")
@PermitAll
public class CompraServices {

    private final CompraService compraService;

    public CompraServices(CompraService compraService) {
        this.compraService = compraService;
    }

    /**
     * Lista todas las compras.
     */
    @GetMapping
    public List<Compra> listAll() {
        return compraService.findAll();
    }

    /**
     * Obtiene una compra por su ID.
     */
    @GetMapping("/{id}")
    public Compra findById(@PathVariable Long id) {
        Optional<Compra> compra = compraService.findById(id);
        return compra.orElse(null);
    }

    /**
     * Busca compras cuyo número de factura contenga el texto dado (búsqueda parcial).
     */
    @GetMapping("/buscar")
    public List<Compra> buscarPorFactura(@RequestParam String texto) {
        if (texto == null || texto.isBlank()) {
            return List.of();
        }
        return compraService.findByNroFacturaContainingIgnoreCase(texto.trim());
    }

    /**
     * Obtiene las compras que no tienen un pago asociado (compras sin pago).
     */
    @GetMapping("/sin-pago")
    public List<Compra> getComprasSinPago() {
        return compraService.findComprasSinPago();
    }

    /**
     * Crea una nueva compra.
     */
    @PostMapping
    public Compra crearCompra(@RequestBody Compra compra) {
        return compraService.save(compra);
    }

    /**
     * Actualiza una compra existente.
     */
    @PutMapping("/{id}")
    public Compra actualizarCompra(@PathVariable Long id, @RequestBody Compra compra) {
        if (!id.equals(compra.getId())) {
            throw new IllegalArgumentException("El ID de la compra no coincide");
        }
        return compraService.update(compra);
    }

    /**
     * Elimina una compra por ID.
     */
    @DeleteMapping("/{id}")
    public void eliminarCompra(@PathVariable Long id) {
        compraService.deleteById(id);
    }
}