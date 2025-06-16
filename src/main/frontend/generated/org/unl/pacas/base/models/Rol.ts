import type Cuenta_1 from "./Cuenta.js";
interface Rol {
    id?: number;
    nombre?: string;
    descripcion?: string;
    cuentas?: Array<Cuenta_1 | undefined>;
    displayName?: string;
}
export default Rol;
