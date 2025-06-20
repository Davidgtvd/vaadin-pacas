package org.unl.pacas.base.models;

public class Cuenta {
    private int id;
    private String correoElectronico;
    private String clave;
    private RolEnum rol;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreoElectronico() {
        return this.correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getClave() {
        return this.clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public RolEnum getRol() {
        return this.rol;
    }

    public void setRol(RolEnum rol) {
        this.rol = rol;
    }

}
