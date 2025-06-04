package com.crm.dto;

import lombok.Data;

@Data
public class CustomerInsightsDTO {
    private Long openSupportTickets;
    private Long upcomingMeetings;
    private Long recentInteractions;
    private Long activeProjects;
} 