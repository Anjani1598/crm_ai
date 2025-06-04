package com.crm.dto;

import com.crm.domain.MeetingLog;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingLogResponseDTO {
    private Long id;
    private Long meetingId;
    private LocalDateTime timestamp;
    private MeetingLog.LogType logType;
    private String message;
    private String sender;
    private String receiver;
    private String status;
    private Integer duration;
    private String attachmentUrl;
    private String metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 