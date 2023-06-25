package msUsers.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"msUsers.controllers, msUsers.domain.repositories"})
@EntityScan(basePackages = "msUsers.domain")
@EnableJpaRepositories("msUsers.domain.repositories")
public class EcoSwapApplication {
	public static void main(String[] args) {

		SpringApplication.run(EcoSwapApplication.class, args);
	}

}
