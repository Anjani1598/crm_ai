package com.crm.statemachine.repository;

import com.crm.statemachine.entity.DealStateTransitionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealStateTransitionHistoryRepository extends JpaRepository<DealStateTransitionHistory, Long> {
    List<DealStateTransitionHistory> findByDealIdOrderByTransitionTimeDesc(Long dealId);
} 