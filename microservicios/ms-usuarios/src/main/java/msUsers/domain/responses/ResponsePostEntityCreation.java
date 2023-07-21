package msUsers.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponsePostEntityCreation {
    private long id;
    private String descripcion;
    private String status;
}
