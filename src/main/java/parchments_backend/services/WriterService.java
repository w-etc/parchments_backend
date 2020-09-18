package parchments_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.WriterRepository;

@Service
public class WriterService {

    public static final String WRITER_NOT_FOUND = "Writer not found";
    @Autowired
    private WriterRepository writerRepository;

    public Long findWriterId(String writerName) {
        Writer writer = writerRepository.findByUsername(writerName);
        if (writer == null) {
            throw new RuntimeException(WRITER_NOT_FOUND);
        }
        return writer.getId();
    }
}
