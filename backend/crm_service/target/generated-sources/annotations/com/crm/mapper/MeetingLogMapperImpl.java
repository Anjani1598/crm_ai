package com.crm.mapper;

import com.crm.domain.Meeting;
import com.crm.domain.MeetingLog;
import com.crm.dto.MeetingLogResponseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-02T02:32:29+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class MeetingLogMapperImpl implements MeetingLogMapper {

    @Override
    public MeetingLogResponseDTO toDto(MeetingLog meetingLog) {
        if ( meetingLog == null ) {
            return null;
        }

        MeetingLogResponseDTO meetingLogResponseDTO = new MeetingLogResponseDTO();

        meetingLogResponseDTO.setMeetingId( meetingLogMeetingId( meetingLog ) );
        meetingLogResponseDTO.setId( meetingLog.getId() );
        meetingLogResponseDTO.setTimestamp( meetingLog.getTimestamp() );
        meetingLogResponseDTO.setLogType( meetingLog.getLogType() );
        meetingLogResponseDTO.setMessage( meetingLog.getMessage() );
        meetingLogResponseDTO.setSender( meetingLog.getSender() );
        meetingLogResponseDTO.setReceiver( meetingLog.getReceiver() );
        meetingLogResponseDTO.setStatus( meetingLog.getStatus() );
        meetingLogResponseDTO.setDuration( meetingLog.getDuration() );
        meetingLogResponseDTO.setAttachmentUrl( meetingLog.getAttachmentUrl() );
        meetingLogResponseDTO.setMetadata( meetingLog.getMetadata() );
        meetingLogResponseDTO.setCreatedAt( meetingLog.getCreatedAt() );
        meetingLogResponseDTO.setUpdatedAt( meetingLog.getUpdatedAt() );

        return meetingLogResponseDTO;
    }

    private Long meetingLogMeetingId(MeetingLog meetingLog) {
        if ( meetingLog == null ) {
            return null;
        }
        Meeting meeting = meetingLog.getMeeting();
        if ( meeting == null ) {
            return null;
        }
        Long id = meeting.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
