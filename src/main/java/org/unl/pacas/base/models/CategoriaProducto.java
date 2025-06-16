package org.unl.pacas.base.models;

public enum CategoriaProducto {
    MIXTA("Mixta"),
    ADULTO("Adulto"),
    VERANO_INVIERNO("Verano/Invierno"),
    NINO("Niño"),
    DEPORTIVA("Deportiva");

    private final String descripcion;

    CategoriaProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

    public static CategoriaProducto fromDescripcion(String descripcion) {
        for (CategoriaProducto categoria : values()) {
            if (categoria.getDescripcion().equalsIgnoreCase(descripcion)) {
                return categoria;
            }
        }
        return MIXTA; // Por defecto MIXTA en lugar de OTROS
    }

    // Métodos útiles específicos para el negocio de pacas
    public boolean esParaAdultos() {
        return this == ADULTO || this == MIXTA || this == VERANO_INVIERNO;
    }

    public boolean esParaNinos() {
        return this == NINO || this == MIXTA;
    }

    public boolean esTemporada() {
        return this == VERANO_INVIERNO;
    }

    public boolean esEspecializada() {
        return this == DEPORTIVA;
    }

    // Método para obtener categorías relacionadas (útil para recomendaciones)
    public CategoriaProducto[] getCategoriasRelacionadas() {
        switch (this) {
            case ADULTO:
                return new CategoriaProducto[]{MIXTA, VERANO_INVIERNO, DEPORTIVA};
            case NINO:
                return new CategoriaProducto[]{MIXTA};
            case DEPORTIVA:
                return new CategoriaProducto[]{ADULTO, MIXTA};
            case VERANO_INVIERNO:
                return new CategoriaProducto[]{ADULTO, MIXTA};
            case MIXTA:
            default:
                return new CategoriaProducto[]{ADULTO, NINO, DEPORTIVA};
        }
    }

    // Método para obtener descripción con detalles
    public String getDescripcionDetallada() {
        switch (this) {
            case MIXTA:
                return "Paca Mixta - Variedad de ropa para todas las edades";
            case ADULTO:
                return "Paca Adulto - Ropa exclusiva para adultos";
            case VERANO_INVIERNO:
                return "Paca Verano/Invierno - Ropa de temporada específica";
            case NINO:
                return "Paca Niño - Ropa infantil y juvenil";
            case DEPORTIVA:
                return "Paca Deportiva - Ropa deportiva y activewear";
            default:
                return descripcion;
        }
    }
}

