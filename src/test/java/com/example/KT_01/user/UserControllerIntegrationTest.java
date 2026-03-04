package com.example.KT_01.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void resetStorage() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAndGetUser() throws Exception {
        String payload = objectMapper.writeValueAsString(new UserPayload("Ivan", "ivan@example.com", 22));

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"))
                .andExpect(jsonPath("$.age").value(22))
                .andReturn();

        Map<?, ?> body = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        Number id = (Number) body.get("id");

        mockMvc.perform(get("/api/users/{id}", id.longValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.age").value(22));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User user = userRepository.save(new User(null, "Ivan", "ivan@example.com", 22));
        String updatePayload = objectMapper.writeValueAsString(new UserPayload("Petr", "petr@example.com", 25));

        mockMvc.perform(put("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Petr"))
                .andExpect(jsonPath("$.email").value("petr@example.com"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        User user = userRepository.save(new User(null, "Ivan", "ivan@example.com", 22));

        mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldExposeOpenApiDocs() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/users']").exists())
                .andExpect(jsonPath("$.paths['/api/users/{id}']").exists());
    }

    @Test
    void shouldExposeSwaggerUiShortcut() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {
        String payload = objectMapper.writeValueAsString(new UserPayload("Ivan", "invalid", 20));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        userRepository.save(new User(null, "Ivan", "ivan@example.com", 22));
        String payload = objectMapper.writeValueAsString(new UserPayload("Petr", "ivan@example.com", 23));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("email must be unique"));
    }

    private record UserPayload(String name, String email, Integer age) {
    }
}
