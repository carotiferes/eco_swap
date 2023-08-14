package msUsers.domain.responses.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ColectaDTO {

    private long idColecta;
    private String fundacion;
    private long idFundacion;
    private String titulo;
    private String imagen;
    private boolean activa;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

}
