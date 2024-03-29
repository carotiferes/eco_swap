package msUsers.domain.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestMensajeChat {

    @JsonCreator
    public static RequestMensajeChat create(
            @JsonProperty("idTrueque") Long idTrueque,
            @JsonProperty("mensaje") String mensaje,
            @JsonProperty("fechaHoraEnvio") ZonedDateTime fechaHoraEnvio,
            @JsonProperty("usuarioReceptor") Long usuarioReceptor) {
        return new RequestMensajeChat(idTrueque, mensaje, usuarioReceptor, fechaHoraEnvio);
    }

    @NotNull(message = "Un mensaje debe estar asociado a un trueque.")
    private Long idTrueque;
    @NotNull(message = "Se debe enviar al menos un mensaje.")
    @NotBlank(message = "El mensaje no debe estar vacío.")
    private String mensaje;
    @NotNull(message = "El mensaje debe ser recibido por usuario válido.")
    private Long usuarioReceptor;
    @NotNull(message = "El mensaje debe tener una fecha.")
    private ZonedDateTime fechaHoraEnvio;
}
