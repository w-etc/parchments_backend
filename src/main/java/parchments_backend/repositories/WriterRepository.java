package parchments_backend.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import parchments_backend.domain.Writer;

import java.util.List;
import java.util.Optional;

public interface WriterRepository extends Neo4jRepository<Writer, Integer> {

    @Override
    <S extends Writer> S save(S s);

    List<Writer> findAll();

    Writer findByUsername(String name);

    @Query("MATCH (w:Writer) WHERE w.username = $name RETURN w.username, w.password")
    Optional<Writer> findAuthenticatedUser(String name);
}
