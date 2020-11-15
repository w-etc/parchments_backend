package parchments_backend.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import parchments_backend.repositories.ParchmentRepository;

import java.util.List;

public class MostVotedParchmentSorter extends ParchmentSorter {

    public MostVotedParchmentSorter(ParchmentRepository parchmentRepository) {
        this.parchmentRepository = parchmentRepository;
    }

    @Override
    public List<Parchment> findAllByIds(List<Long> ids) {
        return parchmentRepository.findAllByMostVoted(ids);
    }

    @Override
    public Page<Parchment> findContinuationsById(Pageable pageable, Long id) {
        return parchmentRepository.findContinuationsByMostVoted(pageable, id);
    }
}
