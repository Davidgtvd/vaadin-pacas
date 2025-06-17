import type DetalleFactura_1 from "./DetalleFactura.js";
import type Persona_1 from "./Persona.js";
interface Factura {
    id?: number;
    nroFactura?: string;
    persona?: Persona_1;
    detalles?: Array<DetalleFactura_1 | undefined>;
    total: number;
}
export default Factura;
