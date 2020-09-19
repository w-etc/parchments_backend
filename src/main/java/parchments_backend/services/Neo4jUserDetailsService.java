package parchments_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.WriterRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class Neo4jUserDetailsService implements UserDetailsService {

    @Autowired
    private WriterRepository writerRepository;

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
