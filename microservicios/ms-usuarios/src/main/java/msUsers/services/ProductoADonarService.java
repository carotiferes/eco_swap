package msUsers.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Producto;
import msUsers.domain.entities.ProductosADonarDeOrden;
import msUsers.domain.repositories.ProductosRepository;
import msUsers.domain.responses.DTOs.ProductosADonarDeOrdenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductoADonarService {

    // Solo va ser para construir los DTOs
    @Autowired
    private ProductosRepository productosRepository;

    public ProductosADonarDeOrdenDTO obtenerProductoDTO(ProductosADonarDeOrden productoADonar) {
        Producto prod = this.productosRepository.findById(productoADonar.getIdProducto())
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el producto: " + productoADonar.getIdProducto()));

        ProductosADonarDeOrdenDTO productosADonarDeOrdenDTO = new ProductosADonarDeOrdenDTO();
        productosADonarDeOrdenDTO.setIdProductoADonar(productoADonar.getIdProductoADonar());
        productosADonarDeOrdenDTO.setIdOrden(productoADonar.getOrdenDeEnvio().getIdOrden());
        productosADonarDeOrdenDTO.setProducto(prod.toDTO(false));
        productosADonarDeOrdenDTO.setIdDonacion(productoADonar.getIdDonacion());
        productosADonarDeOrdenDTO.setCantidad(productoADonar.getCantidad());
        return productosADonarDeOrdenDTO;

    }
}
