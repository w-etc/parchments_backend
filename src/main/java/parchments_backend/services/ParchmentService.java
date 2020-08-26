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

    public void save(Parchment parchment) {
        parchmentRepository.save(parchment);
    }

    public List<Parchment> findAllByWriterId(Long writerId) {
        return parchmentRepository.findAllByWriterId(writerId);
    }

    public List<Parchment> findById(Long id) {
        return parchmentRepository.findById(id);
    }
}
