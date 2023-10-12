package msTransacciones.controllers;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.extern.slf4j.Slf4j;
import msTransacciones.domain.client.shipnow.ResponseOrder;
import msTransacciones.domain.client.shipnow.ResponseOrderDetails;
import msTransacciones.domain.client.shipnow.response.ResponseGetListOrders;
import msTransacciones.domain.logistica.PingPong;
import msTransacciones.domain.requests.logistica.PostOrderRequest;
import msTransacciones.domain.responses.logistica.resultResponse.ResultShippingOptions;
import msTransacciones.services.LogisticaService;
import org.apache.velocity.app.event.implement.EscapeXmlReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
@Slf4j
@Validated
@RequestMapping("/ms-transacciones/api/logistica")
public class LogisticaController {

    @Autowired
    private LogisticaService logisticaService;
    private static final String json = "application/json";


@PostMapping(path = "/orden", consumes = json, produces = json)
@ResponseStatus(HttpStatus.OK)
@Transactional
public ResponseEntity<String> crearOrden(@RequestBody PostOrderRequest postOrderRequest) throws MPException, MPApiException {
    log.info(">> POST ORDER");
    logisticaService.generarOrden(postOrderRequest);
    log.info("<< ORDER CREADA");
    return ResponseEntity.ok("OK");
}


@GetMapping(path = "/orden", consumes = json, produces = json)
@ResponseStatus(HttpStatus.OK)
public ResponseEntity<ArrayList<ResponseOrder>> obtenerOrdenesSegunUserId(
        @RequestParam(value = "external_reference", required = true)  String external_reference) throws MPException, MPApiException {
    log.info(">> GET ORDER PARA EXTERNAL_REFERENCE: {}", external_reference);
    ArrayList<ResponseOrder> order = logisticaService.obtenerOrden(external_reference);
    log.info("<< ORDENES OBTENIDAS PARA {} con la cantidad de {} ordenes", external_reference, order.size());
    return ResponseEntity.ok(order);
}

    @GetMapping(path = "/orden/{orderId}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseOrderDetails> obtenerOrdenesSegunOrderId(
            @PathVariable(value = "orderId", required = true)  String orderId) throws MPException, MPApiException {
        log.info(">> GET ORDER PARA EXTERNAL_REFERENCE: {}", orderId);
        ResponseOrderDetails order = logisticaService.obtenerOrdenPorId(orderId);
        log.info("<< ORDENES OBTENIDAS PARA {} con detalle {}", orderId, order);
        return ResponseEntity.ok(order);
    }
    /*
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
    @GetMapping(path = "/shipping_options", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResultShippingOptions> obtenerPrecioDeShipping(
            @RequestParam("weight") Long weight,
            @RequestParam("to_zip_code") String zipCode,
            @RequestParam("types") String types
    ) throws MPException, MPApiException {
        log.info(">> GET Shipping Options");
        ResultShippingOptions response = logisticaService.getShippingOption(weight, zipCode, types);
        log.info("<< Obteniendo ShippingOptions con respuesta {}", response.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/ping", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PingPong> ping() throws MPException, MPApiException {
        log.info(">> PING");
        PingPong response = logisticaService.pingpong();
        log.info("<< PONG");
        return ResponseEntity.ok(response);
    }
}
