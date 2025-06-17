package org.unl.pacas.base.dao;

import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.Transaccion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class TransaccionDao {

    private final List<Transaccion> transacciones = new ArrayList<>();
    private Long nextId = 1L;

    public List<Transaccion> findAll() {
        return transacciones;
    }

    public Transaccion findById(Long id) {
        return transacciones.stream()
                .filter(t -> Objects.equals(t.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public Transaccion save(Transaccion transaccion) {
        if (transaccion.getId() == null) {
            transaccion.setId(nextId++);
            transacciones.add(transaccion);
        } else {
            transacciones.removeIf(t -> Objects.equals(t.getId(), transaccion.getId()));
            transacciones.add(transaccion);
        }
        return transaccion;
    }

    public void deleteById(Long id) {
        transacciones.removeIf(t -> Objects.equals(t.getId(), id));
    }

    public List<Transaccion> findByProducto(Long productoId) {
        return transacciones.stream()
                .filter(t -> t.getProducto() != null && Objects.equals(t.getProducto().getId(), productoId))
                .collect(Collectors.toList());
    }

    public List<Transaccion> findByFecha(LocalDateTime desde, LocalDateTime hasta) {
        return transacciones.stream()
                .filter(t -> t.getFecha() != null && t.getFecha().isAfter(desde) && t.getFecha().isBefore(hasta))
                .collect(Collectors.toList());
    }

    public List<Transaccion> findByTipo(String tipo) {
        return transacciones.stream()
                .filter(t -> t.getTipo() != null && t.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    public List<Transaccion> findByMetodoPago(String metodoPago) {
        return transacciones.stream()
                .filter(t -> t.getMetodoPago() != null && t.getMetodoPago().equalsIgnoreCase(metodoPago))
                .collect(Collectors.toList());
    }

    public List<Transaccion> findByEstado(String estado) {
        return transacciones.stream()
                .filter(t -> t.getEstado() != null && t.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }
}