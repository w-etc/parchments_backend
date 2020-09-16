package parchments_backend.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import parchments_backend.domain.Parchment;
import parchments_backend.domain.Writer;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
public class ParchmentRepositoryTest {

    @Autowired
    private ParchmentRepository parchmentRepository;

    @Autowired
    private WriterRepository writerRepository;

    private Writer writer;

    @BeforeEach
    void setUp() {
        String user = "user";
        writer = writerRepository.save(new Writer(user, "password"));
    }

    @Test
    void the_repository_starts_empty() {
        List<Parchment> parchments = parchmentRepository.findAll();
        assertThat(parchments.size()).isEqualTo(0);
    }

    @Test
    void a_parchment_is_saved_to_the_repository() {
        Parchment parchment = saveParchment("A new parchment's title", writer, null);

        List<Parchment> parchments = parchmentRepository.findAllByWriterId(writer.getId());
        assertThat(parchments.size()).isEqualTo(1);
        Parchment retrievedParchment = parchments.get(0);
        assertThat(retrievedParchment.getTitle()).isEqualTo(parchment.getTitle());
    }

    @Test
    void parchments_retrieved_by_writer_id_have_no_parent_parchment() {
        Parchment firstParchment = saveParchment("First parchment's title", writer, null);
        Parchment secondParchment = saveParchment("Second parchment's title", writer, firstParchment);
        Parchment thirdParchment = saveParchment("Third parchment's title", writer, secondParchment);

        List<Parchment> parchments = parchmentRepository.findAllByWriterId(writer.getId());
        assertThat(parchments.size()).isEqualTo(3);

        Parchment retrievedFirstParchment = parchments.get(0);
        Parchment retrievedSecondParchment = parchments.get(1);
        Parchment retrievedThirdParchment = parchments.get(2);

        assertThat(retrievedFirstParchment.getTitle()).isEqualTo(firstParchment.getTitle());
        assertThat(retrievedSecondParchment.getTitle()).isEqualTo(secondParchment.getTitle());
        assertThat(retrievedThirdParchment.getTitle()).isEqualTo(thirdParchment.getTitle());

        assertThat(retrievedFirstParchment.getParentParchment()).isNull();
        assertThat(retrievedSecondParchment.getParentParchment()).isNull();
        assertThat(retrievedThirdParchment.getParentParchment()).isNull();
    }

    @Test
    void parchments_retrieved_by_find_all_bring_their_parent_parchment() {
        Parchment firstParchment = saveParchment("First parchment's title", writer, null);
        Parchment secondParchment = saveParchment("Second parchment's title", writer, firstParchment);
        Parchment thirdParchment = saveParchment("Third parchment's title", writer, secondParchment);

        List<Parchment> parchments = parchmentRepository.findAll();
        assertThat(parchments.size()).isEqualTo(3);

        Parchment retrievedFirstParchment = parchments.get(0);
        Parchment retrievedSecondParchment = parchments.get(1);
        Parchment retrievedThirdParchment = parchments.get(2);

        assertThat(retrievedFirstParchment.getTitle()).isEqualTo(firstParchment.getTitle());
        assertThat(retrievedSecondParchment.getTitle()).isEqualTo(secondParchment.getTitle());
        assertThat(retrievedThirdParchment.getTitle()).isEqualTo(thirdParchment.getTitle());

        assertThat(retrievedFirstParchment.getParentParchment()).isNull();
        assertThat(retrievedSecondParchment.getParentParchment().getId()).isEqualTo(retrievedFirstParchment.getId());
        assertThat(retrievedThirdParchment.getParentParchment().getId()).isEqualTo(retrievedSecondParchment.getId());
    }

    @Test
    void a_parchment_is_brought_by_their_id() {
        Parchment parchment = saveParchment("Title", writer, null);
        Optional<Parchment> retrievedParchment = parchmentRepository.findById(parchment.getId());

        assertThat(retrievedParchment).isPresent();
    }

    @Test
    void a_nonexistant_parchment_cannot_be_retrieved() {
        Optional<Parchment> retrievedParchment = parchmentRepository.findById((long) 1);

        assertThat(retrievedParchment).isNotPresent();
    }

    private Parchment saveParchment(String title, Writer writer, Parchment previousParchment) {
        Parchment parchment = new Parchment(title, "contents");
        if (previousParchment != null) {
            return parchmentRepository.save(parchment, writer.getId(), previousParchment.getId());
        }
        return parchmentRepository.save(parchment, writer.getId());
    }
}