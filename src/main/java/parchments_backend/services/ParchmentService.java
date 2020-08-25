package parchments_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parchments_backend.domain.Parchment;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.ParchmentRepository;

@Service
public class ParchmentService {

    @Autowired
    private ParchmentRepository parchmentRepository;

    public void save(Parchment parchment) {
        parchmentRepository.save(parchment);
    }

    public void findAllByWriter(Writer writer) {
        parchmentRepository.findAllByWriterId(writer.getId());
    }
}
