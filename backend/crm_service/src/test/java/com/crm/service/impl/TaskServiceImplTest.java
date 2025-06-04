package com.crm.service.impl;

import com.crm.controller.TaskController.TaskDto;
import com.crm.domain.Task;
import com.crm.repository.TaskRepository;
import com.crm.exception.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() {
        // Given
        Task task1 = new Task("Task 1", "Desc 1", new Date(), "Assignee 1", "High", false);
        Task task2 = new Task("Task 2", "Desc 2", new Date(), "Assignee 2", "Low", true);
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        List<TaskDto> taskDtos = taskService.getAllTasks();

        // Then
        assertNotNull(taskDtos);
        assertEquals(2, taskDtos.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_shouldReturnTaskWhenFound() {
        // Given
        Long taskId = 1L;
        Task task = new Task("Task 1", "Desc 1", new Date(), "Assignee 1", "High", false);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // When
        TaskDto taskDto = taskService.getTaskById(taskId);

        // Then
        assertNotNull(taskDto);
        assertEquals(task.getTitle(), taskDto.getTitle());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getTaskById_shouldThrowTaskNotFoundExceptionWhenNotFound() {
        // Given
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void createTask_shouldReturnCreatedTask() {
        // Given
        TaskDto taskDto = new TaskDto(null, "New Task", "New Desc", new Date(), "New Assignee", "Medium", false);
        Task task = new Task("New Task", "New Desc", taskDto.getDueDate(), "New Assignee", "Medium", false);
        Task savedTask = new Task("New Task", "New Desc", taskDto.getDueDate(), "New Assignee", "Medium", false);
        savedTask.setId(1L);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // When
        TaskDto createdTaskDto = taskService.createTask(taskDto);

        // Then
        assertNotNull(createdTaskDto);
        assertEquals(1L, createdTaskDto.getId());
        assertEquals("New Task", createdTaskDto.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_shouldReturnUpdatedTaskWhenFound() {
        // Given
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto(taskId, "Updated Task", "Updated Desc", new Date(), "Updated Assignee", "High", true);
        Task existingTask = new Task("Old Task", "Old Desc", new Date(), "Old Assignee", "Low", false);
        existingTask.setId(taskId);
        Task updatedTask = new Task("Updated Task", "Updated Desc", taskDto.getDueDate(), "Updated Assignee", "High", true);
        updatedTask.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // When
        TaskDto resultTaskDto = taskService.updateTask(taskId, taskDto);

        // Then
        assertNotNull(resultTaskDto);
        assertEquals("Updated Task", resultTaskDto.getTitle());
        assertTrue(resultTaskDto.isCompleted());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_shouldThrowTaskNotFoundExceptionWhenNotFound() {
        // Given
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto(taskId, "Updated Task", "Updated Desc", new Date(), "Updated Assignee", "High", true);
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskId, taskDto));
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void deleteTask_shouldDeleteTaskWhenFound() {
        // Given
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);

        // When
        taskService.deleteTask(taskId);

        // Then
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void deleteTask_shouldThrowTaskNotFoundExceptionWhenNotFound() {
        // Given
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // When/Then
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(taskId));
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(0)).deleteById(taskId);
    }
} 