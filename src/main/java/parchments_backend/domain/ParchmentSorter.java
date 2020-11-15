package parchments_backend.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import parchments_backend.repositories.ParchmentRepository;

import java.util.List;

public abstract class ParchmentSorter {
    public static String MOST_VOTED = "most_voted";
    public static String ALPHABETIC = "alphabetic";

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

    public abstract Page<Parchment> findContinuationsById(Pageable pageable, Long id);
}
