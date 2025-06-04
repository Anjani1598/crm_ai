package com.crm.mapper;

import com.crm.domain.model.Contact;
import com.crm.dto.ContactDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    @Mapping(target = "totalInteractions", ignore = true)
    @Mapping(target = "lastInteractionDate", ignore = true)
    @Mapping(target = "recentActivities", ignore = true)
    ContactDto toDto(Contact contact);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Contact toEntity(ContactDto dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ContactDto dto, @MappingTarget Contact contact);
} 