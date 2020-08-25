package parchments_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import parchments_backend.domain.Parchment;

public interface ParchmentRepository extends CrudRepository<Parchment, Integer> {

    @Override
    <S extends Parchment> S save(S s);
}
