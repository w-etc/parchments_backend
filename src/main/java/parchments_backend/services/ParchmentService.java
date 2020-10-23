package parchments_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import parchments_backend.domain.BreadcrumbList;
import parchments_backend.domain.Parchment;
import parchments_backend.repositories.ParchmentRepository;
import java.util.List;

@Service
public class ParchmentService {

    public static final String MUST_SPECIFY_A_WRITER_FOR_THIS_PARCHMENT = "You must specify a writer for this Parchment";
    public static final String WRITER_DOES_NOT_EXIST = "The writer does not exist";
    public static final String PARCHMENT_DOES_NOT_EXIST = "The parchment does not exist";
    @Autowired
    private ParchmentRepository parchmentRepository;

    public Parchment save(Parchment parchment, Long writerId, Long previousParchmentId) {
        if (writerId == null) {
            throw new RuntimeException(MUST_SPECIFY_A_WRITER_FOR_THIS_PARCHMENT);
        }
        try {
            if (previousParchmentId != null) {
                return parchmentRepository.save(parchment, writerId, previousParchmentId);
            }
            return parchmentRepository.save(parchment, writerId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Parchment> findAllByWriterId(Long writerId) {
        return parchmentRepository.findAllByWriterId(writerId);
    }

    public List<Parchment> findAllByTitle(String title) {
        return parchmentRepository.findAllByTitle(title);
    }

    public Parchment findById(Long id) {
        try {
            return parchmentRepository.findById(id).get();
        } catch (Exception e) {
            throw new RuntimeException(PARCHMENT_DOES_NOT_EXIST);
        }
    }

    public BreadcrumbList findBreadcrumbs(Parchment parchment) {
        try {
            BreadcrumbList breadcrumbList = parchmentRepository.findBreadcrumbs(parchment.getId());
            if (breadcrumbList == null) {
                breadcrumbList = new BreadcrumbList();
                breadcrumbList.ensureMinimumSize(parchment);
            }
            return breadcrumbList;
        } catch (Exception e) {
            throw new RuntimeException(PARCHMENT_DOES_NOT_EXIST);
        }
    }

    public List<Parchment> findCoreParchments(Integer page) {
        return parchmentRepository.findCoreParchments(PageRequest.of(page, 5)).getContent();
    }

    public Parchment findRandomCoreParchment() {
        try {
            return parchmentRepository.findRandomCoreParchment().get();
        } catch (Exception e) {
            throw new RuntimeException(PARCHMENT_DOES_NOT_EXIST);
        }
    }

    public List<Parchment> findContinuationsById(Long id, Integer page) {
        try {
            return parchmentRepository.findContinuationsById(PageRequest.of(page, 5), id).getContent();
        } catch (Exception e) {
            throw new RuntimeException(PARCHMENT_DOES_NOT_EXIST);
        }
    }
}
