package parchments_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import parchments_backend.domain.Writer;

public interface WriterRepository extends CrudRepository<Writer, Integer> {

    @Override
    <S extends Writer> S save(S s);
}
