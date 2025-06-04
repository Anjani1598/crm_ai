package com.crm.repository;

import com.crm.domain.Meeting;
import com.crm.dto.MeetingFilterDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByContactId(Long contactId);

    @Query(value = "from Meeting m where m.contact.id = ?1 and m.deal.id = ?2 and m.meetingType = ?3")
    List<Meeting> findMeetingsWithFilters(Long contactId,Long dealId, String meetingType);

    @Query(value = "from Meeting m where m.contact.id = ?1 and m.deal.id = ?2 and m.meetingType = ?3")
    List<Meeting> findMeetingsWithFilters2(Long contactId, String meetingType);

    @Query(value = "from Meeting m where m.deal.id = ?1 and m.meetingType = ?2")
    List<Meeting> findMeetingsWithFilter3(Long dealId, String meetingType);
}