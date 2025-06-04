package com.crm.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class MeetingFilterDTO {
    private String meetingType;
    private Long contactId;
    private Long dealId;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
    
    private String sortBy = "meetingDateTime"; // Default sort by meeting date
    private String sortDirection = "DESC"; // Default sort direction

    // Validate sortBy values
    public void setSortBy(String sortBy) {
        if (sortBy != null && !sortBy.matches("meetingDateTime|duration|topic")) {
            throw new IllegalArgumentException("Invalid sortBy value. Must be one of: meetingDateTime, duration, topic");
        }
        this.sortBy = sortBy != null ? sortBy : "meetingDateTime";
    }

    // Validate sortDirection values
    public void setSortDirection(String sortDirection) {
        if (sortDirection != null && !sortDirection.matches("ASC|DESC")) {
            throw new IllegalArgumentException("Invalid sortDirection value. Must be either ASC or DESC");
        }
        this.sortDirection = sortDirection != null ? sortDirection : "DESC";
    }
} 