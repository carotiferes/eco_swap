package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import msUsers.domain.entities.Fundacion;
import msUsers.domain.entities.Perfil;
import msUsers.domain.repositories.FundacionesRepository;
import msUsers.domain.repositories.PerfilRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    public ResponseEntity<List<String>> listFundaciones(){
        final var fundations = this.fundacionesRepository.findAll();
        List<String> fundaciones = fundations.stream().map(Fundacion::getNombre).collect(Collectors.toList());
        return ResponseEntity.ok(fundaciones);
    }
}
