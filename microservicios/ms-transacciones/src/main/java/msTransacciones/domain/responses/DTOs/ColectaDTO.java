package msTransacciones.domain.responses.DTOs;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ColectaDTO {

    private long idColecta;
    private FundacionDTO fundacionDTO;
    private String titulo;
    private String imagen;
    private boolean activa;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<ProductoDTO> productos;

}
