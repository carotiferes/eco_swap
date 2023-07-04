package msUsers.domain.requests;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import msUsers.domain.entities.enums.TipoProducto;

public class RequestFilterSolicitudes {
    private Long idFundacion;
    private Long idPerfil; // Para la ubicación
    private TipoProducto tipoProducto;

    public Long getIdFundacion() {
        return idFundacion;
    }

    public void setIdFundacion(Long idFundacion) {
        this.idFundacion = idFundacion;
    }

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
}