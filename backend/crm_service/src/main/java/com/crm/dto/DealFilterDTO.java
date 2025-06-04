package com.crm.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DealFilterDTO {
    private Long contactId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String sortBy; // "projectName", "estimatedBudget", "targetStartDate", "targetCompletionDate"
    private String sortDirection; // "ASC" or "DESC"
    
    // Pagination parameters
    private Integer page = 0;
    private Integer size = 10;
} 