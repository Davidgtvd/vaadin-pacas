import { _getPropertyModel as _getPropertyModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, Positive as Positive_1, PositiveOrZero as PositiveOrZero_1 } from "@vaadin/hilla-lit-form";
import CompraModel_1 from "./CompraModel.js";
import type DetalleFactura_1 from "./DetalleFactura.js";
import FacturaModel_1 from "./FacturaModel.js";
import PagoModel_1 from "./PagoModel.js";
import ProductoModel_1 from "./ProductoModel.js";
class DetalleFacturaModel<T extends DetalleFactura_1 = DetalleFactura_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(DetalleFacturaModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.Id" }], javaType: "java.lang.Long" } }));
    }
    get cantidad(): NumberModel_1 {
        return this[_getPropertyModel_1]("cantidad", (parent, key) => new NumberModel_1(parent, key, false, { validators: [new Positive_1({ message: "La cantidad debe ser positiva" })], meta: { javaType: "int" } }));
    }
    get precioUnitario(): NumberModel_1 {
        return this[_getPropertyModel_1]("precioUnitario", (parent, key) => new NumberModel_1(parent, key, false, { validators: [new Positive_1({ message: "El precio unitario debe ser positivo" })], meta: { javaType: "float" } }));
    }
    get total(): NumberModel_1 {
        return this[_getPropertyModel_1]("total", (parent, key) => new NumberModel_1(parent, key, false, { validators: [new PositiveOrZero_1({ message: "El total debe ser cero o positivo" })], meta: { javaType: "float" } }));
    }
    get producto(): ProductoModel_1 {
        return this[_getPropertyModel_1]("producto", (parent, key) => new ProductoModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.ManyToOne" }] } }));
    }
    get factura(): FacturaModel_1 {
        return this[_getPropertyModel_1]("factura", (parent, key) => new FacturaModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.ManyToOne" }] } }));
    }
    get compra(): CompraModel_1 {
        return this[_getPropertyModel_1]("compra", (parent, key) => new CompraModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.ManyToOne" }] } }));
    }
    get pago(): PagoModel_1 {
        return this[_getPropertyModel_1]("pago", (parent, key) => new PagoModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.ManyToOne" }] } }));
    }
}
export default DetalleFacturaModel;
