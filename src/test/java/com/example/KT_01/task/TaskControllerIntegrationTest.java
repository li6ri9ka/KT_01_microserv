package com.example.KT_01.task;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearRepository() {
        taskRepository.deleteAll();
    }

    @Test
    void shouldCreateAndListTasks() throws Exception {
        String createPayload = objectMapper.writeValueAsString(new RequestBody("Buy milk"));

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Buy milk"))
                .andExpect(jsonPath("$.completed").value(false));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Buy milk"));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task task = taskRepository.save(new Task("Initial", false));

        String updatePayload = objectMapper.writeValueAsString(new UpdateBody("Updated", true));

        mockMvc.perform(put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task task = taskRepository.save(new Task("Temporary", false));

        mockMvc.perform(delete("/tasks/{id}", task.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnBadRequestForBlankTitleOnCreate() throws Exception {
        String payload = objectMapper.writeValueAsString(new RequestBody("   "));

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void shouldReturnNotFoundForUnknownTaskOnDelete() throws Exception {
        mockMvc.perform(delete("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    private record RequestBody(String title) {
    }

    private record UpdateBody(String title, Boolean completed) {
    }
}
