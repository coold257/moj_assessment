package com.example.hmcts.service;

import com.example.hmcts.dto.TaskRequestDto;
import com.example.hmcts.exception.TaskNotFoundException;
import com.example.hmcts.model.Task;
import com.example.hmcts.model.TaskStatus;
import com.example.hmcts.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void deleteTask(int taskId) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        taskRepository.delete(existingTask);
    }

    @Transactional
    public Task updateTask(int taskId, TaskRequestDto taskRequestDto) {
        validateTaskData(taskRequestDto);

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        setTaskFields(existingTask, taskRequestDto);
        return taskRepository.save(existingTask);
    }

    @Transactional
    public Task updateTaskStatus(int taskId, TaskStatus taskStatus) {
        if (taskStatus == null) {
            throw new IllegalArgumentException("Task status cannot be null");
        }

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        existingTask.setTaskStatus(taskStatus);
        return taskRepository.save(existingTask);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(int taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
    }

    @Transactional
    public Task createTask(TaskRequestDto taskRequestDto) {
        validateTaskData(taskRequestDto);

        Task task = new Task();
        setTaskFields(task, taskRequestDto);
        return taskRepository.save(task);
    }

    private void validateTaskData(TaskRequestDto taskRequestDto) {
        if (taskRequestDto == null) {
            throw new IllegalArgumentException("Task data cannot be null");
        }

        if (taskRequestDto.getTaskTitle() == null) {
            throw new IllegalArgumentException("Task title cannot be null");
        }
        if (taskRequestDto.getTaskTitle().isBlank()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        if (taskRequestDto.getTaskTitle().length() > 50) {
            throw new IllegalArgumentException("Task title exceeds maximum length of 50 characters");
        }

        if (taskRequestDto.getTaskStatus() == null) {
            throw new IllegalArgumentException("Task status cannot be null");
        }

        if (taskRequestDto.getDueDateTime() == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        if (taskRequestDto.getDueDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date must be in the future");
        }
    }

    private void setTaskFields(Task task, TaskRequestDto taskRequestDto) {
        task.setTaskTitle(taskRequestDto.getTaskTitle());
        task.setTaskDescription(taskRequestDto.getTaskDescription());
        task.setTaskStatus(taskRequestDto.getTaskStatus());
        task.setDueDateTime(taskRequestDto.getDueDateTime());
    }
}
