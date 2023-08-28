package msAutenticacion.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = {"msAutenticacion.controllers",
		"msAutenticacion.domain.repositories",
		"msAutenticacion.exceptions.handler",
		"msAutenticacion.services",
		"msAutenticacion.configuration"
})
@EntityScan(basePackages = "msAutenticacion.domain.entities")
@EnableJpaRepositories("msAutenticacion.domain.repositories")
public class EcoSwapApplication {
	public static void main(String[] args) {

		SpringApplication.run(EcoSwapApplication.class, args);
	}
}
