package com.crm.repository;

import com.crm.domain.model.DealRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRequirementRepository extends JpaRepository<DealRequirement, Long> {
    List<DealRequirement> findByDealId(Long dealId);
    void deleteByDealId(Long dealId);
} 