import type Persona_1 from "./Persona.js";
interface Compra {
    id?: number;
    subtotal: number;
    nroFactura?: string;
    iva: number;
    total: number;
    persona?: Persona_1;
}
export default Compra;
