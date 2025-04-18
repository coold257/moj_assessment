package com.example.hmcts.repository;

import com.example.hmcts.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Integer> {

}
