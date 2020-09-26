package parchments_backend.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import parchments_backend.domain.Writer;
import parchments_backend.repositories.WriterRepository;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.notNullValue;
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
        mvc.perform(post("/writer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"\", \"password\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_returns_a_token_on_successful_case() throws Exception {
        String username = "username";
        String password = "password";
        mvc.perform(post("/writer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}"));

        mvc.perform(post("/writer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void check_valid_username_returns_true_if_the_username_is_free_to_use() throws Exception {
        mvc.perform(post("/writer/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", comparesEqualTo(true)));
    }

    @Test
    void check_valid_username_returns_false_if_the_username_is_taken() throws Exception {
        String username = "username";
        writerRepository.save(new Writer(username, "password"));
        mvc.perform(post("/writer/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"" + username + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", comparesEqualTo(false)));
    }

    @Test
    void check_valid_username_returns_400_if_no_username_is_provided() throws Exception {
        mvc.perform(post("/writer/check")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void check_valid_username_returns_400_if_an_empty_username_is_provided() throws Exception {
        mvc.perform(post("/writer/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"\"}"))
                .andExpect(status().isBadRequest());
    }
}
