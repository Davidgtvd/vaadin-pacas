import { _getPropertyModel as _getPropertyModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import type Compra_1 from "./Compra.js";
import PersonaModel_1 from "./PersonaModel.js";
class CompraModel<T extends Compra_1 = Compra_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(CompraModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.Id" }], javaType: "java.lang.Long" } }));
    }
    get subtotal(): NumberModel_1 {
        return this[_getPropertyModel_1]("subtotal", (parent, key) => new NumberModel_1(parent, key, false, { meta: { javaType: "float" } }));
    }
    get nroFactura(): StringModel_1 {
        return this[_getPropertyModel_1]("nroFactura", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get iva(): NumberModel_1 {
        return this[_getPropertyModel_1]("iva", (parent, key) => new NumberModel_1(parent, key, false, { meta: { javaType: "float" } }));
    }
    get total(): NumberModel_1 {
        return this[_getPropertyModel_1]("total", (parent, key) => new NumberModel_1(parent, key, false, { meta: { javaType: "float" } }));
    }
    get persona(): PersonaModel_1 {
        return this[_getPropertyModel_1]("persona", (parent, key) => new PersonaModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.ManyToOne" }] } }));
    }
}
export default CompraModel;
