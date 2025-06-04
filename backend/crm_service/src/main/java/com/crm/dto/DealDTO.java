package com.crm.dto;

import com.crm.domain.model.RequirementPriority;
import com.crm.domain.model.RequirementStatus;
import com.crm.statemachine.entity.DealStateEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DealDTO {
    private Long id;
    private String title;
    private String projectName;
    private String projectType;
    private String description;
    private BigDecimal estimatedBudget;
    private BigDecimal amount;
    private LocalDate targetStartDate;
    private LocalDate targetCompletionDate;
    private Long contactId;
    private String dealState;
    private String lastEvent;
    private LocalDateTime stateCreatedAt;
    private LocalDateTime stateUpdatedAt;
    private List<DealRequirementDTO> requirements;
}