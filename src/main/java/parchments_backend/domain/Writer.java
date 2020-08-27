package parchments_backend.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class Writer {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;

    @Relationship(type = "WROTE")
    private List<Parchment> parchments;

    public Writer() {
    }

    public Writer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Parchment> getParchments() {
        return parchments;
    }

    public void setParchments(List<Parchment> parchments) {
        this.parchments = parchments;
    }
}
