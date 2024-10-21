package ir.multithread.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("ir.multithread.springboot.persistence.repo")
@EntityScan("ir.multithread.springboot.persistence.model")
public class MultiThreadApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiThreadApplication.class, args);
	}

}
