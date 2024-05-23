package tight.commas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
public class CommasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommasApplication.class, args);
	}

}
