package com.example.hmcts.integration;

import com.example.hmcts.dto.TaskRequestDto;
import com.example.hmcts.model.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testTaskCrudOperations() throws Exception {
        // Create a task request with future date
        TaskRequestDto taskRequest = new TaskRequestDto();
        taskRequest.setTaskTitle("Integration Test Task");
        taskRequest.setTaskDescription("Testing the full API flow");
        taskRequest.setTaskStatus(TaskStatus.TODO);
        taskRequest.setDueDateTime(LocalDateTime.now().plusDays(1));

        // 1. Create the task
        MvcResult result = mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskTitle", is("Integration Test Task")))
                .andExpect(jsonPath("$.taskStatus", is("TODO")))
                .andReturn();

        // Extract created task ID from response
        String content = result.getResponse().getContentAsString();
        int taskId = objectMapper.readTree(content).get("taskId").asInt();

        // 2. Get the task by ID
        mockMvc.perform(get("/task/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId", is(taskId)))
                .andExpect(jsonPath("$.taskTitle", is("Integration Test Task")));

        // 3. Update the task
        taskRequest.setTaskTitle("Updated Integration Test Task");
        mockMvc.perform(put("/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskTitle", is("Updated Integration Test Task")));

        // 4. Update task status
        mockMvc.perform(patch("/task/" + taskId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"COMPLETED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskStatus", is("COMPLETED")));

        // 5. Get all tasks (should include our task)
        mockMvc.perform(get("/task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].taskId", hasItem(taskId)));

        // 6. Delete the task
        mockMvc.perform(delete("/task/" + taskId))
                .andExpect(status().isNoContent());

// 7. Verify task is deleted - CORRECTED CODE
        mockMvc.perform(get("/task/" + taskId))
                .andExpect(status().isNotFound()); // Expect 404 Not Found
    }
}