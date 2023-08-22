package msUsers.domain.responses.DTOs;


import lombok.Data;
import msUsers.domain.entities.CaracteristicaDonacion;
import msUsers.domain.entities.Donacion;
import msUsers.domain.entities.enums.EstadoDonacion;

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
    private String imagenes;
}
