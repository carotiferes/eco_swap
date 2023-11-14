package msUsers.domain.responses.DTOs;


import lombok.Data;
import msUsers.domain.entities.CaracteristicaDonacion;
import msUsers.domain.entities.Donacion;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.logistica.enums.EstadoEnvio;

import java.time.LocalDate;
import java.util.List;

@Data
public class DonacionDTO {
    private long idDonacion;
    private String descripcion;
    private int cantidadDonacion;
    private EstadoDonacion estadoDonacion;
    private EstadoEnvio estadoEnvio;
    private ParticularDTO particularDTO;
    private ProductoDTO producto;
    private List<CaracteristicaDonacion> caracteristicaDonacion;
    private LocalDate fechaDonacion;
    private String imagenes;
}
