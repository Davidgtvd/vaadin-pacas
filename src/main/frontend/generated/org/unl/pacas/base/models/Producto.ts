interface Producto {
    id?: number;
    codigo?: string;
    nombre?: string;
    descripcion?: string;
    categoria?: string;
    unidadMedida?: string;
    marca?: string;
    modelo?: string;
    ubicacion?: string;
    proveedor?: string;
    activo?: boolean;
    stock?: number;
    stockMinimo?: number;
    precio?: number;
    precioCosto?: number;
    iva?: number;
    fechaCreacion?: string;
    fechaActualizacion?: string;
}
export default Producto;
