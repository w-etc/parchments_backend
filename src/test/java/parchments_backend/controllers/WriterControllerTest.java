package parchments_backend.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import parchments_backend.ControllerTestFactory;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.WriterRepository;
import parchments_backend.services.WriterService;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WriterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WriterRepository writerRepository;

    String token;

    @BeforeAll
    void setUpAll() throws Exception {
        token = ControllerTestFactory.getUserToken(mvc);
    }

    @Test
    void get_writer_id_returns_bad_request_when_the_writer_name_does_not_exist() throws Exception {
        String nonExistantName = "Non-existant name";

        mvc.perform(get("/writer/" + nonExistantName).header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", comparesEqualTo(WriterService.WRITER_NOT_FOUND)));
    }

    @Test
    void get_writer_id_returns_a_writer_id_by_name() throws Exception {
        String name = "Distinct Name";
        Writer writer = writerRepository.save(new Writer(name, "password"));

        mvc.perform(get("/writer/" + name).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", comparesEqualTo(Math.toIntExact(writer.getId()))));
    }

    @Test
    void register_returns_a_400_when_username_and_password_are_not_in_the_body() throws Exception {
        mvc.perform(post("/writer/register"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returns_a_400_when_username_and_password_are_empty() throws Exception {
        mvc.perform(post("/writer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"\", \"password\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returns_a_token_on_successful_case() throws Exception {
        mvc.perform(post("/writer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void login_returns_a_400_when_username_and_password_are_not_in_the_body() throws Exception {
        mvc.perform(post("/writer/login"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_returns_a_400_when_username_and_password_are_empty() throws Exception {
        mvc.perform(post("/writer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"\", \"password\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_returns_a_token_on_successful_case() throws Exception {
        mvc.perform(post("/writer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }
}
