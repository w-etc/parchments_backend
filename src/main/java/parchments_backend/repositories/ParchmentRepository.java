package parchments_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import parchments_backend.domain.Parchment;

import java.util.List;

public interface ParchmentRepository extends CrudRepository<Parchment, Integer> {

    @Override
    <S extends Parchment> S save(S s);

    List<Parchment> findAllByWriterId(Long writerId);

    Parchment findById(Long id);

    List<Parchment> findAll();
}
