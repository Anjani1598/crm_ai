package com.crm.mapper;

import com.crm.domain.Meeting;
import com.crm.domain.model.Contact;
import com.crm.domain.model.Deal;
import com.crm.dto.MeetingResponseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-02T11:56:03+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class MeetingMapperImpl implements MeetingMapper {

    @Override
    public MeetingResponseDTO toDto(Meeting meeting) {
        if ( meeting == null ) {
            return null;
        }

        MeetingResponseDTO meetingResponseDTO = new MeetingResponseDTO();

        meetingResponseDTO.setContactId( meetingContactId( meeting ) );
        meetingResponseDTO.setDealId( meetingDealId( meeting ) );
        meetingResponseDTO.setId( meeting.getId() );
        meetingResponseDTO.setMeetingType( meeting.getMeetingType() );
        meetingResponseDTO.setMeetingDateTime( meeting.getMeetingDateTime() );
        meetingResponseDTO.setDuration( meeting.getDuration() );
        meetingResponseDTO.setTopic( meeting.getTopic() );
        meetingResponseDTO.setAdditionalNotes( meeting.getAdditionalNotes() );
        meetingResponseDTO.setCreatedAt( meeting.getCreatedAt() );
        meetingResponseDTO.setUpdatedAt( meeting.getUpdatedAt() );

        return meetingResponseDTO;
    }

    private Long meetingContactId(Meeting meeting) {
        if ( meeting == null ) {
            return null;
        }
        Contact contact = meeting.getContact();
        if ( contact == null ) {
            return null;
        }
        Long id = contact.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long meetingDealId(Meeting meeting) {
        if ( meeting == null ) {
            return null;
        }
        Deal deal = meeting.getDeal();
        if ( deal == null ) {
            return null;
        }
        Long id = deal.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
