package msUsers.domain.responses.DTOs;

import lombok.Data;
import msUsers.domain.entities.CaracteristicaProducto;
import msUsers.domain.entities.enums.EstadoPublicacion;
import msUsers.domain.entities.enums.TipoPublicacion;
import msUsers.domain.logistica.enums.EstadoEnvio;

import java.time.LocalDate;
import java.util.List;

@Data
public class PublicacionDTO {

    private Long idPublicacion;
    private String titulo;
    private String descripcion;
    private EstadoPublicacion estadoPublicacion;
    private EstadoEnvio estadoEnvio;
    private TipoPublicacion tipoPublicacion;
    private LocalDate fechaPublicacion;
    private Double precioVenta;
    private Double valorTruequeMax;
    private Double valorTruequeMin;
    private ParticularDTO particularDTO;
    private List<CaracteristicaProducto> caracteristicaProducto;
    private String imagenes;
    private Double peso;
}
