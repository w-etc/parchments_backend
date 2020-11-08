package parchments_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import parchments_backend.domain.BreadcrumbList;
import parchments_backend.domain.Parchment;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.ParchmentRepository;
import parchments_backend.repositories.WriterRepository;
import parchments_backend.wrappers.WriterUser;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParchmentService {

    public static final String MUST_SPECIFY_A_WRITER_FOR_THIS_PARCHMENT = "You must specify a writer for this Parchment";
    public static final String WRITER_DOES_NOT_EXIST = "The writer does not exist";
    public static final String PARCHMENT_DOES_NOT_EXIST = "The parchment does not exist";
    @Autowired
    private ParchmentRepository parchmentRepository;

    @Autowired
    private WriterRepository writerRepository;


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
        List<Long> parchmentIds = parchmentRepository.findAllIdsByTitle(title);
        List<Parchment> parchments = parchmentRepository.findAllByIds(parchmentIds);
        setVoteInformationForParchments(parchments);

        return parchments;
    }

    public Parchment findById(Long id) {
        try {
            Parchment parchment = parchmentRepository.findById(id).get();
            setVoteInformation(parchment);
            return parchment;
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
            Parchment parchment = parchmentRepository.findRandomCoreParchment().get();
            setVoteInformation(parchment);
            Writer writer = writerRepository.findByParchmentId(parchment.getId()).get();
            parchment.setWriter(writer);
            return parchment;
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

    public void voteParchment(Long writerId, Long parchmentId) {
        try {
            parchmentRepository.voteParchment(writerId, parchmentId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void cancelVoteParchment(Long writerId, Long parchmentId) {
        try {
            parchmentRepository.cancelVoteParchment(writerId, parchmentId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean userIsAuthenticated() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    private void setVoteInformation(Parchment parchment) {
        parchment.setVoteCount(parchmentRepository.getVoteCount(parchment.getId()));
        if (userIsAuthenticated()) {
            WriterUser reader = (WriterUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            parchment.setReaderVoted(parchmentRepository.getReaderVoted(reader.getId(), parchment.getId()));
        }
    }

    private void setVoteInformationForParchments(List<Parchment> parchments) {
        List<Long> parchmentIds = parchments.stream().map(Parchment::getId).collect(Collectors.toList());
        List<Integer> voteCounts = parchmentRepository.getVoteCounts(parchmentIds);
        if (userIsAuthenticated()) {
            WriterUser reader = (WriterUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Boolean> readerVotedForParchments = parchmentRepository.getReaderVotedForParchments(reader.getId(), parchmentIds);
            for (int i = 0; i < parchments.size(); i++) {
                parchments.get(i).setVoteCount(voteCounts.get(i));
                parchments.get(i).setReaderVoted(readerVotedForParchments.get(i));
            }
        } else {
            for (int i = 0; i < parchments.size(); i++) {
                parchments.get(i).setVoteCount(voteCounts.get(i));
            }
        }
    }
}
