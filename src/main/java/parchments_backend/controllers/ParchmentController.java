package parchments_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parchments_backend.domain.Parchment;
import parchments_backend.repositories.ParchmentRepository;

@Controller
@RequestMapping(path="/parchment")
public class ParchmentController {

    @Autowired
    private ParchmentRepository parchmentService;

    @PostMapping
    public @ResponseBody String saveParchment(@RequestBody Parchment parchment) {
        parchmentService.save(parchment);
        return "Success!";
    }
}
