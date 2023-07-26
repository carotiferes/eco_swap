package msUsers.controllers;

import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.enums.TipoProducto;
import msUsers.domain.model.EnumValue;
import msUsers.domain.responses.DTOs.TipoProductoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class ProductoController {

    private static final String json = "application/json";

    @GetMapping(path = "/tiposProductos", produces = json)
    public ResponseEntity<List<TipoProductoDTO>> getTiposProductos() {
        List<TipoProductoDTO> tiposProductos = Arrays.stream(TipoProducto.values()).
                map(tipoProducto -> new TipoProductoDTO(tipoProducto.name(),obtenerDescripcion(tipoProducto)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(tiposProductos);
    }

    private String obtenerDescripcion(TipoProducto tipoProducto) {
        try {
            EnumValue annotation = tipoProducto.getClass()
                    .getField(tipoProducto.name())
                    .getAnnotation(EnumValue.class);
            return annotation != null ? annotation.description() : "";
        } catch (NoSuchFieldException e) {
            return "";
        }
    }
}
