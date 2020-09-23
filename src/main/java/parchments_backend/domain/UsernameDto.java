package parchments_backend.domain;

import javax.validation.constraints.NotEmpty;

public class UsernameDto {
    @NotEmpty
    String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
