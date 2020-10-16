package parchments_backend.wrappers;

import parchments_backend.domain.Breadcrumb;
import parchments_backend.domain.Parchment;

import java.util.List;

public class ParchmentResponseData {
    public Parchment parchment;
    public List<Breadcrumb> breadcrumbs;

    public ParchmentResponseData(Parchment parchment, List<Breadcrumb> breadcrumbs) {
        this.parchment = parchment;
        this.breadcrumbs = breadcrumbs;
    }
}
