package com.example.hmcts.controller;

import com.example.hmcts.dto.TaskRequestDto;
import com.example.hmcts.model.Task;
import com.example.hmcts.model.TaskStatus;
import com.example.hmcts.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task testTask;
    private TaskRequestDto testTaskRequestDto;
    private LocalDateTime futureDate;

    @BeforeEach
    void setUp() {
        futureDate = LocalDateTime.now().plusDays(1);

        // Setup test Task
        testTask = new Task();
        testTask.setTaskId(1);
        testTask.setTaskTitle("Test Task");
        testTask.setTaskDescription("Test Description");
        testTask.setTaskStatus(TaskStatus.TODO);
        testTask.setDueDateTime(futureDate);

        // Setup test TaskRequestDto
        testTaskRequestDto = new TaskRequestDto();
        testTaskRequestDto.setTaskTitle("Test Task");
        testTaskRequestDto.setTaskDescription("Test Description");
        testTaskRequestDto.setTaskStatus(TaskStatus.TODO);
        testTaskRequestDto.setDueDateTime(futureDate);
    }

    @Test
    void testCreateTask() throws Exception {
        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(testTask);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId", is(1)))
                .andExpect(jsonPath("$.taskTitle", is("Test Task")))
                .andExpect(jsonPath("$.taskStatus", is("TODO")));
    }

    @Test
    void testGetTaskById() throws Exception {
        when(taskService.getTaskById(1)).thenReturn(testTask);

        mockMvc.perform(get("/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId", is(1)))
                .andExpect(jsonPath("$.taskTitle", is("Test Task")));
    }

    @Test
    void testGetAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].taskId", is(1)))
                .andExpect(jsonPath("$[0].taskTitle", is("Test Task")));
    }

    @Test
    void testUpdateTask() throws Exception {
        when(taskService.updateTask(eq(1), any(TaskRequestDto.class))).thenReturn(testTask);

        mockMvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId", is(1)))
                .andExpect(jsonPath("$.taskTitle", is("Test Task")));
    }

    @Test
    void testUpdateTaskStatus() throws Exception {
        testTask.setTaskStatus(TaskStatus.COMPLETED);
        when(taskService.updateTaskStatus(eq(1), eq(TaskStatus.COMPLETED))).thenReturn(testTask);

        mockMvc.perform(patch("/task/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"COMPLETED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskStatus", is("COMPLETED")));
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/task/1"))
                .andExpect(status().isNoContent());
    }
}