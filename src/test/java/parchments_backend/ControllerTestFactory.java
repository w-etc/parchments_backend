package parchments_backend;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ControllerTestFactory {

    public static String getUserToken(MockMvc mvc) throws Exception {
        AtomicReference<String> token = new AtomicReference<>("");
        mvc.perform(post("/writer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"test user\", \"password\": \"test password\"}"))
                .andDo(handler -> {
                    String body = handler.getResponse().getContentAsString();
                    token.set((String) new JSONObject(body).get("token"));
                });
        return token.get();
    }
}
