package org.unl.pacas.base.models;

public enum EstadoProducto {
    ACTIVO("Activo"),
    INACTIVO("Inactivo"),
    DESCONTINUADO("Descontinuado"),
    AGOTADO("Agotado"),
    EN_REVISION("En Revisión");

    private final String descripcion;

    EstadoProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

    public static EstadoProducto fromDescripcion(String descripcion) {
        for (EstadoProducto estado : values()) {
            if (estado.getDescripcion().equalsIgnoreCase(descripcion)) {
                return estado;
            }
        }
        return ACTIVO;
    }

    public boolean esVendible() {
        return this == ACTIVO;
    }

    public boolean requiereAtencion() {
        return this == AGOTADO || this == EN_REVISION;
    }
}