package com.crm.controller;

import com.crm.CrmApplication;
import com.crm.controller.TaskController.TaskDto;
import com.crm.domain.Task;
import com.crm.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CrmApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    void getAllTasks_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/tasks"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$ ").isEmpty());
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        TaskDto taskDto = new TaskDto(null, "Integration Test Task", "Test Desc", new Date(), "Test Assignee", "Medium", false);

        mockMvc.perform(post("/api/tasks")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(taskDto)))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.title").value("Integration Test Task"));
    }

    @Test
    void getTaskById_shouldReturnTaskWhenFound() throws Exception {
        // Given
        Task task = new Task("Task to Find", "Desc to Find", new Date(), "Assignee to Find", "High", false);
        Task savedTask = taskRepository.save(task);

        // When & Then
        mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.title").value("Task to Find"));
    }

    @Test
    void getTaskById_shouldReturnNotFoundWhenTaskNotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        mockMvc.perform(get("/api/tasks/{id}", nonExistentId))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateTask_shouldReturnUpdatedTaskWhenFound() throws Exception {
        // Given
        Task task = new Task("Task to Update", "Desc to Update", new Date(), "Assignee to Update", "Low", false);
        Task savedTask = taskRepository.save(task);

        TaskDto updatedTaskDto = new TaskDto(savedTask.getId(), "Updated Task", "Updated Desc", new Date(), "Updated Assignee", "High", true);

        // When & Then
        mockMvc.perform(put("/api/tasks/{id}", savedTask.getId())
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(updatedTaskDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.title").value("Updated Task"))
               .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void updateTask_shouldReturnNotFoundWhenTaskNotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;
        TaskDto updatedTaskDto = new TaskDto(nonExistentId, "Updated Task", "Updated Desc", new Date(), "Updated Assignee", "High", true);

        // When & Then
        mockMvc.perform(put("/api/tasks/{id}", nonExistentId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(updatedTaskDto)))
               .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_shouldReturnNoContentWhenFound() throws Exception {
        // Given
        Task task = new Task("Task to Delete", "Desc to Delete", new Date(), "Assignee to Delete", "Medium", false);
        Task savedTask = taskRepository.save(task);

        // When & Then
        mockMvc.perform(delete("/api/tasks/{id}", savedTask.getId()))
               .andExpect(status().isNoContent());

        // Verify task is deleted
        mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()))
               .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_shouldReturnNotFoundWhenTaskNotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        mockMvc.perform(delete("/api/tasks/{id}", nonExistentId))
               .andExpect(status().isNotFound());
    }
} 