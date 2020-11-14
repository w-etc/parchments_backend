package parchments_backend.domain;

import parchments_backend.repositories.ParchmentRepository;

import java.util.List;

public abstract class ParchmentSorter {
    static String MOST_VOTED = "most_voted";
    static String ALPHABETIC = "alphabetic";

    public static ParchmentSorter byType(String type, ParchmentRepository parchmentRepository) {
        if (type.equals(MOST_VOTED)) {
            return new MostVotedParchmentSorter(parchmentRepository);
        }
        if (type.equals(ALPHABETIC)) {
            return new AlphabeticParchmentSorter(parchmentRepository);
        }
        throw new RuntimeException("Unexpected Parchment Sorter type " + type);
    }

    protected ParchmentRepository parchmentRepository;

    public abstract List<Parchment> findAllByIds(List<Long> ids);

    public abstract List<Integer> getVoteCounts(List<Long> ids);

    public abstract List<Boolean> getReaderVotedForParchments(Long readerId, List<Long> ids);
}
