package parchments_backend.wrappers;

import parchments_backend.domain.Parchment;

import java.util.List;

public class ParchmentResponseData {
    public Parchment parchment;
    public List<Parchment> breadcrumbs;

    public ParchmentResponseData(Parchment parchment, List<Parchment> breadcrumbs) {
        this.parchment = parchment;
        this.breadcrumbs = breadcrumbs;
    }
}
