package com.crm.mapper;

import com.crm.domain.MeetingLog;
import com.crm.dto.MeetingLogResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeetingLogMapper {
    @Mapping(target = "meetingId", source = "meeting.id")
    MeetingLogResponseDTO toDto(MeetingLog meetingLog);
} 