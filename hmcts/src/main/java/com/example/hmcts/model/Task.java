package com.example.hmcts.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="Task")
@Getter
@Setter
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="task_id")
    private int taskId;

    @Column(name="task_title")
    private String taskTitle;

    @Column(name="task_description")
    private String taskDescription;

    @Enumerated(EnumType.STRING)
    @Column(name="task_status")
    private TaskStatus taskStatus;

    @Column(name="due_datetime")
    private LocalDateTime dueDateTime;

    public Task() {
    }

    public Task(int taskId, String taskTitle, String taskDescription, TaskStatus taskStatus, LocalDateTime dueDateTime) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.dueDateTime = dueDateTime;
    }

}
