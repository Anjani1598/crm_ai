package com.crm.dto;

import com.crm.domain.model.Contact.ContactStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContactDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private String position;
    private int totalInteractions;
    private LocalDateTime lastInteractionDate;
    private List<String> recentActivities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 