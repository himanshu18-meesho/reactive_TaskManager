package com.himanshu.reactivetaskmanager.repository;

import com.himanshu.reactivetaskmanager.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TaskRepository {
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();

    public Flux<Task> findAll() {
        return Flux.fromIterable(tasks.values());
    }

    public Mono<Task> findById(String id) {
        return Mono.justOrEmpty(tasks.get(id));
    }

    public Mono<Task> save(Task task) {
        tasks.put(task.getId(), task);
        return Mono.just(task);
    }

    public Mono<Task> markCompleted(String id) {
        Task task = tasks.get(id);
        if (task == null) return Mono.empty();
        
        task.setCompleted(true);
        tasks.put(id, task);
        return Mono.just(task);
    }
}
