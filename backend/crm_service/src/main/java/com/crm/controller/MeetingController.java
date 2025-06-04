package com.crm.controller;

import com.crm.domain.Meeting;
import com.crm.dto.CreateMeetingDTO;
import com.crm.dto.MeetingFilterDTO;
import com.crm.dto.MeetingResponseDTO;
import com.crm.mapper.MeetingMapper;
import com.crm.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingMapper meetingMapper;

    @PostMapping
    public ResponseEntity<MeetingResponseDTO> createMeeting(@RequestBody CreateMeetingDTO createMeetingDTO) {
        Meeting meeting;
        if ("CHAT".equals(createMeetingDTO.getMeetingType())) {
            meeting = meetingService.findOrCreateChatMeeting(createMeetingDTO);
        } else {
            meeting = meetingService.createMeeting(createMeetingDTO);
        }
        return ResponseEntity.ok(meetingMapper.toDto(meeting));
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<MeetingResponseDTO>> getMeetingsByContactId(@PathVariable Long contactId) {
        List<Meeting> meetings = meetingService.getMeetingsByContactId(contactId);
        List<MeetingResponseDTO> response = meetings.stream()
                .map(meetingMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MeetingResponseDTO>> getFilteredMeetings(@ModelAttribute MeetingFilterDTO filterDTO) {
        List<Meeting> meetings = meetingService.getFilteredMeetings(filterDTO);
        List<MeetingResponseDTO> response = meetings.stream()
                .map(meetingMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
} 