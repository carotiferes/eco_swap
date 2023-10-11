package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Compra;
import msUsers.domain.entities.Particular;
import msUsers.domain.entities.Publicacion;
import msUsers.domain.entities.Usuario;
import msUsers.domain.entities.enums.EstadoCompra;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.responses.DTOs.CompraDTO;
import msUsers.domain.responses.DTOs.PublicacionDTO;
import msUsers.services.CriteriaBuilderQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class CompraController {

    @Autowired
    CriteriaBuilderQueries criteriaBuilderQueries;
    @Autowired
    EntityManager entityManager;
    private static final String json = "application/json";
    @GetMapping(path = "/misCompras", produces = json)
    public ResponseEntity<List<CompraDTO>> getMisCompras() {

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(user.getIdUsuario());
        Particular particular = optionalParticular.orElseThrow(() -> new EntityNotFoundException("¡El particular no existe!"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Compra> query = cb.createQuery(Compra.class);
        Root<Compra> from = query.from(Compra.class);
        Predicate predicate = cb.conjunction();

        Join<Compra, Particular> join = from.join("particularComprador");
        predicate = cb.and(
                cb.equal(join.get("idParticular"), particular.getIdParticular()),
                cb.equal(from.get("estadoCompra"), EstadoCompra.APROBADA)
        );

        query.where(predicate);

        List<Compra> publicaciones = entityManager.createQuery(query).getResultList();
        List<CompraDTO> compraDTOs = publicaciones.stream().map(Compra::toDTO).toList();

        return ResponseEntity.ok(compraDTOs);
    }

}
