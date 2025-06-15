package org.unl.pacas.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unl.pacas.base.services.TransaccionService;
import org.unl.pacas.base.models.Transaccion;
import org.unl.pacas.base.models.TipoTransaccion;
import org.unl.pacas.base.models.MetodoPago;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin(origins = "*")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping
    public List<Transaccion> getAllTransacciones() {
        return transaccionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaccion> getTransaccionById(@PathVariable Long id) {
        Optional<Transaccion> transaccion = transaccionService.findById(id);
        return transaccion.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{productoId}")
    public List<Transaccion> getTransaccionesByProducto(@PathVariable Long productoId) {
        return transaccionService.findByProductoId(productoId);
    }

    @GetMapping("/tipo/{tipo}")
    public List<Transaccion> getTransaccionesByTipo(@PathVariable TipoTransaccion tipo) {
        return transaccionService.findByTipo(tipo);
    }

    @PostMapping("/compra")
    public ResponseEntity<Transaccion> registrarCompra(@RequestBody CompraRequest request) {
        try {
            Transaccion transaccion = transaccionService.registrarCompra(
                request.productoId, 
                request.cantidad, 
                request.precioUnitario, 
                request.metodoPago, 
                request.observaciones
            );
            return ResponseEntity.ok(transaccion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/venta")
    public ResponseEntity<Transaccion> registrarVenta(@RequestBody VentaRequest request) {
        try {
            Transaccion transaccion = transaccionService.registrarVenta(
                request.productoId, 
                request.cantidad, 
                request.precioUnitario, 
                request.metodoPago, 
                request.observaciones
            );
            return ResponseEntity.ok(transaccion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/ajuste")
    public ResponseEntity<Transaccion> registrarAjuste(@RequestBody AjusteRequest request) {
        try {
            Transaccion transaccion = transaccionService.registrarAjuste(
                request.productoId, 
                request.cantidad, 
                request.tipoAjuste, 
                request.observaciones
            );
            return ResponseEntity.ok(transaccion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/reportes/ventas-hoy")
    public ResponseEntity<Object> getVentasHoy() {
        return ResponseEntity.ok(new Object() {
            public final Double total = transaccionService.calcularVentasHoy();
            public final Long cantidad = transaccionService.contarVentasHoy();
        });
    }

    @GetMapping("/reportes/ventas-periodo")
    public ResponseEntity<Double> getVentasPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Double ventas = transaccionService.calcularVentasEnPeriodo(fechaInicio, fechaFin);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/reportes/compras-periodo")
    public ResponseEntity<Double> getComprasPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Double compras = transaccionService.calcularComprasEnPeriodo(fechaInicio, fechaFin);
        return ResponseEntity.ok(compras);
    }

    // Clases internas para requests
    public static class CompraRequest {
        public Long productoId;
        public Integer cantidad;
        public Double precioUnitario;
        public MetodoPago metodoPago;
        public String observaciones;
    }

    public static class VentaRequest {
        public Long productoId;
        public Integer cantidad;
        public Double precioUnitario;
        public MetodoPago metodoPago;
        public String observaciones;
    }

    public static class AjusteRequest {
        public Long productoId;
        public Integer cantidad;
        public TipoTransaccion tipoAjuste;
        public String observaciones;
    }
}