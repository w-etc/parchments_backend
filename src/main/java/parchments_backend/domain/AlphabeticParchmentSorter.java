package parchments_backend.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import parchments_backend.repositories.ParchmentRepository;

import java.util.List;

public class AlphabeticParchmentSorter extends ParchmentSorter {

    public AlphabeticParchmentSorter(ParchmentRepository parchmentRepository) {
        this.parchmentRepository = parchmentRepository;
    }

    @Override
    public List<Parchment> findAllByIds(List<Long> ids) {
        return parchmentRepository.findAllByAlphabetic(ids);
    }

    @Override
    public Page<Parchment> findContinuationsById(Pageable pageable, Long id) {
        return parchmentRepository.findContinuationsByAlphabetic(pageable, id);
    }
}

