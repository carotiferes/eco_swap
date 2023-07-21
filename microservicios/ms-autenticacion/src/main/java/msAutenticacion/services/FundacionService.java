package msAutenticacion.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Fundacion;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.repositories.FundacionesRepository;
import msAutenticacion.domain.requests.RequestSignin;
import msAutenticacion.domain.requests.propuestas.RequestSigninFundacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FundacionService {

    @Autowired
    private FundacionesRepository fundacionesRepository;

    public Usuario crearUser(Usuario usuario, RequestSignin requestSignin) {
        log.info("Creando usuario como FUNDACION");
        RequestSigninFundacion request = requestSignin.getFundacion();
        Fundacion fundacion = Fundacion.builder()
                .baja(false)
                .cuil(request.getCuil())
                .nombre(request.getNombre())
                .build();
        Fundacion creado = fundacionesRepository.save(fundacion);
        log.info("Usuario fundacion creado con ID: "+creado.getIdFundacion());
        return creado.getUsuario();
    }
}
