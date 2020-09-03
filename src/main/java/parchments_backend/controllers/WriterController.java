package parchments_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parchments_backend.services.WriterService;

@Controller
@RequestMapping(path="/writer")
public class WriterController {

    @Autowired
    private WriterService writerService;

    @GetMapping("/{writerName}")
    public @ResponseBody Long getWriterId(@PathVariable String writerName) {
        return writerService.findWriterId(writerName);
    }
}
