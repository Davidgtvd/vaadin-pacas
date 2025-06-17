package org.unl.pacas.base.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name = "factura")
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de factura es obligatorio")
    @Column(name = "nro_factura", nullable = false, unique = true)
    private String nroFactura;

    @NotNull(message = "La persona es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detalles = new ArrayList<>();

    @PositiveOrZero(message = "El total debe ser cero o positivo")
    @Column(nullable = false)
    private float total;

    public Factura() {
    }

    public Factura(String nroFactura, Persona persona, List<DetalleFactura> detalles, float total) {
        this.nroFactura = nroFactura;
        this.persona = persona;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        this.total = total;
        this.detalles.forEach(detalle -> detalle.setFactura(this));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles.clear();
        if (detalles != null) {
            detalles.forEach(this::addDetalle);
        }
    }

    public void addDetalle(DetalleFactura detalle) {
        detalle.setFactura(this);
        this.detalles.add(detalle);
    }

    public void removeDetalle(DetalleFactura detalle) {
        detalle.setFactura(null);
        this.detalles.remove(detalle);
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Factura)) return false;
        Factura factura = (Factura) o;
        return Objects.equals(id, factura.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", nroFactura='" + nroFactura + '\'' +
                ", persona=" + (persona != null ? persona.getId() : "null") +
                ", total=" + total +
                ", detallesCount=" + (detalles != null ? detalles.size() : 0) +
                '}';
    }
}