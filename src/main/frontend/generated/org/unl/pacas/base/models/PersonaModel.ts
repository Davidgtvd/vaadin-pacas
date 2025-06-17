import { _getPropertyModel as _getPropertyModel_1, ArrayModel as ArrayModel_1, Email as Email_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NotBlank as NotBlank_1, NotNull as NotNull_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, Past as Past_1, Pattern as Pattern_1, Size as Size_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import CuentaModel_1 from "./CuentaModel.js";
import FacturaModel_1 from "./FacturaModel.js";
import type Persona_1 from "./Persona.js";
import SexoModel_1 from "./SexoModel.js";
import TipoIdentificacionModel_1 from "./TipoIdentificacionModel.js";
class PersonaModel<T extends Persona_1 = Persona_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(PersonaModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.Id" }], javaType: "java.lang.Long" } }));
    }
    get nombres(): StringModel_1 {
        return this[_getPropertyModel_1]("nombres", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotBlank_1({ message: "Los nombres son obligatorios" }), new Size_1({ max: 100, message: "Los nombres no pueden tener m\u00E1s de 100 caracteres" })], meta: { javaType: "java.lang.String" } }));
    }
    get apellidos(): StringModel_1 {
        return this[_getPropertyModel_1]("apellidos", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotBlank_1({ message: "Los apellidos son obligatorios" }), new Size_1({ max: 100, message: "Los apellidos no pueden tener m\u00E1s de 100 caracteres" })], meta: { javaType: "java.lang.String" } }));
    }
    get email(): StringModel_1 {
        return this[_getPropertyModel_1]("email", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotBlank_1({ message: "El email es obligatorio" }), new Email_1({ message: "El email debe tener un formato v\u00E1lido" }), new Size_1({ max: 150, message: "El email no puede tener m\u00E1s de 150 caracteres" })], meta: { javaType: "java.lang.String" } }));
    }
    get telefono(): StringModel_1 {
        return this[_getPropertyModel_1]("telefono", (parent, key) => new StringModel_1(parent, key, true, { validators: [new Pattern_1({ regexp: "^[0-9+\\-\\s()]*$", message: "El tel\u00E9fono solo puede contener n\u00FAmeros, espacios y s\u00EDmbolos +, -, (, )" }), new Size_1({ max: 15, message: "El tel\u00E9fono no puede tener m\u00E1s de 15 caracteres" })], meta: { javaType: "java.lang.String" } }));
    }
    get tipoIdentificacion(): TipoIdentificacionModel_1 {
        return this[_getPropertyModel_1]("tipoIdentificacion", (parent, key) => new TipoIdentificacionModel_1(parent, key, true, { validators: [new NotNull_1({ message: "El tipo de identificaci\u00F3n es obligatorio" })] }));
    }
    get identificacion(): StringModel_1 {
        return this[_getPropertyModel_1]("identificacion", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotBlank_1({ message: "La identificaci\u00F3n es obligatoria" }), new Size_1({ max: 20, message: "La identificaci\u00F3n no puede tener m\u00E1s de 20 caracteres" })], meta: { javaType: "java.lang.String" } }));
    }
    get sexo(): SexoModel_1 {
        return this[_getPropertyModel_1]("sexo", (parent, key) => new SexoModel_1(parent, key, true, { validators: [new NotNull_1({ message: "El sexo es obligatorio" })] }));
    }
    get direccion(): StringModel_1 {
        return this[_getPropertyModel_1]("direccion", (parent, key) => new StringModel_1(parent, key, true, { validators: [new Size_1({ max: 200, message: "La direcci\u00F3n no puede tener m\u00E1s de 200 caracteres" })], meta: { javaType: "java.lang.String" } }));
    }
    get fechaNacimiento(): StringModel_1 {
        return this[_getPropertyModel_1]("fechaNacimiento", (parent, key) => new StringModel_1(parent, key, true, { validators: [new Past_1({ message: "La fecha de nacimiento debe ser anterior a hoy" })], meta: { javaType: "java.time.LocalDate" } }));
    }
    get cuenta(): CuentaModel_1 {
        return this[_getPropertyModel_1]("cuenta", (parent, key) => new CuentaModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.OneToOne" }] } }));
    }
    get facturas(): ArrayModel_1<FacturaModel_1> {
        return this[_getPropertyModel_1]("facturas", (parent, key) => new ArrayModel_1(parent, key, true, (parent, key) => new FacturaModel_1(parent, key, true), { meta: { annotations: [{ name: "jakarta.persistence.OneToMany" }], javaType: "java.util.List" } }));
    }
    get nombreCompleto(): StringModel_1 {
        return this[_getPropertyModel_1]("nombreCompleto", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
}
export default PersonaModel;
