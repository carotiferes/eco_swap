package msUsers.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AllowedUrlsProvider {

    public List<String> getAllowedUrls() {
        return Arrays.asList(
                // IMAGENES
                "/api/getImage/*.jpeg",
                "/api/getImage/*.jpg",
                "/api/getImage/*.png",
                "/api/getImage/*.webp",
                // SWAGGER
                "/swagger-ui/*",
                "/v3/api-docs",
                "/v3/api-docs/*",
                // DONACIONES
                "/api/colectas",
                "/fundaciones",
                "/api/tiposProductos",
                "/api/colecta/*",
                "/fundacion/*",
                // TRUEQUES / VENTAS
                "/api/publicaciones",
                "/api/publicacion/*",
                // PRODUCTOS
                "/api/productos/*",
                // COMPRAS
                "/comprar/*",
                "/ms-users/api/webhook");
    }
}
