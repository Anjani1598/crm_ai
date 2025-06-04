package com.crm.statemachine.repository;

import com.crm.statemachine.entity.DealStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealStateRepository extends JpaRepository<DealStateEntity, Long> {
} 