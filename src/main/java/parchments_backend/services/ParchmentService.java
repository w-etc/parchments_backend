package parchments_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parchments_backend.domain.Parchment;
import parchments_backend.repositories.ParchmentRepository;

import java.util.List;

@Service
public class ParchmentService {

    public static final String MUST_SPECIFY_A_WRITER_FOR_THIS_PARCHMENT = "You must specify a writer for this Parchment";
    public static final String WRITER_DOES_NOT_EXIST = "The writer does not exist";
    @Autowired
    private ParchmentRepository parchmentRepository;

    public Parchment save(Parchment parchment, Long writerId, Long previousParchmentId) {
        if (writerId == null) {
            throw new RuntimeException(MUST_SPECIFY_A_WRITER_FOR_THIS_PARCHMENT);
        }
        try {
            if (previousParchmentId != null) {
                return parchmentRepository.save(parchment, writerId, previousParchmentId);
            }
            return parchmentRepository.save(parchment, writerId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Parchment> findAllByWriterId(Long writerId) {
        return parchmentRepository.findAllByWriterId(writerId);
    }

    public Parchment findById(Long id) {
        try {
            return parchmentRepository.findById(id).get();
        } catch (Exception e) {
            throw new RuntimeException(WRITER_DOES_NOT_EXIST);
        }
    }
}
