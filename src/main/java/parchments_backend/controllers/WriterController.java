package parchments_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parchments_backend.services.WriterService;

@Controller
@RequestMapping(path="/writer")
public class WriterController {

    @Autowired
    private WriterService writerService;

    @GetMapping("/{writerName}")
    public @ResponseBody ResponseEntity<Object> getWriterId(@PathVariable String writerName) {
        try {
            return ResponseEntity.ok(writerService.findWriterId(writerName));
        } catch (Exception e) {
            if (e.getMessage().equals(WriterService.WRITER_NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
