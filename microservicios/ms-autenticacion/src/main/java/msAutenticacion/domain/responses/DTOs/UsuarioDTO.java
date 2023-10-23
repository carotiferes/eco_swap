package msAutenticacion.domain.responses.DTOs;

import lombok.Data;
import msAutenticacion.domain.entities.Direccion;

import java.util.List;

@Data
public class UsuarioDTO {

        private long idUsuario;
        private String username;
        private String telefono;
        private String email;
        private float puntaje;
        private boolean isSwapper;
        private Integer intentos;
        private boolean bloqueado;
        private boolean validado;
        private FundacionDTO fundacionDTO;
        private ParticularDTO particularDTO;
        private String avatar;
}
