package com.crm.service;

import com.crm.domain.Meeting;
import com.crm.domain.model.Contact;
import com.crm.domain.model.Deal;
import com.crm.domain.repository.ContactRepository;
import com.crm.dto.CreateMeetingDTO;
import com.crm.dto.MeetingFilterDTO;
import com.crm.repository.MeetingRepository;
import com.crm.repository.MeetingRepositoryImpl;
import com.crm.repository.DealRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingRepositoryImpl meetingRepositoryImpl;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private DealRepository dealRepository;

    @Transactional
    public Meeting createMeeting(CreateMeetingDTO createMeetingDTO) {
        Contact contact = contactRepository.findById(createMeetingDTO.getContactId())
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        Meeting meeting = new Meeting();
        meeting.setMeetingType(createMeetingDTO.getMeetingType());
        meeting.setMeetingDateTime(createMeetingDTO.getMeetingDateTime());
        meeting.setDuration(createMeetingDTO.getDuration());
        meeting.setTopic(createMeetingDTO.getTopic());
        meeting.setAdditionalNotes(createMeetingDTO.getAdditionalNotes());
        meeting.setContact(contact);

        // Handle optional deal association
        if (createMeetingDTO.getDealId() != null) {
            Deal deal = dealRepository.findById(createMeetingDTO.getDealId())
                    .orElseThrow(() -> new RuntimeException("Deal not found"));
            meeting.setDeal(deal);
        }

        return meetingRepository.save(meeting);
    }

    @Transactional(readOnly = true)
    public List<Meeting> getMeetingsByContactId(Long contactId) {
        // Verify contact exists
        if (!contactRepository.existsById(contactId)) {
            throw new RuntimeException("Contact not found");
        }
        return meetingRepositoryImpl.findByContactId(contactId);
    }

    @Transactional(readOnly = true)
    public List<Meeting> getFilteredMeetings(MeetingFilterDTO filterDTO) {
        // If contactId is provided, verify contact exists
        if (filterDTO.getContactId() != null && !contactRepository.existsById(filterDTO.getContactId())) {
            throw new RuntimeException("Contact not found");
        }

        return meetingRepositoryImpl.findMeetingsWithFilters(filterDTO);
    }

    @Transactional
    public Meeting findOrCreateChatMeeting(CreateMeetingDTO createMeetingDTO) {
        // Verify contact exists
        Optional<Contact> optionalContact = createMeetingDTO.getContactId()!=null?contactRepository.findById(createMeetingDTO.getContactId()): Optional.empty();

        Contact contact = optionalContact.orElse(null);
//        Deal deal1 = dealOptional.orElse(null);
        // Create filter to find existing chat meetings
        MeetingFilterDTO filterDTO = new MeetingFilterDTO();
//        if(ObjectUtils.isNotEmpty(contact)){
//            filterDTO.setContactId(contact.getId());
//        }
        filterDTO.setMeetingType("CHAT");
        filterDTO.setSortBy("meetingDateTime");
        filterDTO.setSortDirection("DESC");
        if(ObjectUtils.isNotEmpty(createMeetingDTO.getDealId())) {
            filterDTO.setDealId(createMeetingDTO.getDealId());
        }
        List<Meeting> existingMeetings = meetingRepository.findMeetingsWithFilter3(filterDTO.getDealId(),filterDTO.getMeetingType());


        // Find existing chat meetings
//        List<Meeting> existingMeetings = meetingRepositoryImpl.findMeetingsWithFilters(filterDTO);

//        log.info("existingMeetings {}",existingMeetings);

        // If there are existing chat meetings, return the latest one
        if (!existingMeetings.isEmpty()) {
            return existingMeetings.get(0);
        }

        // If no existing chat meetings found, create a new one
        Meeting meeting = new Meeting();
        meeting.setMeetingType(createMeetingDTO.getMeetingType());
        meeting.setMeetingDateTime(createMeetingDTO.getMeetingDateTime());
        meeting.setDuration(createMeetingDTO.getDuration());
        meeting.setTopic(createMeetingDTO.getTopic());
        meeting.setAdditionalNotes(createMeetingDTO.getAdditionalNotes());
        meeting.setContact(contact);

        // Handle optional deal association
        if (createMeetingDTO.getDealId() != null) {
            Deal deal = dealRepository.findById(createMeetingDTO.getDealId())
                    .orElseThrow(() -> new RuntimeException("Deal not found"));
            meeting.setDeal(deal);
        }

        return meetingRepository.save(meeting);
    }
} 