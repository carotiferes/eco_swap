package msUsers.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = {"msUsers.controllers",
		"msUsers.domain.repositories",
		"msUsers.exceptions.handler",
		"msUsers.services",
		"msUsers.configuration",
		"msUsers.components",
		"msUsers.interceptors"
})
@EntityScan(basePackages = "msUsers.domain.entities")
@EnableJpaRepositories("msUsers.domain.repositories")
public class EcoSwapApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcoSwapApplication.class, args);
	}
}
