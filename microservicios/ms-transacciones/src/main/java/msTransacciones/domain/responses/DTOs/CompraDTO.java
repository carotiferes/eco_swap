package msTransacciones.domain.responses.DTOs;

import lombok.Data;
import msTransacciones.domain.entities.enums.EstadoCompra;

import java.time.ZonedDateTime;

@Data
public class CompraDTO {
    private PublicacionDTO publicacionDTO;
    private ParticularDTO particularCompradorDTO;
    private Long idCompra;
    private String idPaymentMercadoPago;
    private String idPreferenceMercadoPago;
    private EstadoCompra estadoCompra;
    private ZonedDateTime dateApproved;
}
