package msAutenticacion.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AllowedUrlsProvider {

    public List<String> getAllowedUrls() {
        return Arrays.asList(
                // SWAGGER
                "/swagger-ui/*",
                "/v3/api-docs",
                "/v3/api-docs/*",
                "/ms-autenticacion/api/v1/usuario/login",
                "/ms-autenticacion/api/v1/tiposDocumentos",
                "/ms-autenticacion/api/v1/usuario/signup"
        );
    }
}
