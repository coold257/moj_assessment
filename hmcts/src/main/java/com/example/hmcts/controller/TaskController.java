package com.example.hmcts.controller;

import com.example.hmcts.dto.TaskRequestDto;
import com.example.hmcts.dto.TaskResponseDto;
import com.example.hmcts.exception.TaskNotFoundException;
import com.example.hmcts.model.Task;
import com.example.hmcts.model.TaskStatus;
import com.example.hmcts.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for managing tasks.
 * Provides endpoints for creating, retrieving, updating, and deleting tasks.
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Creates a new task.
     *
     * @param taskRequest the task information
     * @return the created task with status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequest) {
        Task task = taskService.createTask(taskRequest);
        TaskResponseDto responseDto = convertToResponseDto(task);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId the ID of the task to retrieve
     * @return the task if found with status 200 (OK)
     * @throws TaskNotFoundException if the task is not found
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable int taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            TaskResponseDto responseDto = convertToResponseDto(task);
            return ResponseEntity.ok(responseDto);
        } catch (TaskNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all tasks.
     *
     * @return list of all tasks with status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskResponseDto> responseDtos = tasks.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Updates an existing task.
     *
     * @param taskId the ID of the task to update
     * @param taskRequest the updated task information
     * @return the updated task with status 200 (OK)
     * @throws TaskNotFoundException if the task is not found
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable int taskId,
            @Valid @RequestBody TaskRequestDto taskRequest) {
        Task updatedTask = taskService.updateTask(taskId, taskRequest);
        TaskResponseDto responseDto = convertToResponseDto(updatedTask);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Updates only the status of an existing task.
     *
     * @param taskId the ID of the task to update
     * @param status the new status
     * @return the updated task with status 200 (OK)
     * @throws TaskNotFoundException if the task is not found
     */
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(
            @PathVariable int taskId,
            @RequestBody TaskStatus status) {
        Task updatedTask = taskService.updateTaskStatus(taskId, status);
        TaskResponseDto responseDto = convertToResponseDto(updatedTask);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Deletes a task.
     *
     * @param taskId the ID of the task to delete
     * @return no content with status 204 (No Content)
     * @throws TaskNotFoundException if the task is not found
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable int taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Converts a Task entity to a TaskResponseDto.
     *
     * @param task the task entity to convert
     * @return the task response DTO
     */
    private TaskResponseDto convertToResponseDto(Task task) {
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setTaskId(task.getTaskId());
        responseDto.setTaskTitle(task.getTaskTitle());
        responseDto.setTaskDescription(task.getTaskDescription());
        responseDto.setTaskStatus(task.getTaskStatus().toString());
        responseDto.setDueDateTime(task.getDueDateTime());
        return responseDto;
    }
}