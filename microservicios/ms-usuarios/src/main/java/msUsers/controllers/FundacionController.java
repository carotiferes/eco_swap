package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import msUsers.domain.entities.Fundacion;
import msUsers.domain.repositories.FundacionesRepository;
import msUsers.domain.responses.DTOs.FundacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class FundacionController {

    @Autowired
    FundacionesRepository fundacionesRepository;

    private static final String json = "application/json";
    @GetMapping(path = "/fundacion/{id_fundacion}", produces = json)
    public ResponseEntity<FundacionDTO> getFundacionById(@PathVariable("id_fundacion") Long id){
        final var fundacion = this.fundacionesRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la fundación: " + id));

        FundacionDTO fundacionDTO = fundacion.toDTO();

        return ResponseEntity.ok(fundacionDTO);
    }
    @GetMapping(path = "/fundaciones", produces = json)
    public ResponseEntity<List<FundacionDTO>> listFundaciones(){
        final var fundaciones = this.fundacionesRepository.findAll();
        List<FundacionDTO> fundacionesDTOS = fundaciones.stream().map(Fundacion::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(fundacionesDTOS);
    }
}