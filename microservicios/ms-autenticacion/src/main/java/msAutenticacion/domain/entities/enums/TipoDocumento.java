package msAutenticacion.domain.entities.enums;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public enum TipoDocumento {

    DNI, LC, LI, LE;
    private String descripcion;
}
