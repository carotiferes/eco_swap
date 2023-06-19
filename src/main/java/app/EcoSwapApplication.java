package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "domain")
public class EcoSwapApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcoSwapApplication.class, args);
	}

}
