package parchments_backend.domain;

public class Breadcrumb {
    private String title;
    private Long id;

    public Breadcrumb() {
    }

    public Breadcrumb(String title, Long id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
