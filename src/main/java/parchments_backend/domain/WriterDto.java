package parchments_backend.domain;

import javax.validation.constraints.NotEmpty;

public class WriterDto {
    @NotEmpty
    String username;
    @NotEmpty
    String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
