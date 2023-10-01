package msTransacciones.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import msTransacciones.domain.responses.DTOs.CompraDTO;

@Entity
@Data
@Table(name = "Compras")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idCompra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idCompra;

    @OneToOne
    @JoinColumn(name = "id_particular")
    private Particular particularComprador;

    @OneToOne
    @JoinColumn(name = "id_publicacion")
    private Publicacion publicacion;

    // Aca agregar datos de las transacciones (MercadoPago, por ejemplo)

    public CompraDTO toDTO(){
        CompraDTO compraDTO = new CompraDTO();
        compraDTO.setIdCompra(idCompra);
        compraDTO.setPublicacionDTO(publicacion.toDTO());
        compraDTO.setParticularCompradorDTO(particularComprador.toDTO());
        return compraDTO;
    }
}
