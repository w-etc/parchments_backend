package parchments_backend.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import parchments_backend.domain.Parchment;

import java.util.List;
import java.util.Optional;

public interface ParchmentRepository extends Neo4jRepository<Parchment, Long> {

    @Override
    <S extends Parchment> S save(S s);

    @Query("MATCH (w:Writer)-[wr:WROTE]->(p:Parchment) WHERE id(w) = $writerId return p")
    List<Parchment> findAllByWriterId(Long writerId);

    Optional<Parchment> findById(Long id);

    List<Parchment> findAll();
}
