import type Compra_1 from "./Compra.js";
import type Factura_1 from "./Factura.js";
import type Pago_1 from "./Pago.js";
import type Producto_1 from "./Producto.js";
interface DetalleFactura {
    id?: number;
    cantidad: number;
    precioUnitario: number;
    total: number;
    producto?: Producto_1;
    factura?: Factura_1;
    compra?: Compra_1;
    pago?: Pago_1;
}
export default DetalleFactura;
