package com.crm.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateMeetingDTO {
    private String meetingType;
    private LocalDateTime meetingDateTime;
    private Integer duration;
    private String topic;
    private String additionalNotes;
    private Long contactId;
    private Long dealId;
} 