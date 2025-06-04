package com.crm.service;

import com.crm.controller.TaskController.TaskDto;

import java.util.List;

public interface TaskService {
    List<TaskDto> getAllTasks();
    TaskDto createTask(TaskDto taskDto);
    TaskDto getTaskById(Long id);
    TaskDto updateTask(Long id, TaskDto taskDto);
    void deleteTask(Long id);
    // TODO: Add other service methods for CRUD operations
} 