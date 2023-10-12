package msTransacciones.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import msTransacciones.domain.client.shipnow.RequestVariant;
import msTransacciones.domain.client.shipnow.ResponseOrder;
import msTransacciones.domain.client.shipnow.ResponseOrderDetails;
import msTransacciones.domain.client.shipnow.response.ResponseGetListOrders;
import msTransacciones.domain.client.shipnow.response.ResponseGetListVariant;
import msTransacciones.domain.client.shipnow.response.ResponseOrders;
import msTransacciones.domain.client.shipnow.response.ResponseVariant;
import msTransacciones.domain.logistica.Item;
import msTransacciones.domain.logistica.Order;
import msTransacciones.domain.logistica.PingPong;
import msTransacciones.domain.logistica.ShipDirection;
import msTransacciones.domain.logistica.enums.OrderStatusEnum;
import msTransacciones.domain.requests.logistica.PostOrderRequest;
import msTransacciones.domain.responses.logistica.ResponseShippingOptions;
import msTransacciones.domain.responses.logistica.resultResponse.ResultShippingOptions;
import org.apache.coyote.Response;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LogisticaService {

    @Value("${logistica.url}")
    private String serviceUrl;

    @Value("${logistica.auth}")
    private String tokenAuth;

    public PingPong pingpong() {
        HttpsURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(serviceUrl+"/ping");
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString = "{}";
            int responseCode = con.getResponseCode();
            log.info("-- SEND GET PING TO URL: {}", serviceUrl);
            log.info("-- RESPONSE CODE: {}", responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            log.info("-- RESPONSE BODY: {}", response.toString());
            in.close();

            JsonObject convertedObject = new Gson().fromJson(response.toString(), JsonObject.class);

           return PingPong.builder()
                   .cache(String.valueOf(convertedObject.get("cache").getAsString()))
                   .db(String.valueOf(convertedObject.get("db").getAsString()))
                   .build();
        } catch (Exception e) {
            e.printStackTrace();
            return PingPong.builder()
                    .cache("fail")
                    .db("fail")
                    .build();
        }
    }
/*
    private ShipDirection createShippingDirection() {
        return ShipDirection.builder()
                .email()
                .floor()
                .city()
                .doc_number()
                .country()
                .zip_code()
                .full_name()
                .last_name()
                .state()
                .street_name()
                .street_name()
                .phone()
                .build();
    }

 */

    public ArrayList<ResponseVariant> obtenerItem(String nombreItemReferente) {
        log.info(">> GET NEW ITEM DESDE SHIPNOW BUSCANDO CON EL NOMBRE: {}", nombreItemReferente);
        try {
            //Create connection
            URL url = new URL(serviceUrl+"/variants?external_reference="+nombreItemReferente);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString = "{}";
            int responseCode = con.getResponseCode();
        //    log.info("-- SEND GET PING TO URL: {}", serviceUrl);
        //    log.info("-- RESPONSE CODE: {}", responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            log.info("<< RESPONSE ITEMS: {}", response.toString());
            in.close();
            return new Gson().fromJson(response.toString(), ResponseGetListVariant.class).getResults();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseVariant crearItemParaShipnow(RequestVariant variant) {
        log.info(">> CREATE NEW ITEM PARA SHIPNOW: {}", serviceUrl);
        try {
            //Create connection

            URL url = new URL(serviceUrl+"/variants");
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String gsonVariant = new Gson().toJson(variant);
            try (DataOutputStream dos = new DataOutputStream(con.getOutputStream())) {
                dos.writeBytes(gsonVariant);
            }

            int responseCode = con.getResponseCode();
     //       log.info("-- SEND CREATE PING TO URL: {}", serviceUrl);
     //       log.info("-- RESPONSE CODE: {}", responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            log.info("<< RESPONSE ITEMS: {}", response.toString());
            in.close();
            return new Gson().fromJson(response.toString(), ResponseVariant.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<ResponseVariant> obtenerVariantes(ArrayList<Item> listItems) {

        Item obtenerItem = listItems.get(0);
        log.info(">> Buscar ITEM con nombre: {}", obtenerItem.getExternal_reference());
        ArrayList<ResponseVariant> itemBuscado = this.obtenerItem(obtenerItem.getExternal_reference());
        ResponseVariant buscado;
        if(itemBuscado.isEmpty()) {
            buscado = this.crearItemParaShipnow(
                    RequestVariant.builder()
                    .title(obtenerItem.getTitle())
                    .price(obtenerItem.getPrice())
                    .stock(obtenerItem.getQuantity())
                    .dimensions(obtenerItem.getDimensions())
                    .currency("ARS")
                    .image_url(obtenerItem.getImage_url())
                    .external_reference(obtenerItem.getExternal_reference())
                    .build());
        } else {
            buscado = itemBuscado.get(0);
        }
        log.info("<< Retornar ITEM encontrado: {}", obtenerItem.getExternal_reference());
        ArrayList<ResponseVariant> list = new ArrayList<>();
        list.add( buscado );
        return list;
    }

    private ArrayList<Item> crearItemAPartirDeResponseVariant(ArrayList<ResponseVariant> lista) {
        return (ArrayList<Item>) lista.stream()
                .map(this::crearItemPorResponseVariant)
                .collect(Collectors.toList());
    }

    private Item crearItemPorResponseVariant(ResponseVariant variant) {
        return Item.builder()
                .id(variant.getId())
                .archived(variant.getArchived())
                .cross_docking(true)
                .dimensions(variant.getDimensions())
           //     .price(variant.getPrice())
                .quantity(variant.getStock().getAvailable())
           //     .stock(variant.getStock())
                .quantity(5)
                .image_url(variant.getImage_url())
                .title(variant.getTitle())
                .status(OrderStatusEnum.AWAITING_PAYMENT.name())
                .build();
    }

    public ArrayList<ResponseOrder>  obtenerOrden(String external_reference) {
        log.info(">> OBTENER ORDENES DE: {}", external_reference);

        try {
            //Create connection
            URL url = new URL(serviceUrl+"/orders?external_reference="+external_reference);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();
            log.info("-- SEND GET ORDENES DE {} TO URL: {}", external_reference, serviceUrl+"/orders?external_reference="+external_reference);
            log.info("-- RESPONSE CODE: {}", responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            ResponseGetListOrders obtenidos = new Gson().fromJson(response.toString(), ResponseGetListOrders.class);
            ArrayList<Order> ordenesADevolver = obtenidos.getResults();
            return (ArrayList<ResponseOrder>) ordenesADevolver.stream()
                    .map(this::buildOrder)
                    .collect(Collectors.toList());
/*
            return PingPong.builder()
                    .cache(String.valueOf(convertedObject.get("cache").getAsString()))
                    .db(String.valueOf(convertedObject.get("db").getAsString()))
                    .build();
 */
        } catch (Exception e) {
            e.printStackTrace();
            return null;
/*
            return PingPong.builder()
                    .cache("fail")
                    .db("fail")
                    .build();


 */
        }
    }

    private ResponseOrder buildOrder(Order order) {
        return ResponseOrder.builder()
                .timestamps(order.getTimestamps())
                .items(order.getItems())
                .comment(order.getComment())
                .id(order.getId())
                .status(order.getStatus()==null?null:order.getStatus().name)
                .last_updated_date(order.getTimestamps().getUpdated_at())
                .build();
    }

    public ResponseOrderDetails obtenerOrdenPorId(String ordenId) {
        log.info(">> OBTENER ORDENES DE: {}", ordenId);

        try {
            //Create connection
            URL url = new URL(serviceUrl+"/orders/"+ordenId);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();
            log.info("-- SEND GET ORDEN X ID DE {} TO URL: {}", ordenId, serviceUrl+"/orders/"+ordenId);
            log.info("-- RESPONSE CODE: {}", responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Order orderEspecifica = new Gson().fromJson(response.toString(), Order.class);
            log.info("-- RESPONSE BODY: {}", orderEspecifica);
       //     Order orderEspecifica = obtenidos;

            return ResponseOrderDetails.builder()
                    .store(orderEspecifica.getStore())
                    .created_at(orderEspecifica.getCreated_at())
                    .timestamps(orderEspecifica.getTimestamps())
                    .items(orderEspecifica.getItems())
                    .comment(orderEspecifica.getComment())
                    .shippingOption(orderEspecifica.getShipping_option())
                    .id(orderEspecifica.getId())
                    .status(orderEspecifica.getStatus()==null?null:orderEspecifica.getStatus().name)
                    .ship_to(orderEspecifica.getShip_to())
                    .ship_from(orderEspecifica.getShip_from())
                    .last_updated_date(orderEspecifica.getTimestamps().getUpdated_at())
                    .build();
/*
            return PingPong.builder()
                    .cache(String.valueOf(convertedObject.get("cache").getAsString()))
                    .db(String.valueOf(convertedObject.get("db").getAsString()))
                    .build();
 */
        } catch (Exception e) {
            e.printStackTrace();
            return null;
/*
            return PingPong.builder()
                    .cache("fail")
                    .db("fail")
                    .build();


 */
        }
    }

    public void generarOrden(PostOrderRequest postOrderRequest) {
        log.info(">> GENERAR ORDEN");
        //PUEDE PASAR A SER UN LISTADO TRANQUILAMENTE
        ArrayList<ResponseVariant> itemBuscado = this.obtenerVariantes(postOrderRequest.getItems());

        HttpsURLConnection connection = null;
        Order orderACrear = Order.builder()
                .archived(false)
                //PARA ESTO HABRIA QUE GENERAR UN ID UNICO
                .external_reference("asf")
                .ship_to(postOrderRequest.getShip_to())
                .ship_from(postOrderRequest.getShip_from())
                .items(this.crearItemAPartirDeResponseVariant(itemBuscado))
                //ESTO ES EL USUARIO PERO DE SHIPNOW
              //  .external_reference_user(postOrderRequest.getExternal_reference_user())
                .comment(postOrderRequest.getComment())
             //   .store(postOrderRequest.getStore_id())
                .status(OrderStatusEnum.AWAITING_SHIPMENT)
                .shipping_option(postOrderRequest.getShipping_option())
                .build();
/*
        try {
            //Create connection
            URL url = new URL(serviceUrl+"/order");
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String gsonVariant = new Gson().toJson(orderACrear);
            try (DataOutputStream dos = new DataOutputStream(con.getOutputStream())) {
                dos.writeBytes(gsonVariant);
            }

            int responseCode = con.getResponseCode();
            log.info("-- SEND GET PING TO URL: {}", serviceUrl);
            log.info("-- RESPONSE CODE: {}", responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            log.info("-- RESPONSE BODY: {}", response.toString());
            in.close();

            JsonObject convertedObject = new Gson().fromJson(response.toString(), JsonObject.class);

            return PingPong.builder()
                    .cache(String.valueOf(convertedObject.get("cache").getAsString()))
                    .db(String.valueOf(convertedObject.get("db").getAsString()))
                    .build();


        } catch (Exception e) {
            e.printStackTrace();
            /*
            return PingPong.builder()
                    .cache("fail")
                    .db("fail")
                    .build();


        }

         */
    }

    public ResultShippingOptions getShippingOption(Long weight, String zipCode, String types) {
        HttpsURLConnection connection = null;
        try {
            //Create connection
            String uriParameters = "weight="+ weight.toString() + "&to_zip_code="+zipCode+"&types="+types;
            String urlString = serviceUrl+"/shipping_options?"+uriParameters;
            URL url = new URL(urlString);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            log.info("-- SEND GET PING TO URL: {}", serviceUrl);
            log.info("-- RESPONSE CODE: {}", responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            log.info("-- RESPONSE BODY: {}", response.toString());

            in.close();

            ResponseShippingOptions result = new Gson().fromJson(response.toString(), ResponseShippingOptions.class);
            log.info("-- RESPONSE BODY: {}", result.toString());
            return result.getResults().get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
