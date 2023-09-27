package msUsers.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Fundacion;
import msUsers.domain.entities.Particular;
import msUsers.domain.entities.Usuario;
import msUsers.domain.responses.DTOs.ParticularDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {

    @Autowired
    EntityManager entityManager;

    public String getEmailPorJwt(String jwt){
        DecodedJWT decodedJWT = JWT.decode(jwt);
        String email = decodedJWT.getClaim("email").toString();
        log.info(">> El email del JWT es: {}", email);
        return email;
    }

    public ParticularDTO getParticularPorJwt(String jwt){
        //TODO: Agregar la verificación del JWT. Dependo de la Salt.

        DecodedJWT decodedJWT = JWT.decode(jwt);
        String email = decodedJWT.getClaim("email").toString();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Particular> query = cb.createQuery(Particular.class);
        Root<Particular> from = query.from(Particular.class);
        Predicate predicate = cb.conjunction();

        Join<Particular, Usuario> join = from.join("usuario");
        predicate = cb.and(predicate, cb.equal(join.get("email"), email));

        query.where(predicate);

        ParticularDTO particularDTO = new ParticularDTO();
        particularDTO.setApellido(particularDTO.getApellido());
        particularDTO.setDni(particularDTO.getDni());
        particularDTO.setCuil(particularDTO.getCuil());
        particularDTO.setNombre(particularDTO.getNombre());
        return particularDTO;
    }

    public Usuario getUsuarioPorJwt(String jwt){

        DecodedJWT decodedJWT = JWT.decode(jwt);
        String email = decodedJWT.getClaim("email").toString();
        String emailFormateado[] = email.split("\"");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
        Root<Usuario> from = query.from(Usuario.class);
        Predicate predicate = cb.equal(from.get("email"), emailFormateado[1]);

        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();

    }

    public Fundacion getFundacionPorJwt(String jwt){

        DecodedJWT decodedJWT = JWT.decode(jwt);
        String email = decodedJWT.getClaim("email").toString();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Fundacion> query = cb.createQuery(Fundacion.class);
        Root<Fundacion> from = query.from(Fundacion.class);
        Predicate predicate = cb.conjunction();

        Join<Fundacion, Usuario> join = from.join("usuario");
        predicate = cb.and(predicate, cb.equal(join.get("email"), email));

        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }

}
