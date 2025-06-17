import { _getPropertyModel as _getPropertyModel_1, ArrayModel as ArrayModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NotBlank as NotBlank_1, NotNull as NotNull_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, PositiveOrZero as PositiveOrZero_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import DetalleFacturaModel_1 from "./DetalleFacturaModel.js";
import type Factura_1 from "./Factura.js";
import PersonaModel_1 from "./PersonaModel.js";
class FacturaModel<T extends Factura_1 = Factura_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(FacturaModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.Id" }], javaType: "java.lang.Long" } }));
    }
    get nroFactura(): StringModel_1 {
        return this[_getPropertyModel_1]("nroFactura", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotBlank_1({ message: "El n\u00FAmero de factura es obligatorio" })], meta: { javaType: "java.lang.String" } }));
    }
    get persona(): PersonaModel_1 {
        return this[_getPropertyModel_1]("persona", (parent, key) => new PersonaModel_1(parent, key, true, { validators: [new NotNull_1({ message: "La persona es obligatoria" })], meta: { annotations: [{ name: "jakarta.persistence.ManyToOne" }] } }));
    }
    get detalles(): ArrayModel_1<DetalleFacturaModel_1> {
        return this[_getPropertyModel_1]("detalles", (parent, key) => new ArrayModel_1(parent, key, true, (parent, key) => new DetalleFacturaModel_1(parent, key, true), { meta: { annotations: [{ name: "jakarta.persistence.OneToMany" }], javaType: "java.util.List" } }));
    }
    get total(): NumberModel_1 {
        return this[_getPropertyModel_1]("total", (parent, key) => new NumberModel_1(parent, key, false, { validators: [new PositiveOrZero_1({ message: "El total debe ser cero o positivo" })], meta: { javaType: "float" } }));
    }
}
export default FacturaModel;
