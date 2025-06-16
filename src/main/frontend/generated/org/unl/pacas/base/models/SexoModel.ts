import { _enum as _enum_1, EnumModel as EnumModel_1, makeEnumEmptyValueCreator as makeEnumEmptyValueCreator_1 } from "@vaadin/hilla-lit-form";
import Sexo_1 from "./Sexo.js";
class SexoModel extends EnumModel_1<typeof Sexo_1> {
    static override createEmptyValue = makeEnumEmptyValueCreator_1(SexoModel);
    readonly [_enum_1] = Sexo_1;
}
export default SexoModel;
