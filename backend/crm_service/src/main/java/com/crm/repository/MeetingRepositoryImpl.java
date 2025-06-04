package com.crm.repository;

import com.crm.domain.Meeting;
import com.crm.dto.MeetingFilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MeetingRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Meeting> findMeetingsWithFilters(MeetingFilterDTO filterDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meeting> query = cb.createQuery(Meeting.class);
        Root<Meeting> root = query.from(Meeting.class);

        // Build predicates for filtering
        List<Predicate> predicates = new ArrayList<>();

        if(filterDTO.getDealId()!=null){
            predicates.add(cb.equal(root.get("deal").get("id"), filterDTO.getContactId()));
        }

        if (filterDTO.getContactId() != null) {
            predicates.add(cb.equal(root.get("contact").get("id"), filterDTO.getContactId()));
        }

        if (filterDTO.getMeetingType() != null) {
            predicates.add(cb.equal(root.get("meetingType"), filterDTO.getMeetingType()));
        }

        if (filterDTO.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("meetingDateTime"), filterDTO.getStartDate()));
        }

        if (filterDTO.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("meetingDateTime"), filterDTO.getEndDate()));
        }

        // Add all predicates to the query
        query.where(predicates.toArray(new Predicate[0]));

        // Handle sorting
        if (filterDTO.getSortBy() != null) {
            Path<?> sortPath;
            switch (filterDTO.getSortBy()) {
                case "meetingDateTime":
                    sortPath = root.get("meetingDateTime");
                    break;
                case "duration":
                    sortPath = root.get("duration");
                    break;
                case "topic":
                    sortPath = root.get("topic");
                    break;
                default:
                    sortPath = root.get("meetingDateTime");
            }

            if ("DESC".equalsIgnoreCase(filterDTO.getSortDirection())) {
                query.orderBy(cb.desc(sortPath));
            } else {
                query.orderBy(cb.asc(sortPath));
            }
        }

        return entityManager.createQuery(query).getResultList();
    }

    public List<Meeting> findByContactId(Long contactId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meeting> query = cb.createQuery(Meeting.class);
        Root<Meeting> root = query.from(Meeting.class);

        // Create predicate for contact ID
        Predicate contactPredicate = cb.equal(root.get("contact").get("id"), contactId);
        
        // Add the predicate to the query
        query.where(contactPredicate);
        
        // Order by meeting date time descending by default
        query.orderBy(cb.desc(root.get("meetingDateTime")));

        return entityManager.createQuery(query).getResultList();
    }
} 