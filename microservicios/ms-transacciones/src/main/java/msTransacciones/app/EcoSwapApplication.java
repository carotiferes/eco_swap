package msTransacciones.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"msTransacciones.controllers",
		"msTransacciones.domain.repositories",
		"msTransacciones.exceptions.handler",
		"msTransacciones.services",
		"msTransacciones.configuration"
})
@EntityScan(basePackages = "msTransacciones.domain.entities")
@EnableJpaRepositories("msTransacciones.domain.repositories")
//@ComponentScan("com.mercadopago")
public class EcoSwapApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcoSwapApplication.class, args);
	}
}
