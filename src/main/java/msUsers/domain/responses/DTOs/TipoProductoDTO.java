package msUsers.domain.responses.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TipoProductoDTO {
    private String nombre;
    private String descripcion;
}
