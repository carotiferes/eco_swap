package msUsers.domain.responses.DTOs;

import lombok.Data;
import msUsers.domain.entities.enums.EstadoCompra;
import msUsers.domain.logistica.enums.EstadoEnvio;

import java.time.ZonedDateTime;

@Data
public class CompraDTO {
    private PublicacionDTO publicacionDTO;
    private ParticularDTO particularCompradorDTO;
    private Long idCompra;
    private String idPaymentMercadoPago;
    private String idPreferenceMercadoPago;
    private EstadoCompra estadoCompra;
    private EstadoEnvio estadoEnvio;
    private ZonedDateTime dateApproved;
}
