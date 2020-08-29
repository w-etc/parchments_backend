package parchments_backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class Parchment {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String contents;

    @JsonIgnoreProperties("continuations")
    @Relationship(type = "CONTINUATION")
    private List<Parchment> continuations;

    public Parchment() {
    }

    public Parchment(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<Parchment> getContinuations() {
        return continuations;
    }

    public void setContinuations(List<Parchment> continuations) {
        this.continuations = continuations;
    }
}
