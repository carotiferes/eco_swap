package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import msUsers.domain.entities.Fundacion;
import msUsers.domain.repositories.FundacionesRepository;
import msUsers.domain.responses.DTOs.FundacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class FundacionesController {
    private final FundacionesRepository fundacionesRepository;

    public FundacionesController(FundacionesRepository fundacionesRepository) {
        this.fundacionesRepository = fundacionesRepository;
    }

    private static final String json = "application/json";
    @GetMapping(path = "/fundacion/{id_fundacion}", produces = json)
    public ResponseEntity<Fundacion> getFundacionById(@PathVariable("id_fundacion") Long id){
        final var fundacion = this.fundacionesRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la fundación: " + id));
        return ResponseEntity.ok(fundacion);
    }
    @GetMapping(path = "/fundaciones", produces = json)
    public ResponseEntity<List<FundacionDTO>> listFundaciones(){
        final var fundaciones = this.fundacionesRepository.findAll();
        List<FundacionDTO> fundacionesDTOS = fundaciones.stream().map(fundacion -> {
            FundacionDTO f = new FundacionDTO();
            f.setIdFundacion(fundacion.getIdFundacion());
            f.setNombre(fundacion.getNombre());
            return f;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(fundacionesDTOS);
    }
}
