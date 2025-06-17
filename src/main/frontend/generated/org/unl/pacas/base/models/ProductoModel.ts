import { _getPropertyModel as _getPropertyModel_1, BooleanModel as BooleanModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NotEmpty as NotEmpty_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import type Producto_1 from "./Producto.js";
class ProductoModel<T extends Producto_1 = Producto_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(ProductoModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, true, { meta: { annotations: [{ name: "jakarta.persistence.Id" }], javaType: "java.lang.Long" } }));
    }
    get codigo(): StringModel_1 {
        return this[_getPropertyModel_1]("codigo", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotEmpty_1()], meta: { javaType: "java.lang.String" } }));
    }
    get nombre(): StringModel_1 {
        return this[_getPropertyModel_1]("nombre", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotEmpty_1()], meta: { javaType: "java.lang.String" } }));
    }
    get descripcion(): StringModel_1 {
        return this[_getPropertyModel_1]("descripcion", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get categoria(): StringModel_1 {
        return this[_getPropertyModel_1]("categoria", (parent, key) => new StringModel_1(parent, key, true, { validators: [new NotEmpty_1()], meta: { javaType: "java.lang.String" } }));
    }
    get unidadMedida(): StringModel_1 {
        return this[_getPropertyModel_1]("unidadMedida", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get marca(): StringModel_1 {
        return this[_getPropertyModel_1]("marca", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get modelo(): StringModel_1 {
        return this[_getPropertyModel_1]("modelo", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get ubicacion(): StringModel_1 {
        return this[_getPropertyModel_1]("ubicacion", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get proveedor(): StringModel_1 {
        return this[_getPropertyModel_1]("proveedor", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get activo(): BooleanModel_1 {
        return this[_getPropertyModel_1]("activo", (parent, key) => new BooleanModel_1(parent, key, true, { meta: { javaType: "java.lang.Boolean" } }));
    }
    get stock(): NumberModel_1 {
        return this[_getPropertyModel_1]("stock", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.lang.Integer" } }));
    }
    get stockMinimo(): NumberModel_1 {
        return this[_getPropertyModel_1]("stockMinimo", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.lang.Integer" } }));
    }
    get precio(): NumberModel_1 {
        return this[_getPropertyModel_1]("precio", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.math.BigDecimal" } }));
    }
    get precioCosto(): NumberModel_1 {
        return this[_getPropertyModel_1]("precioCosto", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.math.BigDecimal" } }));
    }
    get iva(): NumberModel_1 {
        return this[_getPropertyModel_1]("iva", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.math.BigDecimal" } }));
    }
    get fechaCreacion(): StringModel_1 {
        return this[_getPropertyModel_1]("fechaCreacion", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.time.LocalDateTime" } }));
    }
    get fechaActualizacion(): StringModel_1 {
        return this[_getPropertyModel_1]("fechaActualizacion", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.time.LocalDateTime" } }));
    }
}
export default ProductoModel;
