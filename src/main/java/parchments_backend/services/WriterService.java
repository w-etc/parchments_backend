package parchments_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.WriterRepository;

@Service
public class WriterService {

    @Autowired
    private WriterRepository writerRepository;

    public Long findWriterId(String writerName) {
        Writer writer = writerRepository.findByUsername(writerName);
        return writer.getId();
    }
}
