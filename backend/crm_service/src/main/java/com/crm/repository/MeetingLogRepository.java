package com.crm.repository;

import com.crm.domain.MeetingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingLogRepository extends JpaRepository<MeetingLog, Long> {
    List<MeetingLog> findByMeetingIdOrderByTimestampAsc(Long meetingId);
    List<MeetingLog> findByMeetingIdAndLogTypeOrderByTimestampAsc(Long meetingId, MeetingLog.LogType logType);
} 