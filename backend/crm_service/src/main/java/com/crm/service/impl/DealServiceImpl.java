package com.crm.service.impl;

import com.crm.domain.model.Contact;
import com.crm.domain.model.Deal;
import com.crm.domain.model.DealRequirement;
import com.crm.domain.model.RequirementStatus;
import com.crm.domain.repository.ContactRepository;
import com.crm.dto.DealDTO;
import com.crm.dto.DealFilterDTO;
import com.crm.dto.DealRequirementDTO;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.DealRepository;
import com.crm.service.DealService;
import com.crm.statemachine.DealState;
import com.crm.statemachine.entity.DealStateEntity;
import com.crm.statemachine.repository.DealStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final ContactRepository contactRepository;
    private final DealStateRepository dealStateRepository;

    @Override
    public DealDTO createDeal(DealDTO dealDTO) {
        log.info("creating Deal");
        Contact contact = contactRepository.findById(dealDTO.getContactId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + dealDTO.getContactId()));

        Deal deal = new Deal();
        deal.setTitle(dealDTO.getTitle());
        deal.setProjectName(dealDTO.getProjectName());
        deal.setProjectType(dealDTO.getProjectType());
        deal.setDescription(dealDTO.getDescription());
        deal.setEstimatedBudget(dealDTO.getEstimatedBudget());
        deal.setAmount(dealDTO.getEstimatedBudget());
        deal.setTargetStartDate(dealDTO.getTargetStartDate());
        deal.setTargetCompletionDate(dealDTO.getTargetCompletionDate());
        deal.setContact(contact);

        // Create and set up the DealStateEntity
        DealStateEntity dealStateEntity = new DealStateEntity();
        dealStateEntity.setDeal(deal);
        dealStateEntity.setLastEvent("INITIAL");
        dealStateEntity.setState(DealState.PROPOSAL_SENT);
        
        // Set up the bidirectional relationship
        deal.setDealState(dealStateEntity);

        // Add and set up DealRequirement entities
        if (dealDTO.getRequirements() != null) {
            for (DealRequirementDTO reqDTO : dealDTO.getRequirements()) {
                DealRequirement requirement = new DealRequirement();
                requirement.setTitle(reqDTO.getTitle());
                requirement.setDescription(reqDTO.getDescription());
                requirement.setPriority(reqDTO.getPriority());
                requirement.setStatus(RequirementStatus.PENDING); // Set initial status
                requirement.setCategory(reqDTO.getCategory());
                deal.addRequirement(requirement); // Use the helper method
            }
        }

        // Save the deal (this will cascade to save the state and requirement entities)
        Deal savedDeal = dealRepository.save(deal);
        return mapToDTO(savedDeal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DealDTO> getDealsByContactId(Long contactId) {
        // Verify contact exists
        if (!contactRepository.existsById(contactId)) {
            throw new ResourceNotFoundException("Contact not found with id: " + contactId);
        }
        
        return dealRepository.findByContactId(contactId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealDTO> getFilteredDeals(DealFilterDTO filterDTO) {
        // If contactId is provided, verify contact exists
        if (filterDTO.getContactId() != null && !contactRepository.existsById(filterDTO.getContactId())) {
            throw new ResourceNotFoundException("Contact not found with id: " + filterDTO.getContactId());
        }

        // Set default sort if not provided
        String sortBy = filterDTO.getSortBy() != null ? filterDTO.getSortBy() : "targetStartDate";
        String sortDirection = filterDTO.getSortDirection() != null ? filterDTO.getSortDirection() : "DESC";

        // Create pageable object with sorting
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        // Get paginated results
        Page<Deal> dealsPage = dealRepository.findDealsWithFilters(
            filterDTO.getContactId(),
            filterDTO.getStartDate(),
            filterDTO.getEndDate(),
            pageable
        );

        // Map to DTOs
        return dealsPage.map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public DealDTO getDealById(Long dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found with id: " + dealId));
        return mapToDTO(deal);
    }

    private DealDTO mapToDTO(Deal deal) {
        DealDTO dto = new DealDTO();
        dto.setId(deal.getId());
        dto.setTitle(deal.getTitle());
        dto.setProjectName(deal.getProjectName());
        dto.setProjectType(deal.getProjectType());
        dto.setDescription(deal.getDescription());
        dto.setEstimatedBudget(deal.getEstimatedBudget());
        dto.setAmount(deal.getAmount());
        dto.setTargetStartDate(deal.getTargetStartDate());
        dto.setTargetCompletionDate(deal.getTargetCompletionDate());
        dto.setContactId(deal.getContact() != null ? deal.getContact().getId() : null);
        dto.setDealState(deal.getDealState() != null ? deal.getDealState().getState().name() : null);
        dto.setLastEvent(deal.getDealState() != null ? deal.getDealState().getLastEvent() : null);
        dto.setStateCreatedAt(deal.getDealState() != null ? deal.getDealState().getCreatedAt() : null);
        dto.setStateUpdatedAt(deal.getDealState() != null ? deal.getDealState().getUpdatedAt() : null);

        // Map requirements
        if (deal.getRequirements() != null) {
            dto.setRequirements(deal.getRequirements().stream()
                    .map(this::mapRequirementToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private DealRequirementDTO mapRequirementToDTO(DealRequirement requirement) {
        DealRequirementDTO dto = new DealRequirementDTO();
        dto.setId(requirement.getId());
        dto.setDealId(requirement.getDeal().getId());
        dto.setTitle(requirement.getTitle());
        dto.setDescription(requirement.getDescription());
        dto.setPriority(requirement.getPriority());
        dto.setStatus(requirement.getStatus());
        dto.setCategory(requirement.getCategory());
        dto.setCreatedAt(requirement.getCreatedAt());
        dto.setUpdatedAt(requirement.getUpdatedAt());
        return dto;
    }
} 