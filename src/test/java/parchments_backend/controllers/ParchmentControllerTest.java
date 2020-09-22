package parchments_backend.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import parchments_backend.ControllerTestFactory;
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

    String token;

    @BeforeEach
    void setUp() throws Exception {
        token = ControllerTestFactory.getUserToken(mvc);
    }

    @Test
    void get_parchments_by_writer_id_brings_an_empty_list_of_parchments_when_the_writer_does_not_exist() throws Exception {
        int nonExistantId = 100;

        mvc.perform(get("/parchment?writerId=" + nonExistantId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void get_parchments_by_writer_id_brings_an_empty_list_of_parchments_when_the_writer_has_nothing_written() throws Exception {
        Writer writer = getWriter();

        mvc.perform(get("/parchment?writerId=" + writer.getId()).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void get_parchments_by_writer_id_brings_the_writers_parchments() throws Exception {
        Writer writer = getWriter();
        parchmentRepository.save(new Parchment("Title", "Contents"), writer.getId());

        mvc.perform(get("/parchment?writerId=" + writer.getId()).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void get_parchment_by_id_brings_a_parchment_by_id() throws Exception {
        Parchment parchment = getParchment();

        mvc.perform(get("/parchment/" + parchment.getId()).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(Math.toIntExact(parchment.getId()))))
                .andExpect(jsonPath("$.title", comparesEqualTo(parchment.getTitle())))
                .andExpect(jsonPath("$.contents", comparesEqualTo(parchment.getContents())))
                .andExpect(jsonPath("$.continuations", hasSize(0)))
                .andExpect(jsonPath("$.parentParchment", equalTo(null)));
    }

    @Test
    void get_parchment_by_id_returns_bad_request_if_the_parchment_does_not_exist() throws Exception {
        mvc.perform(get("/parchment/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", comparesEqualTo(ParchmentService.WRITER_DOES_NOT_EXIST)));
    }

    @Test
    void create_parchment_creates_a_parchment_without_parent_parchment() throws Exception {
        String title = "Recognizable title";
        String json = "{\"parchment\": {\"title\": \"" + title + "\", \"contents\": \"\"}}";
        mvc.perform(post("/parchment").contentType(MediaType.APPLICATION_JSON).content(json).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        Optional<Parchment> parchment = parchmentRepository.findByTitle(title);

        assertThat(parchment).isPresent();
    }

    @Test
    void create_parchment_creates_a_parchment_with_parent_parchment() throws Exception {
        Parchment parentParchment = getParchment();
        String title = "Recognizable title";
        String json = "{\"parchment\": {\"title\": \"" + title + "\", \"contents\": \"\"}, \"previousParchmentId\": " + parentParchment.getId() + "}";
        mvc.perform(post("/parchment").contentType(MediaType.APPLICATION_JSON).content(json).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        Optional<Parchment> parchment = parchmentRepository.findByTitle(title);

        assertThat(parchment).isPresent();
        Parchment retrievedParentParchment = parchment.get().getParentParchment();
        assertThat(retrievedParentParchment.getId()).isEqualTo(parentParchment.getId());
    }

    @Test
    void create_parchment_does_not_create_a_parchment_when_no_body_is_sent() throws Exception {
        mvc.perform(post("/parchment").header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

// TODO: Make it pass
//    @Test
//    void create_parchment_does_not_create_a_parchment_when_no_parchment_data_is_sent() throws Exception {
//        String json = "{\"parchment\": {}}";
//        mvc.perform(post("/parchment").contentType(MediaType.APPLICATION_JSON).content(json).header("Authorization", "Bearer " + token))
//                .andExpect(status().isBadRequest());
//    }

    private Writer getWriter() {
        return writerRepository.save(new Writer("username", "password"));
    }

    private Parchment getParchment() {
        return parchmentRepository.save(new Parchment("Title", "Contents"));
    }
}
