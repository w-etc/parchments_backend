package parchments_backend.domain;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String token;
    private final Long id;

    public JwtResponse(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public Long getId() {
        return id;
    }
}