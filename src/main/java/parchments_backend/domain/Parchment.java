package parchments_backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class Parchment {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String synopsis;
    private String contents;

    @Transient
    private Integer voteCount;
    @Transient
    private boolean readerVoted = false;

    @JsonIgnore
    @Relationship(type = "CONTINUATION")
    private List<Parchment> continuations = new ArrayList<>();

    @JsonIgnoreProperties({"title", "synopsis", "contents", "continuations", "parentParchment", "writer"})
    @Relationship(type = "CONTINUATION", direction = Relationship.INCOMING)
    private Parchment parentParchment;

    @JsonIgnoreProperties({"parchments", "username", "password"})
    @Relationship(type = "WROTE", direction = Relationship.INCOMING)
    private Writer writer;

    public Parchment() {
    }

    public Parchment(String title, String synopsis, String contents) {
        this.title = title;
        this.synopsis = synopsis;
        this.contents = contents;
    }

    public Parchment(Writer writer, String title, String synopsis, String contents) {
        this.writer = writer;
        this.title = title;
        this.synopsis = synopsis;
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

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
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

    public Parchment getParentParchment() {
        return parentParchment;
    }

    public void setParentParchment(Parchment parentParchment) {
        this.parentParchment = parentParchment;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public boolean getReaderVoted() {
        return readerVoted;
    }

    public void setReaderVoted(boolean readerVoted) {
        this.readerVoted = readerVoted;
    }
}
