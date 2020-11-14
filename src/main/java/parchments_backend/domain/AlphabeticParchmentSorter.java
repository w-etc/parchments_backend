package parchments_backend.domain;

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
    public List<Integer> getVoteCounts(List<Long> ids) {
        return parchmentRepository.getVoteCountsByAlphabetic(ids);
    }

    @Override
    public List<Boolean> getReaderVotedForParchments(Long readerId, List<Long> ids) {
        return parchmentRepository.getReaderVotedForParchmentsByAlphabetic(readerId, ids);
    }
}
