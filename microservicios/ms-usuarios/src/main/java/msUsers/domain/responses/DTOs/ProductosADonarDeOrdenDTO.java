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
    private long idProductoADonar; // Es la primary key de la tabla, no es un dato.
    private long idOrden;
    private ProductoDTO producto;
    private long cantidad;
    private Long idDonacion;
}
