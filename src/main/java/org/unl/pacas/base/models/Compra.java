package org.unl.pacas.base.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "compras")
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float subtotal;
    private String nroFactura;
    private float iva;
    private float total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    public Compra() {}

    public Compra(float subtotal, String nroFactura, float iva, float total, Persona persona) {
        this.subtotal = subtotal;
        this.nroFactura = nroFactura;
        this.iva = iva;
        this.total = total;
        this.persona = persona;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public float getSubtotal() { return subtotal; }
    public void setSubtotal(float subtotal) { this.subtotal = subtotal; }

    public String getNroFactura() { return nroFactura; }
    public void setNroFactura(String nroFactura) { this.nroFactura = nroFactura; }

    public float getIva() { return iva; }
    public void setIva(float iva) { this.iva = iva; }

    public float getTotal() { return total; }
    public void setTotal(float total) { this.total = total; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compra)) return false;
        Compra compra = (Compra) o;
        return Objects.equals(id, compra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Compra #" + id + " - Factura: " + nroFactura;
    }
}