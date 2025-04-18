package com.example.hmcts.service;

import com.example.hmcts.dto.TaskRequestDto;
import com.example.hmcts.exception.TaskNotFoundException;
import com.example.hmcts.model.Task;
import com.example.hmcts.model.TaskStatus;
import com.example.hmcts.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

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
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task created = taskService.createTask(testTaskRequestDto);

        assertNotNull(created);
        assertEquals("Test Task", created.getTaskTitle());
        assertEquals(TaskStatus.TODO, created.getTaskStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));

        Task found = taskService.getTaskById(1);

        assertNotNull(found);
        assertEquals(1, found.getTaskId());
        assertEquals("Test Task", found.getTaskTitle());
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(99);
        });
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(testTask));

        List<Task> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        testTaskRequestDto.setTaskTitle("Updated Title");
        Task updated = taskService.updateTask(1, testTaskRequestDto);

        assertNotNull(updated);
        assertEquals("Updated Title", updated.getTaskTitle());
    }

    @Test
    void testUpdateTaskStatus() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task updated = taskService.updateTaskStatus(1, TaskStatus.COMPLETED);

        assertNotNull(updated);
        assertEquals(TaskStatus.COMPLETED, updated.getTaskStatus());
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        doNothing().when(taskRepository).delete(any(Task.class));

        assertDoesNotThrow(() -> taskService.deleteTask(1));
        verify(taskRepository, times(1)).delete(any(Task.class));
    }
}