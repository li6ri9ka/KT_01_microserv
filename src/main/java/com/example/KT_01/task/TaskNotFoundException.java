package com.example.KT_01.task;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task with id=" + id + " not found");
    }
}
