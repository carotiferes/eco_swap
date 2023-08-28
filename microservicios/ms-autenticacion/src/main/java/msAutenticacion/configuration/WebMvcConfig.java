package msAutenticacion.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${url.host}")
    private String urlHost;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(urlHost)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Agrega aquí los métodos permitidos
                .allowedHeaders("Content-Type", "Authorization", "Access-Control-Allow-Origin") // Agrega aquí los encabezados permitidos en tus solicitudes
                .allowCredentials(true)
                .maxAge(1800);
    }
};
