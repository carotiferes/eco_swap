package msTransacciones.controllers;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msTransacciones.domain.entities.Particular;
import msTransacciones.domain.entities.Usuario;
import msTransacciones.domain.model.UsuarioContext;
import msTransacciones.domain.repositories.PublicacionesRepository;
import msTransacciones.services.CriteriaBuilderQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
@Validated
@RequestMapping("/ms-transacciones/api")
public class MercadoPagoController {

    @Autowired
    private CriteriaBuilderQueries criteriaBuilderQueries;

    @Autowired
    private PublicacionesRepository publicacionesRepository;

    private static final String json = "application/json";
    @PostMapping(path = "/comprar/{id_publicacion}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<Preference> comprarProducto(@PathVariable("id_publicacion") Long idPublicacion) throws MPException, MPApiException {

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(user.getIdUsuario());
        Particular particular = optionalParticular.orElseThrow(() -> new EntityNotFoundException("¡El particular no existe!"));

        final var publicacion = this.publicacionesRepository.findById(idPublicacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el producto: " + idPublicacion));

        log.info(">> Comienza la compra del producto: {}", idPublicacion);

        // MP
        PreferenceClient client = new PreferenceClient();
        List<PreferenceItemRequest> items = new ArrayList<>();
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title(publicacion.getTitulo())
                .description(publicacion.getDescripcion())
                .pictureUrl("https://ecoswap.com.ar:8080/api/getImage/154b0f0f-71fd-4aea-bdcd-e2ebb76402b1.jpeg")
                .quantity(1)
                .currencyId("ARS")
                .unitPrice(BigDecimal.valueOf(publicacion.getPrecioVenta()))
                .build();
        items.add(item);


        PreferenceRequest request = PreferenceRequest.builder().items(items).build();

        Preference preference = client.create(request);

        log.info("<< Link de MercadoPago generado: {}", preference.getId());
        return ResponseEntity.ok(preference);
    }
}
