package parchments_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories("parchments_backend.repositories")
@EntityScan("parchments_backend.domain")
public class ParchmentsBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(ParchmentsBackendApplication.class, args);
	}
}

