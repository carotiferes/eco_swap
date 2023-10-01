package msTransacciones.configuration;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfiguration {
    @Value("${MP.ACCESS.TOKEN}")
    private String MPAccessToken;
    @PostConstruct
    public void configure(){
        MercadoPagoConfig.setAccessToken(MPAccessToken);
    }
}
