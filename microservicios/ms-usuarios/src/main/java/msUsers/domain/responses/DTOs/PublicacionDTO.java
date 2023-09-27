package msUsers.domain.responses.DTOs;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import msUsers.domain.entities.CaracteristicaProducto;
import msUsers.domain.entities.Particular;
import msUsers.domain.entities.Producto;
import msUsers.domain.entities.enums.EstadoPublicacion;
import msUsers.domain.entities.enums.TipoPublicacion;

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
    private List<ProductoDTO> productos;
    private String imagenes;
}
