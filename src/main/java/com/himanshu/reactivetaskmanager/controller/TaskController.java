package com.himanshu.reactivetaskmanager.controller;

import com.himanshu.reactivetaskmanager.model.Task;
import com.himanshu.reactivetaskmanager.service.TaskService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import org.springframework.http.MediaType;

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

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Task> streamTasks() {
        return service.getAllTasks()
                .collectList()
                .flatMapMany(tasks -> {
                    if (tasks.isEmpty()) {
                        return Flux.interval(Duration.ofSeconds(2))
                                .map(i -> new Task("dummy", "No tasks available", false));
                    }
                    return Flux.interval(Duration.ofSeconds(2))
                            .map(index -> tasks.get((int)(index % tasks.size())));
                });
    }

    @Data
    public static class CreateTaskRequest {
        private String description;
    }
}
