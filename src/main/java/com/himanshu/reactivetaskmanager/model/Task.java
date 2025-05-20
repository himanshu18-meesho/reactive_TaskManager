package com.himanshu.reactivetaskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String id;
    private String description;
    private boolean completed;

    public Task(String description) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.completed = false;
    }
}
