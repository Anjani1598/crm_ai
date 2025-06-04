package com.crm.repository;

import com.crm.domain.model.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long>, JpaSpecificationExecutor<Deal> {

    @Query("SELECT d FROM Deal d WHERE " +
           "(:customerId IS NULL OR d.contact.id = :customerId) AND " +
           "(:startDate IS NULL OR d.targetStartDate >= :startDate) AND " +
           "(:endDate IS NULL OR d.targetCompletionDate <= :endDate)")
    Page<Deal> findDealsWithFilters(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    List<Deal> findByContactId(Long contactId);
}