package msUsers.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import msUsers.converters.ZonedDateTimeConverter;
import msUsers.domain.responses.DTOs.MensajeChatDTO;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "Mensajes")
public class MensajeChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_emisor_id")
    private Usuario usuarioEmisor;

    @ManyToOne
    @JoinColumn(name = "usuario_receptor_id")
    private Usuario usuarioReceptor;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "timestampEnvio")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime fechaHoraEnvio;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trueque")
    private Trueque trueque;

    public MensajeChatDTO toDTO(){
        MensajeChatDTO mensajeChatDTO = new MensajeChatDTO();
        mensajeChatDTO.setMensaje(mensaje);
        mensajeChatDTO.setFechaHoraEnvio(fechaHoraEnvio);
        mensajeChatDTO.setUsuarioEmisor(usuarioEmisor.toUsuarioEnChatDTO());
        mensajeChatDTO.setUsuarioReceptor(usuarioReceptor.toUsuarioEnChatDTO());
        return mensajeChatDTO;
    }
}