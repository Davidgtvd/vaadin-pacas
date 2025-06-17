import { _getPropertyModel as _getPropertyModel_1, ArrayModel as ArrayModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NotBlank as NotBlank_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, Size as Size_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import CuentaModel_1 from "./CuentaModel.js";
import type Rol_1 from "./Rol.js";
class RolModel<T extends Rol_1 = Rol_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(RolModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.Id" }], javaType: "java.lang.Long" } }));
    }
    get nombre(): StringModel_1 {
        return this[_getPropertyModel_1]("nombre", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotBlank_1({ message: "El nombre es obligatorio" }), new Size_1({ max: 50, message: "El nombre no puede tener m\u00E1s de 50 caracteres" })], meta: { javaType: "java.lang.String" } }));
    }
    get descripcion(): StringModel_1 {
        return this[_getPropertyModel_1]("descripcion", (parent, key) => new StringModel_1(parent, key, true, { validators: [new Size_1({ max: 200, message: "La descripci\u00F3n no puede tener m\u00E1s de 200 caracteres" })], meta: { javaType: "java.lang.String" } }));
    }
    get cuentas(): ArrayModel_1<CuentaModel_1> {
        return this[_getPropertyModel_1]("cuentas", (parent, key) => new ArrayModel_1(parent, key, true, (parent, key) => new CuentaModel_1(parent, key, true), { meta: { annotations: [{ name: "jakarta.persistence.OneToMany" }], javaType: "java.util.List" } }));
    }
    get displayName(): StringModel_1 {
        return this[_getPropertyModel_1]("displayName", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
}
export default RolModel;
