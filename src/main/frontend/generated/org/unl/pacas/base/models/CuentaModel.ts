import { _getPropertyModel as _getPropertyModel_1, BooleanModel as BooleanModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NotBlank as NotBlank_1, NotNull as NotNull_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, Pattern as Pattern_1, Size as Size_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import type Cuenta_1 from "./Cuenta.js";
import PersonaModel_1 from "./PersonaModel.js";
import RolModel_1 from "./RolModel.js";
class CuentaModel<T extends Cuenta_1 = Cuenta_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(CuentaModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.Id" }], javaType: "java.lang.Long" } }));
    }
    get usuario(): StringModel_1 {
        return this[_getPropertyModel_1]("usuario", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotBlank_1({ message: "El usuario es obligatorio" }), new Size_1({ min: 3, max: 50, message: "El usuario debe tener entre 3 y 50 caracteres" }), new Pattern_1({ regexp: "^[a-zA-Z0-9._-]+$", message: "El usuario solo puede contener letras, n\u00FAmeros, puntos, guiones y guiones bajos" })], meta: { javaType: "java.lang.String" } }));
    }
    get contrasena(): StringModel_1 {
        return this[_getPropertyModel_1]("contrasena", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotBlank_1({ message: "La contrase\u00F1a es obligatoria" }), new Size_1({ min: 6, max: 255, message: "La contrase\u00F1a debe tener al menos 6 caracteres" })], meta: { javaType: "java.lang.String" } }));
    }
    get activo(): BooleanModel_1 {
        return this[_getPropertyModel_1]("activo", (parent, key) => new BooleanModel_1(parent, key, true, { validators: [new NotNull_1()], meta: { javaType: "java.lang.Boolean" } }));
    }
    get fechaCreacion(): StringModel_1 {
        return this[_getPropertyModel_1]("fechaCreacion", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.time.LocalDateTime" } }));
    }
    get ultimoAcceso(): StringModel_1 {
        return this[_getPropertyModel_1]("ultimoAcceso", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.time.LocalDateTime" } }));
    }
    get intentosFallidos(): NumberModel_1 {
        return this[_getPropertyModel_1]("intentosFallidos", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.lang.Integer" } }));
    }
    get fechaBloqueo(): StringModel_1 {
        return this[_getPropertyModel_1]("fechaBloqueo", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.time.LocalDateTime" } }));
    }
    get rol(): RolModel_1 {
        return this[_getPropertyModel_1]("rol", (parent, key) => new RolModel_1(parent, key, true, { validators: [new NotNull_1({ message: "El rol es obligatorio" })], meta: { annotations: [{ name: "jakarta.persistence.ManyToOne" }] } }));
    }
    get persona(): PersonaModel_1 {
        return this[_getPropertyModel_1]("persona", (parent, key) => new PersonaModel_1(parent, key, true, { validators: [new NotNull_1({ message: "La persona es obligatoria" })], meta: { annotations: [{ name: "jakarta.persistence.OneToOne" }] } }));
    }
    get clave(): StringModel_1 {
        return this[_getPropertyModel_1]("clave", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get estadoTexto(): StringModel_1 {
        return this[_getPropertyModel_1]("estadoTexto", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get rolNombre(): StringModel_1 {
        return this[_getPropertyModel_1]("rolNombre", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get personaNombre(): StringModel_1 {
        return this[_getPropertyModel_1]("personaNombre", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get displayName(): StringModel_1 {
        return this[_getPropertyModel_1]("displayName", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
}
export default CuentaModel;
