package msTransacciones.controllers;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.extern.slf4j.Slf4j;
import msTransacciones.domain.logistica.PingPong;
import msTransacciones.domain.requests.logistica.PostOrderRequest;
import msTransacciones.domain.responses.logistica.resultResponse.ResultShippingOptions;
import msTransacciones.services.LogisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Slf4j
@Validated
@RequestMapping("/ms-transacciones/api/logistica")
public class LogisticaController {

    @Autowired
    private LogisticaService logisticaService;
    private static final String json = "application/json";

    /*
@PostMapping(path = "/orden", consumes = json, produces = json)
@ResponseStatus(HttpStatus.OK)
@Transactional
public ResponseEntity<String> crearOrden(@RequestBody PostOrderRequest postOrderRequest) throws MPException, MPApiException {
    log.info(">> POST ORDER");
    logisticaService.generarOrden(postOrderRequest);
    return ResponseEntity.ok("");
}

@GetMapping(path = "/orden", consumes = json, produces = json)
@ResponseStatus(HttpStatus.OK)
public ResponseEntity<String> obtenerOrdenesSegunUserId(@PathVariable("ordenId") Long ordenId) throws MPException, MPApiException {
    return ResponseEntity.ok("");
}

@GetMapping(path = "/orden/{ordenId}", consumes = json, produces = json)
@ResponseStatus(HttpStatus.OK)
public ResponseEntity<String> obtenerDetalleOrdenSegunOrdenId(@PathVariable("ordenId") Long ordenId) throws MPException, MPApiException {
    return ResponseEntity.ok("");
}

@PutMapping(path = "/orden/{ordenId}", consumes = json, produces = json)
@ResponseStatus(HttpStatus.OK)
public ResponseEntity<String> actualizarOrden(@PathVariable("ordenId") Long ordenId) throws MPException, MPApiException {
    return ResponseEntity.ok("");
}


@GetMapping(path = "/shipping_options", consumes = json, produces = json)
@ResponseStatus(HttpStatus.OK)
public ResponseEntity<ResultShippingOptions> obtenerPrecioDeShipping(
        @RequestParam("weight") Long weight,
        @RequestParam("to_zip_code") String zipCode,
        @RequestParam("types") String types
) throws MPException, MPApiException {
    log.info(">> GET Shipping Options");
    ResultShippingOptions response = logisticaService.getShippingOption(weight, zipCode, types);
    log.info(("<< Obteniendo ShippingOptions"));
    return ResponseEntity.ok(response);
}

    @GetMapping(path = "/store", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> obtenerStoresDeUsuario(@PathVariable("ordenId") Long ordenId) throws MPException, MPApiException {
        return ResponseEntity.ok("");
    }

    @GetMapping(path = "/store/{storeId}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> obtenerDetalleDeStorePorId(@PathVariable("storeId") Long storeId) throws MPException, MPApiException {
        return ResponseEntity.ok("");
    }

    @PostMapping(path = "/store", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> crearStore(@PathVariable("ordenId") Long ordenId) throws MPException, MPApiException {
        return ResponseEntity.ok("");
    }

    @PostMapping(path = "/webhook", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> crearWebhook(@PathVariable("ordenId") Long ordenId) throws MPException, MPApiException {
        return ResponseEntity.ok("");
    }
*/
    @GetMapping(path = "/ping", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PingPong> ping() throws MPException, MPApiException {
        log.info(">> PING");
        PingPong response = logisticaService.pingpong();
        log.info("<< PONG");
        return ResponseEntity.ok(response);
    }
}
