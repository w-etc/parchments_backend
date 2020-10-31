package parchments_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parchments_backend.domain.BreadcrumbList;
import parchments_backend.domain.Parchment;
import parchments_backend.services.ParchmentService;
import parchments_backend.wrappers.ParchmentPostData;
import parchments_backend.wrappers.ParchmentResponseData;
import parchments_backend.wrappers.WriterUser;
import java.util.List;

@Controller
@RequestMapping(path="/parchment")
public class ParchmentController {

    @Autowired
    private ParchmentService parchmentService;

    @PostMapping
    public @ResponseBody ResponseEntity<Object> saveParchment(@RequestBody ParchmentPostData postData) {
        try {
            WriterUser user = (WriterUser) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            return ResponseEntity.ok(parchmentService.save(postData.parchment, user.getId(), postData.previousParchmentId));
        } catch (Exception e) {
            if (e.getMessage().equals(ParchmentService.MUST_SPECIFY_A_WRITER_FOR_THIS_PARCHMENT)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/writer/{id}")
    public @ResponseBody List<Parchment> getParchmentsByWriterId(@PathVariable Long id) {
        return parchmentService.findAllByWriterId(id);
    }

    @GetMapping("/title/{title}")
    public @ResponseBody List<Parchment> getParchmentsByTitle(@PathVariable String title) {
        return parchmentService.findAllByTitle(title);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Object> getParchment(@PathVariable Long id) {
        try {
            Parchment parchment = parchmentService.findById(id);
            BreadcrumbList breadcrumbList = parchmentService.findBreadcrumbs(parchment);
            return ResponseEntity.ok(new ParchmentResponseData(parchment, breadcrumbList.getList()));
        } catch (Exception e) {
            if (e.getMessage().equals(ParchmentService.PARCHMENT_DOES_NOT_EXIST)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/continuations")
    public @ResponseBody ResponseEntity<Object> getContinuations(@PathVariable Long id, @RequestParam Integer page) {
        try {
            List<Parchment> parchments = parchmentService.findContinuationsById(id, page);
            return ResponseEntity.ok(parchments);
        } catch (Exception e) {
            if (e.getMessage().equals(ParchmentService.PARCHMENT_DOES_NOT_EXIST)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/core")
    public @ResponseBody ResponseEntity<Object> getCoreParchments(@RequestParam Integer page) {
        try {
            return ResponseEntity.ok(parchmentService.findCoreParchments(page));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/core/random")
    public @ResponseBody ResponseEntity<Object> getRandomCoreParchment() {
        try {
            Parchment parchment = parchmentService.findRandomCoreParchment();
            BreadcrumbList breadcrumbList = parchmentService.findBreadcrumbs(parchment);
            return ResponseEntity.ok(new ParchmentResponseData(parchment, breadcrumbList.getList()));
        } catch (Exception e) {
            if (e.getMessage().equals(ParchmentService.PARCHMENT_DOES_NOT_EXIST)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
