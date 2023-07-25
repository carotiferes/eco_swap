package msUsers.domain.entities.enums;

import msUsers.domain.model.EnumValue;

public enum TipoProducto {

    @EnumValue(description = "Todo lo que sea un objeto mueble.")
    MUEBLES,

    @EnumValue(description = "Colchones y frazadas")
    COLCHONES_Y_FRAZADAS,

    @EnumValue(description = "Celulares, computadoras, etc")
    TECNOLOGIA,

    @EnumValue(description = "Libros usados y/o nuevos")
    LIBROS,

    @EnumValue(description = "Blisters, jeringas, etc.")
    SALUD,

    @EnumValue(description = "Cosas varias")
    OTROS;
    private String descripcion;
}
