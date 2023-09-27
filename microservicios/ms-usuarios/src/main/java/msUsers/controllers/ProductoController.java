package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Colecta;
import msUsers.domain.entities.Fundacion;
import msUsers.domain.entities.Producto;
import msUsers.domain.entities.enums.TipoProducto;
import msUsers.domain.model.EnumValue;
import msUsers.domain.repositories.ColectasRepository;
import msUsers.domain.repositories.ProductosRepository;
import msUsers.domain.responses.DTOs.ColectaDTO;
import msUsers.domain.responses.DTOs.ProductoDTO;
import msUsers.domain.responses.DTOs.TipoProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class ProductoController {

    @Autowired
    EntityManager entityManager;
    @Autowired
    ColectasRepository colectasRepository;
    private static final String json = "application/json";

    @GetMapping(path = "/tiposProductos", produces = json)
    public ResponseEntity<List<TipoProductoDTO>> getTiposProductos() {
        List<TipoProductoDTO> tiposProductos = Arrays.stream(TipoProducto.values()).
                map(tipoProducto -> new TipoProductoDTO(tipoProducto.name(),obtenerDescripcion(tipoProducto)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(tiposProductos);
    }

    @GetMapping(path = "/productos/{id_colecta}", produces = json)
    public ResponseEntity<List<ProductoDTO>> getProductosDeColecta(@PathVariable(name = "id_colecta") Long idColecta) {

        Colecta colecta = this.colectasRepository.findById(idColecta).
                orElseThrow(() -> new EntityNotFoundException("¡La colecta " + idColecta + " no existe!"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
        Root<Producto> from = query.from(Producto.class);

        Join<Producto, Colecta> join = from.join("colecta");
        Predicate predicate = cb.equal(join.get("idColecta"), idColecta);

        query.where(predicate);

        List<Producto> productos = entityManager.createQuery(query).getResultList();
        List<ProductoDTO> productoDTOs = productos.stream().map(producto -> producto.toDTO(false)).toList();

        return ResponseEntity.ok(productoDTOs);
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
