package ru.t1.service;

import org.springframework.stereotype.Service;
import ru.t1.annotation.LogBefore;
import ru.t1.annotation.LogException;
import ru.t1.annotation.LogExecution;
import ru.t1.annotation.LogResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final List<String> tasks = new ArrayList<>();

    public TaskService() {
        tasks.addAll(List.of("task1", "task2", "task3"));
    }

    @LogBefore
    public boolean isContainTask(String task) {
        return tasks.contains(task);
    }

    @LogException
    public void addTask(String task) {
        if (!task.isBlank()) {
            tasks.add(task);
        } else {
            throw new IllegalArgumentException("Task is blank");
        }
    }

    @LogResult
    public List<String> getTasks() {
        return tasks;
    }

    @LogExecution
    public void addTasks(){
        for (int i = 0; i < 100_000; i++){
            tasks.add(UUID.randomUUID().toString());
        }
    }
}
