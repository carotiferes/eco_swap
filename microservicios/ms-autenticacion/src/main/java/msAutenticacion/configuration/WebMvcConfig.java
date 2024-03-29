package msAutenticacion.configuration;

import msAutenticacion.services.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${url.host.domain}")
    private String urlHost;

    @Value("${url.host.subdomain}")
    private String urlHost2;

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(urlHost, urlHost2)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Agrega aquí los métodos permitidos
                .allowedHeaders("Content-Type", "Authorization", "Access-Control-Allow-Origin") // Agrega aquí los encabezados permitidos en tus solicitudes
                .allowCredentials(true)
                .maxAge(1800);
    }
};
