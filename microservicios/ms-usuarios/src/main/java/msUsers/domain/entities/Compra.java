package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import msUsers.converters.ZonedDateTimeConverter;
import msUsers.domain.entities.enums.EstadoCompra;
import msUsers.domain.entities.enums.EstadoEnvio;
import msUsers.domain.responses.DTOs.CompraDTO;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "Compras")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idCompra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idCompra;

    @ManyToOne
    @JoinColumn(name = "id_particular")
    private Particular particularComprador;

    @ManyToOne
    @JoinColumn(name = "id_publicacion")
    private Publicacion publicacion;

    @Column(name = "id_payment")
    private String idPaymentMercadoPago;

    @Column(name = "id_preference")
    private String idPreferenceMercadoPago;

    @Enumerated(EnumType.STRING)
    private EstadoCompra estadoCompra;

    @Column(name = "date_approved")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime dateApproved;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estadoEnvio;

    public CompraDTO toDTO(){
        CompraDTO compraDTO = new CompraDTO();
        compraDTO.setIdCompra(idCompra);
        compraDTO.setPublicacionDTO(publicacion.toDTO());
        compraDTO.setParticularCompradorDTO(particularComprador.toDTO());
        compraDTO.setEstadoCompra(estadoCompra);
        compraDTO.setIdPreferenceMercadoPago(idPreferenceMercadoPago);
        compraDTO.setIdPaymentMercadoPago(idPaymentMercadoPago);
        compraDTO.setEstadoEnvio(estadoEnvio);
        return compraDTO;
    }
}
