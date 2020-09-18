package parchments_backend.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.WriterRepository;
import parchments_backend.services.WriterService;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WriterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WriterRepository writerRepository;

    @Test
    void the_controller_returns_bad_request_when_the_writer_name_does_not_exist() throws Exception {
        String nonExistantName = "Non-existant name";

        mvc.perform(get("/writer/" + nonExistantName))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", comparesEqualTo(WriterService.WRITER_NOT_FOUND)));
    }

    @Test
    void the_controller_returns_a_writer_id_by_name() throws Exception {
        String name = "Distinct Name";
        Writer writer = writerRepository.save(new Writer(name, "password"));

        mvc.perform(get("/writer/" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", comparesEqualTo(Math.toIntExact(writer.getId()))));
    }
}
