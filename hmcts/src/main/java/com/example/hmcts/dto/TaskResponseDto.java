package com.example.hmcts.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskResponseDto {
    private int taskId;
    private String taskTitle;
    private String taskDescription;
    private String taskStatus;
    private LocalDateTime dueDateTime;

}
