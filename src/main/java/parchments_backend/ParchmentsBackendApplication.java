package parchments_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import parchments_backend.domain.Parchment;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.ParchmentRepository;
import parchments_backend.repositories.WriterRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class ParchmentsBackendApplication {

	private Writer firstWriter;

	@Autowired
	private WriterRepository writerRepository;
	@Autowired
	private ParchmentRepository parchmentRepository;

	public static void main(String[] args) {
		SpringApplication.run(ParchmentsBackendApplication.class, args);
	}

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedWritersTable();
		seedParchmentsTable();
	}

	private void seedWritersTable() {
		List<Writer> presentWriters = writerRepository.findAll();
		if (presentWriters.size() >= 1) {
			return;
		}

		firstWriter = new Writer("First", "");

		writerRepository.save(firstWriter);
	}

	private void seedParchmentsTable() {
		List<Parchment> presentParchments = parchmentRepository.findAll();
		if (presentParchments.size() >= 3) {
			return;
		}

		Parchment chapterI = new Parchment("Chapter I", "");
		Parchment chapterII = new Parchment("Chapter II", "");
		Parchment chapterIII = new Parchment("Chapter III", "");

		chapterI.setContinuations(Collections.singletonList(chapterII));
		chapterII.setContinuations(Collections.singletonList(chapterIII));

		firstWriter.setParchments(Arrays.asList(chapterI, chapterII));
		writerRepository.save(firstWriter);

		parchmentRepository.save(chapterI);
		parchmentRepository.save(chapterII);
		parchmentRepository.save(chapterIII);
	}
}
