package parchments_backend.wrappers;

import parchments_backend.domain.Parchment;

import java.util.List;

public class ParchmentResponseData {
    public Parchment parchment;
    public List<Object> breadcrumbs;

    public ParchmentResponseData(Parchment parchment, List<Object> breadcrumbs) {
        this.parchment = parchment;
        this.breadcrumbs = breadcrumbs;
    }
}
