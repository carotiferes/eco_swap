package msAutenticacion.services;

import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Fundacion;
import msAutenticacion.domain.entities.Particular;
import msAutenticacion.domain.entities.enums.TipoDocumento;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.repositories.ParticularRepository;
import msAutenticacion.domain.requests.RequestSignin;
import msAutenticacion.domain.requests.propuestas.RequestSigninSwapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParticularService {

    @Autowired
    private ParticularRepository particularRepository;

    public Usuario crearUser(Usuario usuario, RequestSignin requestSignin) {
        log.info("Creando usuario como PARTICULAR: {}", requestSignin);
        RequestSigninSwapper requestParticular = requestSignin.getParticular();
        Particular particular = Particular.builder()
                .usuario(usuario)
                .apellido(requestParticular.getApellido())
                .cuil(requestParticular.getCuil())
                .dni(requestParticular.getDni())
                .fechaNacimiento(requestParticular.getFechaNacimiento())
                .nombre(requestParticular.getNombre())
                .tipoDocumento(TipoDocumento.valueOf(requestParticular.getTipoDocumento()))
                .build();
        Particular creado = particularRepository.save(particular);
        log.info("Usuario Particular creado con ID: "+creado.getIdParticular());
        return creado.getUsuario();
    }
}
