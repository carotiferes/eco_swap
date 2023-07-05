package msUsers.domain.requests;

import msUsers.domain.entities.enums.TipoProducto;

public class RequestFilterSolicitudes {
    private Long idFundacion;
    private String codigoPostal;
    private TipoProducto tipoProducto;

    public Long getIdFundacion() {
        return idFundacion;
    }

    public void setIdFundacion(Long idFundacion) {
        this.idFundacion = idFundacion;
    }

    public String getCodigoPostal() {   return codigoPostal;    }

    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal;  }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
}