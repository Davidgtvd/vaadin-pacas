import { _getPropertyModel as _getPropertyModel_1, BooleanModel as BooleanModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NotBlank as NotBlank_1, NotNull as NotNull_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import CompraModel_1 from "./CompraModel.js";
import CuentaModel_1 from "./CuentaModel.js";
import type Pago_1 from "./Pago.js";
class PagoModel<T extends Pago_1 = Pago_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(PagoModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.Id" }], javaType: "java.lang.Long" } }));
    }
    get codigoSeguridad(): StringModel_1 {
        return this[_getPropertyModel_1]("codigoSeguridad", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotBlank_1({ message: "El c\u00F3digo de seguridad es obligatorio" })], meta: { javaType: "java.lang.String" } }));
    }
    get metodoPago(): StringModel_1 {
        return this[_getPropertyModel_1]("metodoPago", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get estado(): BooleanModel_1 {
        return this[_getPropertyModel_1]("estado", (parent, key) => new BooleanModel_1(parent, key, true, { validators: [new NotNull_1({ message: "El estado es obligatorio" })], meta: { javaType: "java.lang.Boolean" } }));
    }
    get fechaPago(): StringModel_1 {
        return this[_getPropertyModel_1]("fechaPago", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotNull_1({ message: "La fecha de pago es obligatoria" })], meta: { javaType: "java.time.LocalDateTime" } }));
    }
    get cuenta(): CuentaModel_1 {
        return this[_getPropertyModel_1]("cuenta", (parent, key) => new CuentaModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.ManyToOne" }] } }));
    }
    get compra(): CompraModel_1 {
        return this[_getPropertyModel_1]("compra", (parent, key) => new CompraModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.ManyToOne" }] } }));
    }
}
export default PagoModel;
