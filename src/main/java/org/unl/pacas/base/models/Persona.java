package org.unl.pacas.base.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "personas")
public class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede tener más de 150 caracteres")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "El teléfono solo puede contener números, espacios y símbolos +, -, (, )")
    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    @Column(length = 15)
    private String telefono;

    @NotNull(message = "El tipo de identificación es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_identificacion", nullable = false, length = 20)
    private TipoIdentificacion tipoIdentificacion;

    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 20, message = "La identificación no puede tener más de 20 caracteres")
    @Column(nullable = false, length = 20)
    private String identificacion;

    @NotNull(message = "El sexo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Sexo sexo;

    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    @Column(length = 200)
    private String direccion;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cuenta cuenta;

    // Constructores
    public Persona() {}

    public Persona(String nombres, String apellidos, String email) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }

    public Persona(String nombres, String apellidos, String email, TipoIdentificacion tipoIdentificacion,
                   String identificacion, Sexo sexo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.tipoIdentificacion = tipoIdentificacion;
        this.identificacion = identificacion;
        this.sexo = sexo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    // Métodos utilitarios para las vistas
    public String getNombreCompleto() {
        return (nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : "");
    }

    /**
     * Permite establecer el nombre completo en una sola cadena.
     * Divide el nombre completo en nombres y apellidos (el último espacio separa).
     */
    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            this.nombres = "";
            this.apellidos = "";
            return;
        }
        String[] partes = nombreCompleto.trim().split(" ");
        if (partes.length == 1) {
            this.nombres = partes[0];
            this.apellidos = "";
        } else {
            this.nombres = String.join(" ", java.util.Arrays.copyOf(partes, partes.length - 1));
            this.apellidos = partes[partes.length - 1];
        }
    }

    public String getDisplayName() {
        return getNombreCompleto() + " (" + email + ")";
    }

    public String getIdentificacionCompleta() {
        return tipoIdentificacion != null ? tipoIdentificacion.name() + ": " + identificacion : identificacion;
    }

    public Integer getEdad() {
        if (fechaNacimiento != null) {
            return LocalDate.now().getYear() - fechaNacimiento.getYear();
        }
        return null;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona)) return false;
        Persona persona = (Persona) o;
        return Objects.equals(id, persona.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}