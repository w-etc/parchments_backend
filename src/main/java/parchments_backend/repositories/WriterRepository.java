package parchments_backend.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import parchments_backend.domain.Writer;

import java.util.List;

public interface WriterRepository extends Neo4jRepository<Writer, Integer> {

    @Override
    <S extends Writer> S save(S s);

    List<Writer> findAll();
}
