package parchments_backend.controllers;

import net.minidev.json.JSONArray;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import parchments_backend.ControllerTestFactory;
import parchments_backend.DataLoader;
import parchments_backend.domain.Parchment;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.ParchmentRepository;
import parchments_backend.repositories.WriterRepository;
import parchments_backend.services.ParchmentService;

import java.util.Arrays;
import java.util.List;
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

    @MockBean
    private DataLoader dataLoader;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private ParchmentRepository parchmentRepository;

    @Test
    void get_parchments_by_writer_id_brings_an_empty_list_of_parchments_when_the_writer_does_not_exist() throws Exception {
        int nonExistantId = 100;

        mvc.perform(get("/parchment/writer/" + nonExistantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void get_parchments_by_writer_id_brings_an_empty_list_of_parchments_when_the_writer_has_nothing_written() throws Exception {
        Writer writer = getWriter();

        mvc.perform(get("/parchment/writer/" + writer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void get_parchments_by_writer_id_brings_the_writers_parchments() throws Exception {
        Writer writer = getWriter();
        parchmentRepository.save(new Parchment("Title", "Synopsis", "Contents"), writer.getId());

        mvc.perform(get("/parchment/writer/" + writer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void get_parchment_by_id_brings_a_parchment_by_id() throws Exception {
        Parchment parchment = getParchment();

        mvc.perform(get("/parchment/" + parchment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parchment.id", comparesEqualTo(Math.toIntExact(parchment.getId()))))
                .andExpect(jsonPath("$.parchment.title", comparesEqualTo(parchment.getTitle())))
                .andExpect(jsonPath("$.parchment.contents", comparesEqualTo(parchment.getContents())))
                .andExpect(jsonPath("$.parchment.parentParchment", equalTo(null)));
    }

    @Test
    void get_parchment_by_id_returns_bad_request_if_the_parchment_does_not_exist() throws Exception {
        mvc.perform(get("/parchment/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", comparesEqualTo(ParchmentService.PARCHMENT_DOES_NOT_EXIST)));
    }

    @Test
    void create_parchment_creates_a_parchment_without_parent_parchment() throws Exception {
        String token = ControllerTestFactory.getUserToken(mvc);
        String title = "Recognizable title";
        String json = "{\"parchment\": {\"title\": \"" + title + "\", \"contents\": \"\"}}";
        mvc.perform(post("/parchment").contentType(MediaType.APPLICATION_JSON).content(json).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        Optional<Parchment> parchment = parchmentRepository.findByTitle(title);

        assertThat(parchment).isPresent();
    }

    @Test
    void create_parchment_creates_a_parchment_with_parent_parchment() throws Exception {
        String token = ControllerTestFactory.getUserToken(mvc);
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
        String token = ControllerTestFactory.getUserToken(mvc);
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

    @Test
    void get_core_parchments_brings_parchments_without_parent() throws Exception {
        Parchment parchment = getParchment();
        mvc.perform(get("/parchment/core?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", comparesEqualTo(Math.toIntExact(parchment.getId()))));
    }

    @Test
    void get_core_parchments_does_not_bring_parchments_with_parent() throws Exception {
        Parchment coreParchment = getParchment();
        Writer writer = getWriter();
        Parchment parchment = parchmentRepository.save(new Parchment("Title", "Synopsis", "Contents"), writer.getId(), coreParchment.getId());

        mvc.perform(get("/parchment/core?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", comparesEqualTo(Math.toIntExact(coreParchment.getId()))))
                .andExpect(jsonPath("$[0].id", not(comparesEqualTo(Math.toIntExact(parchment.getId())))));
    }

    @Test
    void get_continuations_returns_the_continuations_of_a_parchment() throws Exception {
        Writer writer = getWriter();
        Parchment coreParchment = getParchment();
        List<Parchment> continuations = Arrays.asList(getContinuation(writer, coreParchment), getContinuation(writer, coreParchment));


        mvc.perform(get("/parchment/" + coreParchment.getId() + "/continuations?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(continuations.size())));
    }

    @Test
    void get_continuations_returns_an_empty_list_if_the_parchment_has_no_continuations() throws Exception {
        Parchment coreParchment = getParchment();

        mvc.perform(get("/parchment/" + coreParchment.getId() + "/continuations?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void get_random_parchment_brings_a_parchment_with_breadcrumbs() throws Exception {
        getParchment();

        mvc.perform(get("/parchment/core/random"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parchment", notNullValue()))
                .andExpect(jsonPath("$.breadcrumbs", isA(JSONArray.class)));
    }

    private Writer getWriter() {
        return writerRepository.save(new Writer("username", "password"));
    }

    private Parchment getParchment() {
        return parchmentRepository.save(new Parchment("Title", "Synopsis", "Contents"));
    }

    private Parchment getContinuation(Writer writer, Parchment parent) {
        return parchmentRepository.save(new Parchment("Continuation", "Synopsis", "Contents"), writer.getId(), parent.getId());
    }
}
