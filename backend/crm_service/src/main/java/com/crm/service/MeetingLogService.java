package com.crm.service;

import com.crm.domain.Meeting;
import com.crm.domain.MeetingLog;
import com.crm.dto.CreateMeetingLogDTO;
import com.crm.repository.MeetingLogRepository;
import com.crm.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingLogService {

    @Autowired
    private MeetingLogRepository meetingLogRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Transactional
    public MeetingLog createMeetingLog(CreateMeetingLogDTO createMeetingLogDTO) {
        Meeting meeting = meetingRepository.findById(createMeetingLogDTO.getMeetingId())
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        MeetingLog meetingLog = new MeetingLog();
        meetingLog.setMeeting(meeting);
        meetingLog.setTimestamp(createMeetingLogDTO.getTimestamp());
        meetingLog.setLogType(createMeetingLogDTO.getLogType());
        meetingLog.setMessage(createMeetingLogDTO.getMessage());
        meetingLog.setSender(createMeetingLogDTO.getSender());
        meetingLog.setReceiver(createMeetingLogDTO.getReceiver());
        meetingLog.setStatus(createMeetingLogDTO.getStatus());
        meetingLog.setDuration(createMeetingLogDTO.getDuration());
        meetingLog.setAttachmentUrl(createMeetingLogDTO.getAttachmentUrl());
        meetingLog.setMetadata(createMeetingLogDTO.getMetadata());

        return meetingLogRepository.save(meetingLog);
    }

    @Transactional(readOnly = true)
    public List<MeetingLog> getMeetingLogsByMeetingId(Long meetingId) {
        return meetingLogRepository.findByMeetingIdOrderByTimestampAsc(meetingId);
    }

    @Transactional(readOnly = true)
    public List<MeetingLog> getMeetingLogsByMeetingIdAndType(Long meetingId, MeetingLog.LogType logType) {
        return meetingLogRepository.findByMeetingIdAndLogTypeOrderByTimestampAsc(meetingId, logType);
    }
} 