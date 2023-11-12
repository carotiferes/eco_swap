package msUsers.domain.responses.DTOs;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import msUsers.domain.entities.OrdenDeEnvio;
import msUsers.domain.entities.Producto;
import msUsers.domain.requests.logistica.PostProductosRequest;

@Data
public class ProductosADonarDeOrdenDTO {
    private long idOrden;
    private long idProductoADonar;
    private long idProducto;
    private long cantidad;
    private Long idDonacion;
}
