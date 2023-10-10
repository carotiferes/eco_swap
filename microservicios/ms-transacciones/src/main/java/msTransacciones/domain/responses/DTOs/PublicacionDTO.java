package msTransacciones.domain.responses.DTOs;

import lombok.Data;
import msTransacciones.domain.entities.CaracteristicaProducto;
import msTransacciones.domain.entities.enums.EstadoPublicacion;
import msTransacciones.domain.entities.enums.TipoPublicacion;

import java.time.LocalDate;
import java.util.List;

@Data
public class PublicacionDTO {

    private Long idPublicacion;
    private String titulo;
    private String descripcion;
    private EstadoPublicacion estadoPublicacion;
    private TipoPublicacion tipoPublicacion;
    private LocalDate fechaPublicacion;
    private Double precioVenta;
    private Double valorTruequeMax;
    private Double valorTruequeMin;
    private ParticularDTO particularDTO;
    private List<CaracteristicaProducto> caracteristicaProducto;
    private String imagenes;
}
