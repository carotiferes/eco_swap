package msAutenticacion.services;

import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Particular;
import msAutenticacion.domain.entities.enums.TipoDocumento;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.repositories.ParticularRepository;
import msAutenticacion.domain.requests.RequestSignUp;
import msAutenticacion.domain.requests.propuestas.RequestSignUpSwapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class ParticularService {

    @Autowired
    private ParticularRepository particularRepository;

    public Usuario crearUser(Usuario usuario, RequestSignUp requestSignUp) {
        log.info("Creando usuario como PARTICULAR: {}", requestSignUp);
        RequestSignUpSwapper requestParticular = requestSignUp.getParticular();
        Particular particular = Particular.builder()
                .usuario(usuario)
                .apellido("apellido")
                .cuil(requestParticular.getCuil())
                .dni("39712     414")
                .fechaNacimiento(LocalDate.now())
                .nombre(requestParticular.getNombre())
                .tipoDocumento(TipoDocumento.valueOf("DNI"))
                .build();
        Particular creado = particularRepository.save(particular);
        log.info("Usuario Particular creado con ID: "+creado.getIdParticular());
        return creado.getUsuario();
    }
}
