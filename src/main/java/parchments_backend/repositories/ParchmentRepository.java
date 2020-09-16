package parchments_backend.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import parchments_backend.domain.Parchment;

import java.util.List;
import java.util.Optional;

public interface ParchmentRepository extends Neo4jRepository<Parchment, Long> {

    @Query("MATCH (w:Writer) WHERE id(w) = $writerId CREATE (w)-[:WROTE]->(p:Parchment{title:$s.title, contents:$s.contents}) RETURN p")
    Parchment save(Parchment s, Long writerId);

    @Query("MATCH (w:Writer) WHERE id(w) = $writerId MATCH (pre:Parchment) WHERE id(pre) = $previousParchmentId CREATE (w)-[:WROTE]->(p:Parchment{title:$s.title, contents:$s.contents})<-[:CONTINUATION]-(pre) RETURN p")
    Parchment save(Parchment s, Long writerId, Long previousParchmentId);

    @Query("MATCH (w:Writer)-[wr:WROTE]->(p:Parchment) WHERE id(w) = $writerId return p")
    List<Parchment> findAllByWriterId(Long writerId);

    Optional<Parchment> findById(Long id);

    List<Parchment> findAll();
}
