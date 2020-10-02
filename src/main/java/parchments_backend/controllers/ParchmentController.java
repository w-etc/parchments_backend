package parchments_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parchments_backend.domain.Parchment;
import parchments_backend.services.ParchmentService;
import parchments_backend.wrappers.ParchmentPostData;
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

    @GetMapping
    public @ResponseBody List<Parchment> getParchments(@RequestParam Long writerId) {
        return parchmentService.findAllByWriterId(writerId);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Object> getParchment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(parchmentService.findById(id));
        } catch (Exception e) {
            if (e.getMessage().equals(ParchmentService.WRITER_DOES_NOT_EXIST)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/core")
    public @ResponseBody ResponseEntity<Object> getCoreParchments() {
        try {
            return ResponseEntity.ok(parchmentService.findCoreParchments());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/core/random")
    public @ResponseBody ResponseEntity<Object> getRandomCoreParchment() {
        try {
            return ResponseEntity.ok(parchmentService.findRandomCoreParchment());
        } catch (Exception e) {
            if (e.getMessage().equals(ParchmentService.WRITER_DOES_NOT_EXIST)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
