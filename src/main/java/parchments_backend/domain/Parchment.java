package parchments_backend.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Parchment {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String contents;

    @OneToMany(mappedBy="previousParchment")
    private Set<Parchment> continuations;
    @ManyToOne
    private Parchment previousParchment;

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


    public void tieTo(Parchment previousParchment) {
        this.previousParchment = previousParchment;
    }
}
