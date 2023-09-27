package msUsers.domain.responses.DTOs;

import lombok.Data;

@Data
public class CompraDTO {
    private PublicacionDTO publicacionDTO;
    private ParticularDTO particularCompradorDTO;
    private Long idCompra;
}
