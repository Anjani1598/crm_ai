package com.crm.mapper;

import com.crm.domain.Meeting;
import com.crm.dto.MeetingResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeetingMapper {
    @Mapping(target = "contactId", source = "contact.id")
    @Mapping(target = "dealId", source = "deal.id")
    MeetingResponseDTO toDto(Meeting meeting);
} 