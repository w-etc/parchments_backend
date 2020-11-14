package parchments_backend.domain;

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
    public List<Integer> getVoteCounts(List<Long> ids) {
        return parchmentRepository.getVoteCountsByMostVoted(ids);
    }

    @Override
    public List<Boolean> getReaderVotedForParchments(Long readerId, List<Long> ids) {
        return parchmentRepository.getReaderVotedForParchmentsByMostVoted(readerId, ids);
    }
}
