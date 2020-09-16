package parchments_backend.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import parchments_backend.domain.Writer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataNeo4jTest
public class WriterRepositoryTest {

    @Autowired
    private WriterRepository writerRepository;

    @Test
    void the_repository_starts_empty() {
        List<Writer> parchments = writerRepository.findAll();
        assertThat(parchments.size()).isEqualTo(0);
    }

    @Test
    void a_writer_can_be_retrieved_by_username() {
        Writer writer = writerRepository.save(new Writer("username", "password"));
        Writer retrievedWriter = writerRepository.findByUsername(writer.getUsername());

        assertThat(writer.getId()).isEqualTo(retrievedWriter.getId());
    }

    @Test
    void all_writers_can_be_retrieved() {
        Writer firstWriter = writerRepository.save(new Writer("username", "password"));
        Writer secondWriter = writerRepository.save(new Writer("otherUsername", "password"));
        List<Writer> allWriters = writerRepository.findAll();

        assertThat(allWriters.size()).isEqualTo(2);
        assertTrue(allWriters.stream().anyMatch(writer -> writer.getId().equals(firstWriter.getId())));
        assertTrue(allWriters.stream().anyMatch(writer -> writer.getId().equals(secondWriter.getId())));
    }
}
