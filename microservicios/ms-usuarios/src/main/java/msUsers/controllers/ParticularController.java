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


    // ToDo: Queda pendiente a definir si generamos una nueva entidad Compra o no.
//    @GetMapping(path = "/particular/misCompras", produces = json)
//    @Transactional(readOnly = true)
//    public ResponseEntity<List<PublicacionDTO>> getMisCompras(){
//
//        final Usuario user = UsuarioContext.getUsuario();
//        Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(user.getIdUsuario());
//        Particular particular = optionalParticular.orElseThrow(() -> new EntityNotFoundException("¡El particular no existe!"));
//
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Publicacion> query = cb.createQuery(Publicacion.class);
//        Root<Publicacion> from = query.from(Publicacion.class);
//        Predicate predicate = cb.conjunction();
//
//        Join<Publicacion, Particular> join = from.join("particular");
//        predicate = cb.and(predicate, cb.equal(join.get("idParticular"), particular.getIdParticular()));
//
//        query.where(predicate);
//
////        List<Publicacion> publicaciones = entityManager.createQuery(query).getResultList();
////        List<PublicacionDTO> publicacionesDTO = publicaciones.stream().map(publicacion -> {
////            PublicacionDTO publicacionDTO = new PublicacionDTO();
////            publicacionDTO.setTitulo(publicacion.getTitulo());
////            publicacionDTO.setEstadoPublicacion(publicacion.getEstadoPublicacion());
////            publicacionDTO.setFechaPublicacion(publicacion.getFechaPublicacion());
////            publicacionDTO.setDescripcion(publicacion.getDescripcion());
////            publicacionDTO.setImagenes(publicacion.getImagenes());
////            publicacionDTO.setCaracteristicaProducto(publicacion.getCaracteristicaProducto());
////            publicacionDTO.setParticularDTO(publicacion.getParticular().toDTO());
////
////            List<ProductoDTO> productos = publicacion.getProductos().stream()
////                    .map(producto -> {
////                        ProductoDTO productoDTO = new ProductoDTO();
////                        return producto.toDTO();
////                    }).collect(Collectors.toList());
////
////            publicacionDTO.setProductos(productos);
////            publicacionDTO.setValorTruequeMax(publicacion.getValorTruequeMax());
////            publicacionDTO.setValorTruequeMin(publicacion.getValorTruequeMin());
////            publicacionDTO.setPrecioVenta(publicacion.getPrecioVenta());
////            return publicacionDTO;
////        }).toList();
//
//        //
//
////        return ResponseEntity.ok(publicacionesDTO);
//
//    }
}
