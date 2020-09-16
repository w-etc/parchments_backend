package parchments_backend.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import parchments_backend.domain.Parchment;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
public class ParchmentRepositoryTest {

    @Autowired
    private ParchmentRepository parchmentRepository;

    @Test
    void shouldWork() {
        List<Parchment> parchments = parchmentRepository.findAll();
        assertThat(parchments.size()).isEqualTo(0);
    }
}