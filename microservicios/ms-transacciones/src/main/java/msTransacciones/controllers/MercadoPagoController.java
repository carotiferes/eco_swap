package msTransacciones.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msTransacciones.domain.entities.Compra;
import msTransacciones.domain.entities.Particular;
import msTransacciones.domain.entities.Usuario;
import msTransacciones.domain.entities.enums.EstadoCompra;
import msTransacciones.domain.entities.enums.EstadoPublicacion;
import msTransacciones.domain.model.UsuarioContext;
import msTransacciones.domain.repositories.ComprasRepository;
import msTransacciones.domain.repositories.PublicacionesRepository;
import msTransacciones.domain.requests.RequestMercadoPagoWebhook;
import msTransacciones.domain.responses.ResponseWebhook;
import msTransacciones.services.CriteriaBuilderQueries;
import msTransacciones.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private ImageService imageService;

    private static String globalAccessToken;

    private static final String json = "application/json";
    @PostMapping(path = "/comprar/{id_publicacion}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Preference> comprarProducto(@PathVariable("id_publicacion") Long idPublicacion) throws MPException, MPApiException {

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(user.getIdUsuario());
        Particular particular = optionalParticular.orElseThrow(() -> new EntityNotFoundException("¡El particular no existe!"));

        final var publicacion = this.publicacionesRepository.findById(idPublicacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el producto: " + idPublicacion));

        globalAccessToken = publicacion.getParticular().getAccessToken();

        log.info(">> Comienza la compra del producto: {}", idPublicacion);

        // Creamos la compra
        if(publicacion.getCompras().stream().noneMatch(compra -> compra.getEstadoCompra() == EstadoCompra.APROBADA)) {

            Compra compra = new Compra();
            compra.setPublicacion(publicacion);
            compra.setParticularComprador(particular);
            compra.setEstadoCompra(EstadoCompra.PENDIENTE);
            var entity = this.comprasRepository.save(compra);

            // MP
            MercadoPagoConfig.setAccessToken(globalAccessToken);
            PreferenceClient client = new PreferenceClient();
            List<PreferenceItemRequest> items = new ArrayList<>();
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(publicacion.getTitulo().trim() + " " + publicacion.getDescripcion().trim())
                    .description(publicacion.getDescripcion().trim())
                    .pictureUrl(imageService.getImage(publicacion.getImagenes().split("\\|")[0]))
                    .quantity(1)
                    .currencyId("ARS")
                    .unitPrice(BigDecimal.valueOf(publicacion.getPrecioVenta()))
                    .build();
            items.add(item);


            PreferenceRequest request = PreferenceRequest.builder()
                    .items(items)
                    .externalReference(String.valueOf(entity.getIdCompra()))
                    .notificationUrl("https://1d7e-190-194-141-45.ngrok.io/ms-transacciones/api/webhook") // Esto cambia cada vez que use ngrok
                    .build();

            Preference preference = client.create(request);

            compra.setIdPreferenceMercadoPago(preference.getId());
            this.comprasRepository.save(compra);

            log.info("<< Link de MercadoPago generado: {}", preference.getId());
            return ResponseEntity.ok(preference);
        }else
            throw new EntityExistsException("La publicación ya fue comprada");
    }

    @PostMapping(path = "/webhook", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ResponseWebhook> getWebhook(@RequestBody RequestMercadoPagoWebhook requestMercadoPagoWebhook) {

        log.info(">> Request de la transacción: {}", requestMercadoPagoWebhook);

        if(requestMercadoPagoWebhook.getId() != null){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mercadopago.com/v1/payments/" + requestMercadoPagoWebhook.getData().getId()))
                    .GET()
                    .setHeader("Authorization", "Bearer " + globalAccessToken)
                    .build();

            try{
                log.info(">> Se consulta a la API de MercadoPago con el ID Payment: {}", requestMercadoPagoWebhook.getData().getId());
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.body());
                log.info("Respuesta MercadoPago: {}", response.statusCode());

                Long idCompra = jsonNode.get("external_reference").asLong();
                String estadoCompra = jsonNode.get("status").asText();
                final var compra = this.comprasRepository.findById(idCompra).
                        orElseThrow(() -> new EntityNotFoundException("No fue encontrada la compra: " + idCompra));

                log.info(">> Compra con estado: {}", estadoCompra);
                if(estadoCompra.equals("approved")) {
                    compra.setEstadoCompra(EstadoCompra.APROBADA);
                    compra.setIdPaymentMercadoPago(requestMercadoPagoWebhook.getData().getId().toString());
                    compra.setDateApproved(ZonedDateTime.parse(jsonNode.get("date_approved").asText()));
                    this.comprasRepository.save(compra);
                    log.info("<< Compra actualizada correctamente en la base de datos.");

                    final var publicacion = this.publicacionesRepository.findById(compra.getPublicacion().getIdPublicacion()).
                            orElseThrow(() -> new EntityNotFoundException("No fue encontrada la publicación: " + compra.getPublicacion().getIdPublicacion()));

                    publicacion.setEstadoPublicacion(EstadoPublicacion.CERRADA);
                    this.publicacionesRepository.save(publicacion);
                    log.info("Publicación {} cerrada.", publicacion.getIdPublicacion());
                }
                else if (estadoCompra.equals("rejected")) {
                    log.info("Compra rechazada.");
                    compra.setEstadoCompra(EstadoCompra.RECHAZADA);
                }
                else{
                    log.info("Compra todavía pendiente en MercadoPago. Es probable que el usuario no haya concluido la transacción.");
                    compra.setEstadoCompra(EstadoCompra.PENDIENTE);
                }

                ResponseWebhook responseWebhook = new ResponseWebhook();
                responseWebhook.setStatus(HttpStatus.OK.name());
                responseWebhook.setIdPayment(requestMercadoPagoWebhook.getData().getId());
                responseWebhook.setDescripcion("Compra " + estadoCompra);
                return ResponseEntity.ok(responseWebhook);

            }catch (IOException | InterruptedException e) {
                log.error("Error en la consulta a la API de MP con el ID Payment {}: {}", requestMercadoPagoWebhook.getData().getId(), e.getMessage());
                return ResponseEntity.internalServerError().build();
            }
        }
        else{
            log.warn("<< Request de Mercadopago sin datos/datos inválidos.");
            return ResponseEntity.badRequest().build();
        }
    }
}
