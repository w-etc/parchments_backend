package parchments_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parchments_backend.domain.Parchment;
import parchments_backend.services.ParchmentService;
import java.util.List;

@Controller
@RequestMapping(path="/parchment")
public class ParchmentController {

    @Autowired
    private ParchmentService parchmentService;

    @PostMapping
    public @ResponseBody String saveParchment(@RequestBody Parchment parchment) {
        parchmentService.save(parchment);
        return "Success!";
    }

    @GetMapping
    public @ResponseBody List<Parchment> getParchments(@RequestParam Long writerId) {
        return parchmentService.findAllByWriterId(writerId);
    }

    @GetMapping("/{id}")
    public @ResponseBody List<Parchment> getParchment(@PathVariable Long id) {
        return parchmentService.findById(id);
    }
}
