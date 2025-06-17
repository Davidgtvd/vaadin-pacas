import type Cuenta_1 from "./Cuenta.js";
import type Sexo_1 from "./Sexo.js";
import type TipoIdentificacion_1 from "./TipoIdentificacion.js";
interface Persona {
    id?: number;
    nombres?: string;
    apellidos?: string;
    email?: string;
    telefono?: string;
    tipoIdentificacion?: TipoIdentificacion_1;
    identificacion?: string;
    sexo?: Sexo_1;
    direccion?: string;
    fechaNacimiento?: string;
    cuenta?: Cuenta_1;
    nombreCompleto?: string;
    identificacionCompleta?: string;
    edad?: number;
    displayName?: string;
}
export default Persona;
