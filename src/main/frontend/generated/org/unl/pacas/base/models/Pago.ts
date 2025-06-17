import type Compra_1 from "./Compra.js";
import type Cuenta_1 from "./Cuenta.js";
interface Pago {
    id?: number;
    codigoSeguridad?: string;
    metodoPago?: string;
    estado?: boolean;
    fechaPago?: string;
    cuenta?: Cuenta_1;
    compra?: Compra_1;
}
export default Pago;
