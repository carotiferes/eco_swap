package msAutenticacion.domain.model.enums;

import msAutenticacion.domain.model.EnumValue;

public enum TipoDocumentoEnum {
    @EnumValue(description = "DNI")
    DNI,
    @EnumValue(description = "CIVICA")
    CIVICA;

    private String descripcion;
}
