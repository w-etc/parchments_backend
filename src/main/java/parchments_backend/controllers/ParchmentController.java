package parchments_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parchments_backend.domain.Parchment;
import parchments_backend.services.ParchmentService;
import parchments_backend.wrappers.ParchmentPostData;

import java.util.List;

@Controller
@RequestMapping(path="/parchment")
public class ParchmentController {

    @Autowired
    private ParchmentService parchmentService;

    @PostMapping
    public @ResponseBody Parchment saveParchment(@RequestBody ParchmentPostData postData) {
        Parchment createdParchment = parchmentService.save(postData.parchment, postData.writerId, postData.previousParchmentId);
        return createdParchment;
    }

    @GetMapping
    public @ResponseBody List<Parchment> getParchments(@RequestParam Long writerId) {
        return parchmentService.findAllByWriterId(writerId);
    }

    @GetMapping("/{id}")
    public @ResponseBody Parchment getParchment(@PathVariable Long id) {
        return parchmentService.findById(id);
    }
}
