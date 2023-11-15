package msUsers.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
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

    public OpinionDTO obtenerOpinionDTO(Opinion op) {
        Opinion opinion = this.opinionesRepository.findById(op.getIdOpinion())
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrada la opinion: " + op.getIdOpinion()));
        Particular particularOpina = this.criteriaBuilderQueries.getParticularPorUsuario(op.getUsuarioOpina().getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el particular: " + op.getUsuarioOpina().getIdUsuario()));
        Particular particularOpinado = this.criteriaBuilderQueries.getParticularPorUsuario(op.getUsuarioOpinado().getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el particular: " + op.getUsuarioOpinado().getIdUsuario()));

        UsuarioEnOpinionDTO usuarioOpinaDTO = op.getUsuarioOpina().toUsuarioEnOpinionDTO();
        usuarioOpinaDTO.setApellido(particularOpina.getApellido());
        usuarioOpinaDTO.setNombre(particularOpina.getNombre());

        UsuarioEnOpinionDTO usuarioOpinadoDTO = op.getUsuarioOpinado().toUsuarioEnOpinionDTO();
        usuarioOpinadoDTO.setApellido(particularOpinado.getApellido());
        usuarioOpinadoDTO.setNombre(particularOpinado.getNombre());

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
