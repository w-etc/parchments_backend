package parchments_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import parchments_backend.domain.Parchment;
import parchments_backend.repositories.ParchmentRepository;

@SpringBootApplication
public class ParchmentsBackendApplication {

	@Autowired
	private ParchmentRepository parchmentRepository;

	public static void main(String[] args) {
		SpringApplication.run(ParchmentsBackendApplication.class, args);
	}

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedParchmentsTable();
	}

	private void seedParchmentsTable() {
		Parchment chapterI = new Parchment("Chapter I", "");
		Parchment chapterII = new Parchment("Chapter II", "");
		Parchment chapterIII = new Parchment("Chapter III", "");

		chapterIII.tieTo(chapterII);
		chapterII.tieTo(chapterI);

		parchmentRepository.save(chapterI);
		parchmentRepository.save(chapterII);
		parchmentRepository.save(chapterIII);
	}
}
