package com.crm.dto;

import com.crm.domain.model.RequirementPriority;
import com.crm.domain.model.RequirementStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DealRequirementDTO {
    private Long id;
    private Long dealId;
    private String title;
    private String description;
    private RequirementPriority priority;
    private RequirementStatus status;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 