package com.example.hmcts.repository;

import com.example.hmcts.model.Task;
import com.example.hmcts.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testSaveTask() {
        // Create a new task
        Task task = new Task();
        task.setTaskTitle("Repository Test Task");
        task.setTaskDescription("Testing JPA Repository");
        task.setTaskStatus(TaskStatus.TODO);
        task.setDueDateTime(LocalDateTime.now().plusDays(1));

        // Save it
        Task savedTask = taskRepository.save(task);

        // Verify it was saved with an ID
        assertNotNull(savedTask.getTaskId());
        assertEquals("Repository Test Task", savedTask.getTaskTitle());
    }

    @Test
    void testFindById() {
        // Create and save a task
        Task task = new Task();
        task.setTaskTitle("Find By ID Test");
        task.setTaskStatus(TaskStatus.TODO);
        task.setDueDateTime(LocalDateTime.now().plusDays(1));
        Task savedTask = taskRepository.save(task);

        // Find it by ID
        Optional<Task> foundTask = taskRepository.findById(savedTask.getTaskId());

        // Verify it was found
        assertTrue(foundTask.isPresent());
        assertEquals("Find By ID Test", foundTask.get().getTaskTitle());
    }

    @Test
    void testFindAll() {
        // Clear existing data
        taskRepository.deleteAll();

        // Create and save two tasks
        Task task1 = new Task();
        task1.setTaskTitle("Task 1");
        task1.setTaskStatus(TaskStatus.TODO);
        task1.setDueDateTime(LocalDateTime.now().plusDays(1));
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTaskTitle("Task 2");
        task2.setTaskStatus(TaskStatus.IN_PROGRESS);
        task2.setDueDateTime(LocalDateTime.now().plusDays(2));
        taskRepository.save(task2);

        // Find all tasks
        List<Task> tasks = taskRepository.findAll();

        // Verify both were found
        assertEquals(2, tasks.size());
    }

    @Test
    void testUpdateTask() {
        // Create and save a task
        Task task = new Task();
        task.setTaskTitle("Original Title");
        task.setTaskStatus(TaskStatus.TODO);
        task.setDueDateTime(LocalDateTime.now().plusDays(1));
        Task savedTask = taskRepository.save(task);

        // Update the task
        savedTask.setTaskTitle("Updated Title");
        taskRepository.save(savedTask);

        // Verify the update
        Optional<Task> updatedTask = taskRepository.findById(savedTask.getTaskId());
        assertTrue(updatedTask.isPresent());
        assertEquals("Updated Title", updatedTask.get().getTaskTitle());
    }

    @Test
    void testDeleteTask() {
        // Create and save a task
        Task task = new Task();
        task.setTaskTitle("To Be Deleted");
        task.setTaskStatus(TaskStatus.TODO);
        task.setDueDateTime(LocalDateTime.now().plusDays(1));
        Task savedTask = taskRepository.save(task);

        // Delete the task
        taskRepository.deleteById(savedTask.getTaskId());

        // Verify it was deleted
        Optional<Task> deletedTask = taskRepository.findById(savedTask.getTaskId());
        assertFalse(deletedTask.isPresent());
    }
}