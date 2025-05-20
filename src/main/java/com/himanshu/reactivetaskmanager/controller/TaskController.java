package com.himanshu.reactivetaskmanager.controller;

import com.himanshu.reactivetaskmanager.model.Task;
import com.himanshu.reactivetaskmanager.service.TaskService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Task> getAllTasks() {
        return service.getAllTasks();
    }

    @PostMapping
    public Mono<Task> createTask(@RequestBody CreateTaskRequest request) {
        return service.createTask(request.getDescription());
    }

    @PutMapping("/{id}/complete")
    public Mono<Task> markTaskCompleted(@PathVariable String id) {
        return service.completeTask(id);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteTask(@PathVariable String id) {
        // return service.deleteTask(id);
        return service.deleteTask(id)
        .flatMap(task -> Mono.just(ResponseEntity.ok("Task deleted: " + task.getId())))
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Data
    public static class CreateTaskRequest {
        private String description;
    }
}
