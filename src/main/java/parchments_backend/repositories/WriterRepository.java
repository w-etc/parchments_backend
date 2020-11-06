package parchments_backend.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import parchments_backend.domain.Writer;

import java.util.List;
import java.util.Optional;

public interface WriterRepository extends Neo4jRepository<Writer, Long> {

    @Override
    <S extends Writer> S save(S s);

    List<Writer> findAll();

    @Query("MATCH (w:Writer) WHERE w.username = $name RETURN w")
    Optional<Writer> findAuthenticatedUser(String name);

    @Query("MATCH (w:Writer) WHERE w.username = $name RETURN count(w) = 0")
    boolean checkValidUsername(String name);
}
