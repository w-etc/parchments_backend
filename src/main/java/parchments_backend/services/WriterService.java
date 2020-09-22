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
import parchments_backend.wrappers.WriterUser;

import java.util.Collections;
import java.util.Optional;

@Service
public class WriterService implements UserDetailsService {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            return new WriterUser(retrievedWriter.getUsername(), retrievedWriter.getPassword(), retrievedWriter.getId());
        }
        throw new UsernameNotFoundException("Writer with name " + username + " could not be found");
    }
}
