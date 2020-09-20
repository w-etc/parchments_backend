package parchments_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import parchments_backend.domain.Writer;
import parchments_backend.domain.WriterDto;
import parchments_backend.repositories.WriterRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class WriterService implements UserDetailsService {

    public static final String WRITER_NOT_FOUND = "Writer not found";

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Long findWriterId(String writerName) {
        Writer writer = writerRepository.findByUsername(writerName);
        if (writer == null) {
            throw new RuntimeException(WRITER_NOT_FOUND);
        }
        return writer.getId();
    }

    public UserDetails register(WriterDto writerDto) {
        String encodedPassword = passwordEncoder.encode(writerDto.getPassword());
        writerRepository.save(new Writer(writerDto.getUsername(), encodedPassword));
        return new User(writerDto.getUsername(), encodedPassword, Collections.emptyList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Writer> optionalWriter = writerRepository.findAuthenticatedUser(username);
        if (optionalWriter.isPresent()) {
            Writer retrievedWriter = optionalWriter.get();
            return new User(retrievedWriter.getUsername(), retrievedWriter.getPassword(), new ArrayList<>());
        }
        throw new UsernameNotFoundException("Writer with name " + username + " could not be found");
    }
}
