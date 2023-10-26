package msUsers.controllers;

import lombok.extern.slf4j.Slf4j;
import msUsers.domain.client.shipnow.ResponseOrder;
import msUsers.domain.client.shipnow.ResponseOrderDetails;
import msUsers.domain.entities.OrdenDeEnvio;
import msUsers.domain.entities.Usuario;
import msUsers.domain.logistica.PingPong;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.requests.logistica.PostOrderRequest;
import msUsers.domain.requests.logistica.PutOrderRequest;
import msUsers.domain.responses.logistica.resultResponse.ResultShippingOptions;
import msUsers.domain.responses.logisticaResponse.ResponseCostoEnvio;
import msUsers.domain.responses.logisticaResponse.ResponseOrdenDeEnvio;
import msUsers.services.LogisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<ResponseOrdenDeEnvio> crearOrden(
            @RequestBody PostOrderRequest postOrderRequest) throws Exception {
        final Usuario user = UsuarioContext.getUsuario();
        Long userId = user.getIdUsuario();
        if(!userId.equals(postOrderRequest.getUserIdOrigen())) {
            throw new Exception("El usuario no tiene permiso para enviar una orden con los datos enviados");
        }
        log.info(">> POST ORDER");
        ResponseOrdenDeEnvio response = logisticaService.generarOrden(postOrderRequest);
        log.info("<< ORDER CREADA CON ORDER ID {}", response.getOrderId());
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/orden", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ResponseOrdenDeEnvio>> obtenerOrdenesSegunUserId() {
        final Usuario user = UsuarioContext.getUsuario();
        String userId = String.valueOf(user.getIdUsuario());
        log.info(">> GET ORDER PARA EXTERNAL_REFERENCE: {}", userId);
        List<ResponseOrdenDeEnvio> order = logisticaService.obtenerOrden(userId);
        log.info("<< ORDENES OBTENIDAS PARA {} con la cantidad de {} ordenes", userId, order.size());
        return ResponseEntity.ok(order);
    }

    @GetMapping(path = "/orden/{orderId}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrdenDeEnvio> obtenerOrdenesSegunOrderId(
            @PathVariable(value = "orderId", required = true)  String orderId) throws Exception {
        log.info(">> GET ORDER PARA EXTERNAL_REFERENCE: {}", orderId);
        OrdenDeEnvio order = logisticaService.obtenerDetallesDeOrdenXOrdenId(Long.valueOf(orderId));
        log.info("<< ORDENES OBTENIDAS PARA {} con detalle {}", orderId, order);
        return ResponseEntity.ok(order);
    }

    @PutMapping(path = "/orden/{orderId}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> actualizarOrdenDeCompra(
            @PathVariable(value = "orderId", required = true)  String orderId,
            @RequestBody PutOrderRequest putOrderRequest)  {
        log.info(">> PUT ORDER  {} PARA ACTUALIZAR A NUEVO ESTADO: {}", orderId, putOrderRequest.getNuevoEstado());
        logisticaService.actualizarEstadoDeOrdenXOrdenId(orderId, putOrderRequest);
        log.info("<< ORDEN {} ACTUALIZADA CON NUEVO ESTATUS PARA {}", orderId, putOrderRequest.getNuevoEstado());
        return ResponseEntity.ok("OK");
    }

    @GetMapping(path = "/costoEnvio", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseCostoEnvio> obtenerPrecioDeShipping(
            @RequestParam("peso") Long peso,
            @RequestParam("codigoPostal") String codigoPostal
    ) {
        log.info(">> GET Costo de envio");
        ResultShippingOptions response = logisticaService.getCostoEnvio(peso, codigoPostal, "ship_pap,ship_pas");
        ResponseCostoEnvio costoEnvio = ResponseCostoEnvio.builder()
                .fechaMaximaEnvio(response.getMaximum_delivery())
                .precio(response.getPrice())
                .build();
        log.info("<< Obteniendo ShippingOptions con respuesta {}", response.toString());
        return ResponseEntity.ok(costoEnvio);
    }

    @GetMapping(path = "/ping", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PingPong> ping(
    )  {
        log.info(">> PING");
        PingPong response = logisticaService.pingpong();
        log.info("<< PONG");
        return ResponseEntity.ok(response);
    }
}
