package com.himanshu.reactivetaskmanager.service;

import com.himanshu.reactivetaskmanager.model.Task;
import com.himanshu.reactivetaskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskService {
    @Autowired
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public Flux<Task> getAllTasks() {
        return repo.findAll();
    }

    public Mono<Task> createTask(String description) {
        return repo.save(new Task(description));
    }

    public Mono<Task> completeTask(String id) {
        return repo.markCompleted(id);
    }
}
