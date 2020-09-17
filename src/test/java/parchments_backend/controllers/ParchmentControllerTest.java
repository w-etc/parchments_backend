package parchments_backend.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import parchments_backend.domain.Parchment;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.ParchmentRepository;
import parchments_backend.repositories.WriterRepository;
import parchments_backend.services.ParchmentService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ParchmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private ParchmentRepository parchmentRepository;

    @Test
    void the_controller_brings_an_empty_list_of_parchments_when_the_writer_does_not_exist() throws Exception {
        int nonExistantId = 100;

        mvc.perform(get("/parchment?writerId=" + nonExistantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void the_controller_brings_an_empty_list_of_parchments_when_the_writer_has_nothing_written() throws Exception {
        Writer writer = getWriter();

        mvc.perform(get("/parchment?writerId=" + writer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void the_controller_brings_the_writers_parchments() throws Exception {
        Writer writer = getWriter();
        parchmentRepository.save(new Parchment("Title", "Contents"), writer.getId());

        mvc.perform(get("/parchment?writerId=" + writer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void the_controller_brings_a_parchment_by_id() throws Exception {
        Parchment parchment = getParchment();

        mvc.perform(get("/parchment/" + parchment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(Math.toIntExact(parchment.getId()))))
                .andExpect(jsonPath("$.title", comparesEqualTo(parchment.getTitle())))
                .andExpect(jsonPath("$.contents", comparesEqualTo(parchment.getContents())))
                .andExpect(jsonPath("$.continuations", hasSize(0)))
                .andExpect(jsonPath("$.parentParchment", equalTo(null)));
    }

    @Test
    void the_controller_returns_bad_request_if_the_parchment_does_not_exist() throws Exception {
        mvc.perform(get("/parchment/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", comparesEqualTo(ParchmentService.WRITER_DOES_NOT_EXIST)));
    }

    @Test
    void the_controller_creates_a_parchment_without_parent_parchment() throws Exception {
        Writer writer = getWriter();
        String title = "Recognizable title";
        String json = "{\"parchment\": {\"title\": \"" + title + "\", \"contents\": \"\"}, \"writerId\": " + writer.getId() + "}";
        mvc.perform(post("/parchment").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        Optional<Parchment> parchment = parchmentRepository.findByTitle(title);

        assertThat(parchment).isPresent();
    }

    @Test
    void the_controller_creates_a_parchment_with_parent_parchment() throws Exception {
        Writer writer = getWriter();
        Parchment parentParchment = getParchment();
        String title = "Recognizable title";
        String json = "{\"parchment\": {\"title\": \"" + title + "\", \"contents\": \"\"}, \"writerId\": " + writer.getId() + ", \"previousParchmentId\": " + parentParchment.getId() + "}";
        mvc.perform(post("/parchment").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        Optional<Parchment> parchment = parchmentRepository.findByTitle(title);

        assertThat(parchment).isPresent();
        Parchment retrievedParentParchment = parchment.get().getParentParchment();
        assertThat(retrievedParentParchment.getId()).isEqualTo(parentParchment.getId());
    }

    @Test
    void the_controller_does_not_create_a_parchment_when_no_body_is_sent() throws Exception {
        mvc.perform(post("/parchment"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void the_controller_does_not_create_a_parchment_when_no_parchment_data_is_sent() throws Exception {
        Writer writer = getWriter();
        String json = "{\"parchment\": {}, \"writerId\": " + writer.getId();
        mvc.perform(post("/parchment").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void the_controller_does_not_create_a_parchment_when_no_writer_id_is_sent() throws Exception {
        String title = "Recognizable title";
        String json = "{\"parchment\": {\"title\": \"" + title + "\", \"contents\": \"\"}}";
        mvc.perform(post("/parchment").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", comparesEqualTo(ParchmentService.MUST_SPECIFY_A_WRITER_FOR_THIS_PARCHMENT)));
    }


    private Writer getWriter() {
        return writerRepository.save(new Writer("username", "password"));
    }

    private Parchment getParchment() {
        return parchmentRepository.save(new Parchment("Title", "Contents"));
    }
}
