package parchments_backend.domain;

import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.ArrayList;
import java.util.List;

@QueryResult
public class BreadcrumbList {
    private List<Breadcrumb> list = new ArrayList<>();

    public BreadcrumbList() {
    }

    public BreadcrumbList(List<Breadcrumb> list) {
        this.list = list;
    }

    public void ensureMinimumSize(Parchment parchment) {
        if (this.list.isEmpty()) {
            this.list.add(new Breadcrumb(parchment.getTitle(), parchment.getId()));
        }
    }

    public List<Breadcrumb> getList() {
        return this.list;
    }
}
