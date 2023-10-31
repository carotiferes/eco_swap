package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Particular;
import msUsers.domain.entities.Publicacion;
import msUsers.domain.entities.Usuario;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.repositories.ParticularesRepository;
import msUsers.domain.responses.DTOs.ParticularDTO;
import msUsers.domain.responses.DTOs.ProductoDTO;
import msUsers.domain.responses.DTOs.PublicacionDTO;
import msUsers.services.CriteriaBuilderQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api")
public class ParticularController {

    @Autowired
    ParticularesRepository particularesRepository;

    @Autowired
    CriteriaBuilderQueries criteriaBuilderQueries;

    @Autowired
    EntityManager entityManager;

    private static final String json = "application/json";
    @GetMapping(path = "/particular/{id_particular}", produces = json)
    @Transactional(readOnly = true)
    public ResponseEntity<ParticularDTO> getParticularById(@PathVariable("id_particular") Long idParticular){
        final var particular = this.particularesRepository.findById(idParticular).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el particular: " + idParticular));

        ParticularDTO particularDTO = particular.toDTO();

        return ResponseEntity.ok(particularDTO);
    }
}
