package msUsers.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Fundacion;
import msUsers.domain.entities.Opinion;
import msUsers.domain.entities.Particular;
import msUsers.domain.entities.Usuario;
import msUsers.domain.repositories.OpinionesRepository;
import msUsers.domain.repositories.UsuariosRepository;
import msUsers.domain.responses.DTOs.OpinionDTO;
import msUsers.domain.responses.DTOs.UsuarioDTO;
import msUsers.domain.responses.DTOs.UsuarioEnOpinionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OpinionesService {
    @Autowired
    private OpinionesRepository opinionesRepository;
    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private CriteriaBuilderQueries criteriaBuilderQueries;

    public OpinionDTO obtenerOpinionDTO(Opinion opinion) {

        Particular particularOpina;
        Particular particularOpinado;
        Fundacion fundacionOpina;
        Fundacion fundacionOpinado;
        UsuarioEnOpinionDTO usuarioOpinaDTO = opinion.getUsuarioOpina().toUsuarioEnOpinionDTO();
        UsuarioEnOpinionDTO usuarioOpinadoDTO = opinion.getUsuarioOpinado().toUsuarioEnOpinionDTO();

        if(opinion.getUsuarioOpina().isSwapper()){
            particularOpina = this.criteriaBuilderQueries.getParticularPorUsuario(opinion.getUsuarioOpina().getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el particular: " + opinion.getUsuarioOpina().getIdUsuario()));
            usuarioOpinaDTO.setNombre(particularOpina.getNombre());
            usuarioOpinaDTO.setApellido(particularOpina.getApellido());
        }
        else {
            fundacionOpina = this.criteriaBuilderQueries.getFundacionPorUsuario(opinion.getUsuarioOpina().getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("No fue encontrado la fundación: " + opinion.getUsuarioOpina().getIdUsuario()));
            usuarioOpinaDTO.setNombre(fundacionOpina.getNombre());
        }

        if(opinion.getUsuarioOpinado().isSwapper()){
            particularOpinado = this.criteriaBuilderQueries.getParticularPorUsuario(opinion.getUsuarioOpinado().getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el particular: " + opinion.getUsuarioOpinado().getIdUsuario()));
            usuarioOpinadoDTO.setApellido(particularOpinado.getApellido());
            usuarioOpinadoDTO.setNombre(particularOpinado.getNombre());
        }else{
            fundacionOpinado = this.criteriaBuilderQueries.getFundacionPorUsuario(opinion.getUsuarioOpinado().getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("No fue encontrado la fundación: " + opinion.getUsuarioOpina().getIdUsuario()));
            usuarioOpinaDTO.setNombre(fundacionOpinado.getNombre());
        }

        OpinionDTO opinionDTO = new OpinionDTO();
        opinionDTO.setIdOpinion(opinion.getIdOpinion());
        opinionDTO.setDescripcion(opinion.getDescripcion());
        opinionDTO.setValoracion(opinion.getValoracion());
        opinionDTO.setUsuarioOpina(usuarioOpinaDTO);
        opinionDTO.setUsuarioOpinado(usuarioOpinadoDTO);
        opinionDTO.setFechaHoraOpinion(opinion.getFechaHoraOpinion());
        return opinionDTO;
    }
}
