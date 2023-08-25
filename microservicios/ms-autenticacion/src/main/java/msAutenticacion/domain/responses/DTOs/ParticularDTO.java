package msAutenticacion.domain.responses.DTOs;

import lombok.Data;
import msAutenticacion.domain.entities.enums.TipoDocumento;

import java.time.LocalDate;
import java.util.List;

@Data
public class ParticularDTO {
    private Long idParticular;
    private String nombre;
    private String apellido;
    private String dni;
    private String cuil;
    private LocalDate fechaNacimiento;
    private TipoDocumento tipoDocumento;
    private Integer puntaje;
    private List<DireccionDTO> direcciones;
}
