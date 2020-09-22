package parchments_backend.wrappers;

import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class WriterUser extends User {
    private Long id;

    public WriterUser(String username, String password, Long id) {
        super(username, password, Collections.emptyList());
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
