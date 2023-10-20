package msUsers.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Trueque;
import msUsers.domain.entities.enums.EstadoTrueque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TruequeService {

    @Autowired
    private EntityManager entityManager;

    public boolean existeTruequeConPublicaciones(Long idPublicacionOrigen, Long idPublicacionPropuesta) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Trueque> root = query.from(Trueque.class);

        Predicate condicion1 = criteriaBuilder.and(
                criteriaBuilder.equal(root.get("publicacionOrigen").get("id"), idPublicacionOrigen),
                criteriaBuilder.equal(root.get("publicacionPropuesta").get("id"), idPublicacionPropuesta)
        );
        Predicate condicion2 = criteriaBuilder.and(
                criteriaBuilder.equal(root.get("publicacionOrigen").get("id"), idPublicacionPropuesta),
                criteriaBuilder.equal(root.get("publicacionPropuesta").get("id"), idPublicacionOrigen)
        );

        Predicate condicionFinal = criteriaBuilder.or(condicion1, condicion2);

        query.select(criteriaBuilder.count(root));
        query.where(condicionFinal);

        Long count = entityManager.createQuery(query).getSingleResult();

        return count > 0;
    }

    public boolean existeTruequeConXPublicacion(Long idPublicacion){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Trueque> root = query.from(Trueque.class);

        Predicate condicion1 = criteriaBuilder.and(criteriaBuilder.equal(root.get("publicacionOrigen").get("id"), idPublicacion));
        Predicate condicion2 = criteriaBuilder.and(criteriaBuilder.equal(root.get("publicacionPropuesta").get("id"), idPublicacion));
        Predicate condicionFinal = criteriaBuilder.or(condicion1, condicion2);

        Predicate condicionEstadoPendiente  = criteriaBuilder.and(criteriaBuilder.notEqual(root.get("estadoTrueque"), EstadoTrueque.PENDIENTE));
        Predicate condicionEstadoAnulado = criteriaBuilder.and(criteriaBuilder.notEqual(root.get("estadoTrueque"), EstadoTrueque.ANULADO));

        condicionFinal = criteriaBuilder.and(condicionFinal, condicionEstadoAnulado, condicionEstadoPendiente);

        query.select(criteriaBuilder.count(root));
        query.where(condicionFinal);

        Long count = entityManager.createQuery(query).getSingleResult();

        return count > 0;
    }


}
