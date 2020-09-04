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

    public void save(Parchment parchment, Long writerId) {
        try {
            parchmentRepository.save(parchment, writerId);
        } catch (Exception e) {
            // TODO: Handle this with a custom exception
            System.out.println(e.getMessage());
        }
    }

    public List<Parchment> findAllByWriterId(Long writerId) {
        return parchmentRepository.findAllByWriterId(writerId);
    }

    public Parchment findById(Long id) {
        return parchmentRepository.findById(id).get();
    }
}
