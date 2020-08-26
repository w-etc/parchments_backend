package parchments_backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Parchment {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String contents;

    @OneToMany(mappedBy="previousParchment")
    @JsonManagedReference
    @JsonIgnoreProperties("continuations")
    private List<Parchment> continuations;

    @ManyToOne
    @JsonIgnore
    private Parchment previousParchment;

    @ManyToOne
    private Writer writer;

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

    public Parchment getPreviousParchment() {
        return previousParchment;
    }

    public void setPreviousParchment(Parchment previousParchment) {
        this.previousParchment = previousParchment;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void tieTo(Parchment previousParchment) {
        this.previousParchment = previousParchment;
    }
}
