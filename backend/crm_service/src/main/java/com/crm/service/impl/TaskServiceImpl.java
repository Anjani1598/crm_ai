package com.crm.service.impl;

import com.crm.controller.TaskController.TaskDto;
import com.crm.domain.Task;
import com.crm.repository.TaskRepository;
import com.crm.service.TaskService;
import com.crm.exception.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskDto> getAllTasks() {
        logger.info("Fetching all tasks");
        List<Task> tasks = taskRepository.findAll();
        logger.info("Fetched {} tasks", tasks.size());
        return tasks.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        logger.info("Creating task with title: {}", taskDto.getTitle());
        // Basic service-level validation
        if (taskDto.getTitle() == null || taskDto.getTitle().trim().isEmpty()) {
            logger.error("Task title cannot be empty during creation");
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        if (taskDto.getDueDate() == null) {
            logger.error("Task due date cannot be null during creation");
            throw new IllegalArgumentException("Task due date cannot be null");
        }

        Task task = convertToEntity(taskDto);
        // Ensure ID is null for creation
        task.setId(null);
        Task savedTask = taskRepository.save(task);
        logger.info("Task created with ID: {}", savedTask.getId());
        return convertToDto(savedTask);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        logger.info("Fetching task with ID: {}", id);
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            logger.info("Task with ID {} found", id);
            return taskOptional.map(this::convertToDto)
                               .orElseThrow(() -> new TaskNotFoundException(id)); // This part is unreachable due to isPresent check
        } else {
            logger.warn("Task with ID {} not found", id);
            throw new TaskNotFoundException(id);
        }
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        logger.info("Updating task with ID: {}", id);
        // Basic service-level validation
        if (taskDto.getTitle() == null || taskDto.getTitle().trim().isEmpty()) {
            logger.error("Task title cannot be empty during update for ID: {}", id);
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        if (taskDto.getDueDate() == null) {
            logger.error("Task due date cannot be null during update for ID: {}", id);
            throw new IllegalArgumentException("Task due date cannot be null");
        }

        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            logger.info("Task with ID {} found for update", id);
            Task task = taskOptional.get();
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setDueDate(taskDto.getDueDate());
            task.setAssignee(taskDto.getAssignee());
            task.setPriority(taskDto.getPriority());
            task.setCompleted(taskDto.isCompleted());
            Task updatedTask = taskRepository.save(task);
            logger.info("Task with ID {} updated successfully", id);
            return convertToDto(updatedTask);
        } else {
            logger.warn("Task with ID {} not found for update", id);
            throw new TaskNotFoundException(id);
        }
    }

    @Override
    public void deleteTask(Long id) {
        logger.info("Deleting task with ID: {}", id);
        // Check if task exists before deleting
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            logger.info("Task with ID {} deleted successfully", id);
        } else {
            logger.warn("Task with ID {} not found for deletion.", id);
            throw new TaskNotFoundException(id);
        }
    }

    private TaskDto convertToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setAssignee(task.getAssignee());
        taskDto.setPriority(task.getPriority());
        taskDto.setCompleted(task.isCompleted());
        return taskDto;
    }

    private Task convertToEntity(TaskDto taskDto) {
        Task task = new Task();
        // Do not set ID here, it's handled by JPA for new entities
        // task.setId(taskDto.getId());
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setAssignee(taskDto.getAssignee());
        task.setPriority(taskDto.getPriority());
        task.setCompleted(taskDto.isCompleted());
        return task;
    }
} 