package msTransacciones.domain.responses.DTOs;


import lombok.Data;
import msTransacciones.domain.entities.CaracteristicaDonacion;
import msTransacciones.domain.entities.enums.EstadoDonacion;

import java.time.LocalDate;
import java.util.List;

@Data
public class DonacionDTO {
    private long idDonacion;
    private String descripcion;
    private int cantidadDonacion;
    private EstadoDonacion estadoDonacion;
    private ParticularDTO particularDTO;
    private ProductoDTO producto;
    private List<CaracteristicaDonacion> caracteristicaDonacion;
    private LocalDate fechaDonacion;
    private String imagenes;
}
