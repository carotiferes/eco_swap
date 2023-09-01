package msAutenticacion.services;

import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Fundacion;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.repositories.FundacionesRepository;
import msAutenticacion.domain.requests.RequestSignUp;
import msAutenticacion.domain.requests.propuestas.RequestSignUpFundacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FundacionService {

    @Autowired
    private FundacionesRepository fundacionesRepository;

    public Usuario crearUser(Usuario usuario, RequestSignUp requestSignUp) {
        log.info("Creando usuario como FUNDACION");
        RequestSignUpFundacion request = requestSignUp.getFundacion();
        Fundacion fundacion = Fundacion.builder()
                .baja(false)
                .cuil(request.getCuil())
                .nombre(request.getNombre())
                .usuario(usuario)
                .build();
        Fundacion creado = fundacionesRepository.save(fundacion);
        log.info("Usuario fundacion creado con ID: "+creado.getIdFundacion());
        return creado.getUsuario();
    }
}
