import type Persona_1 from "./Persona.js";
import type Rol_1 from "./Rol.js";
interface Cuenta {
    id?: number;
    usuario?: string;
    contrasena?: string;
    activo?: boolean;
    fechaCreacion?: string;
    ultimoAcceso?: string;
    intentosFallidos?: number;
    fechaBloqueo?: string;
    rol?: Rol_1;
    persona?: Persona_1;
    clave?: string;
    estadoTexto?: string;
    rolNombre?: string;
    personaNombre?: string;
    displayName?: string;
}
export default Cuenta;
