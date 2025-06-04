package com.crm.controller;

import com.crm.domain.MeetingLog;
import com.crm.dto.CreateMeetingLogDTO;
import com.crm.dto.MeetingLogResponseDTO;
import com.crm.mapper.MeetingLogMapper;
import com.crm.service.MeetingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meeting-logs")
public class MeetingLogController {

    @Autowired
    private MeetingLogService meetingLogService;

    @Autowired
    private MeetingLogMapper meetingLogMapper;

    @PostMapping
    public ResponseEntity<MeetingLogResponseDTO> createMeetingLog(@RequestBody CreateMeetingLogDTO createMeetingLogDTO) {
        MeetingLog meetingLog = meetingLogService.createMeetingLog(createMeetingLogDTO);
        return ResponseEntity.ok(meetingLogMapper.toDto(meetingLog));
    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<List<MeetingLogResponseDTO>> getMeetingLogsByMeetingId(@PathVariable Long meetingId) {
        List<MeetingLog> meetingLogs = meetingLogService.getMeetingLogsByMeetingId(meetingId);
        List<MeetingLogResponseDTO> response = meetingLogs.stream()
                .map(meetingLogMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meeting/{meetingId}/type/{logType}")
    public ResponseEntity<List<MeetingLogResponseDTO>> getMeetingLogsByMeetingIdAndType(
            @PathVariable Long meetingId,
            @PathVariable MeetingLog.LogType logType) {
        List<MeetingLog> meetingLogs = meetingLogService.getMeetingLogsByMeetingIdAndType(meetingId, logType);
        List<MeetingLogResponseDTO> response = meetingLogs.stream()
                .map(meetingLogMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Endpoint specifically for chat interactions
    @PostMapping("/chat")
    public ResponseEntity<MeetingLogResponseDTO> createChatLog(@RequestBody CreateMeetingLogDTO createMeetingLogDTO) {
        // Ensure the log type is CHAT
        createMeetingLogDTO.setLogType(MeetingLog.LogType.CHAT);
        MeetingLog meetingLog = meetingLogService.createMeetingLog(createMeetingLogDTO);
        return ResponseEntity.ok(meetingLogMapper.toDto(meetingLog));
    }

    // Get all chat logs for a meeting
    @GetMapping("/meeting/{meetingId}/chat")
    public ResponseEntity<List<MeetingLogResponseDTO>> getChatLogsByMeetingId(@PathVariable Long meetingId) {
        List<MeetingLog> chatLogs = meetingLogService.getMeetingLogsByMeetingIdAndType(meetingId, MeetingLog.LogType.CHAT);
        List<MeetingLogResponseDTO> response = chatLogs.stream()
                .map(meetingLogMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
} 