package com.example.hmcts.dto;

import com.example.hmcts.model.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequestDto {

    @NotBlank(message = "Task title is required")
    @Size(max = 50)
    private String taskTitle;

    private String taskDescription;

    @NotNull(message = "Task status is required")
    private TaskStatus taskStatus;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDateTime;

}

