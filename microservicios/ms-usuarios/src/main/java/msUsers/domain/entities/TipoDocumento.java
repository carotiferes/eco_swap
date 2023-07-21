package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "TipoDocumentos")
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idTipoDocumento;

    @Size(max = 20)
    private String descripción;

}
