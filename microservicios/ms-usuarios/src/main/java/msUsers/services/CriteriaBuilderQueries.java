package msUsers.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Fundacion;
import msUsers.domain.entities.Particular;
import msUsers.domain.entities.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CriteriaBuilderQueries {
    @Autowired
    EntityManager entityManager;

    public Optional<Fundacion> getFundacionPorUsuario(Long idUsuario){

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Fundacion> query = cb.createQuery(Fundacion.class);
        Root<Fundacion> from = query.from(Fundacion.class);
        Predicate predicate = cb.conjunction();

        Join<Fundacion, Usuario> join = from.join("usuario");
        predicate = cb.and(predicate, cb.equal(join.get("id"), idUsuario));

        query.where(predicate);

        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }

    public Optional<Particular> getParticularPorUsuario(Long idUsuario){

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Particular> query = cb.createQuery(Particular.class);
        Root<Particular> from = query.from(Particular.class);
        Predicate predicate = cb.conjunction();

        Join<Particular, Usuario> join = from.join("usuario");
        predicate = cb.and(predicate, cb.equal(join.get("id"), idUsuario));

        query.where(predicate);

        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }

}
