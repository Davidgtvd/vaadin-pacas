import { _enum as _enum_1, EnumModel as EnumModel_1, makeEnumEmptyValueCreator as makeEnumEmptyValueCreator_1 } from "@vaadin/hilla-lit-form";
import TipoIdentificacion_1 from "./TipoIdentificacion.js";
class TipoIdentificacionModel extends EnumModel_1<typeof TipoIdentificacion_1> {
    static override createEmptyValue = makeEnumEmptyValueCreator_1(TipoIdentificacionModel);
    readonly [_enum_1] = TipoIdentificacion_1;
}
export default TipoIdentificacionModel;
