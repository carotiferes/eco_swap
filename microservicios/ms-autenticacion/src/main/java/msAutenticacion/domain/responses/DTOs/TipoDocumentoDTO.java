package msAutenticacion.domain.responses.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoDTO {
    private String nombre;
    private String descripcion;
}
