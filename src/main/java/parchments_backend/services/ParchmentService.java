package parchments_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parchments_backend.domain.Parchment;
import parchments_backend.repositories.ParchmentRepository;

import java.util.List;

@Service
public class ParchmentService {

    @Autowired
    private ParchmentRepository parchmentRepository;

    public Parchment save(Parchment parchment, Long writerId, Long previousParchmentId) {
        Parchment createdParchment = null;
        try {
            createdParchment = parchmentRepository.save(parchment, writerId, previousParchmentId);
        } catch (Exception e) {
            // TODO: Handle this with a custom exception
            System.out.println(e.getMessage());
        }
        return createdParchment;
    }

    public List<Parchment> findAllByWriterId(Long writerId) {
        return parchmentRepository.findAllByWriterId(writerId);
    }

    public Parchment findById(Long id) {
        return parchmentRepository.findById(id).get();
    }

    public List<Parchment> findAllByParentParchmentId(Long parchmentId) {
        return parchmentRepository.findAllByParentParchmentId(parchmentId);
    }
}
