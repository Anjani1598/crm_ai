package com.crm.service.impl;

import com.crm.domain.model.Deal;
import com.crm.domain.model.DealRequirement;
import com.crm.domain.model.RequirementPriority;
import com.crm.domain.model.RequirementStatus;
import com.crm.dto.DealRequirementDTO;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.DealRepository;
import com.crm.repository.DealRequirementRepository;
import com.crm.service.DealRequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DealRequirementServiceImpl implements DealRequirementService {

    private final DealRequirementRepository requirementRepository;
    private final DealRepository dealRepository;

    @Override
    public DealRequirementDTO addRequirement(Long dealId, DealRequirementDTO requirementDTO) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found with id: " + dealId));

        DealRequirement requirement = new DealRequirement();
        requirement.setDeal(deal);
        requirement.setTitle(requirementDTO.getTitle());
        requirement.setDescription(requirementDTO.getDescription());
        requirement.setPriority(requirementDTO.getPriority());
        requirement.setStatus(RequirementStatus.PENDING);
        requirement.setCategory(requirementDTO.getCategory());

        DealRequirement savedRequirement = requirementRepository.save(requirement);
        return mapToDTO(savedRequirement);
    }

    @Override
    public List<DealRequirementDTO> getRequirements(Long dealId) {
        List<DealRequirement> requirements = requirementRepository.findByDealId(dealId);
        return requirements.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DealRequirementDTO updateRequirement(Long dealId, Long requirementId, DealRequirementDTO requirementDTO) {
        DealRequirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new ResourceNotFoundException("Requirement not found with id: " + requirementId));

        if (!requirement.getDeal().getId().equals(dealId)) {
            throw new ResourceNotFoundException("Requirement does not belong to deal with id: " + dealId);
        }

        requirement.setTitle(requirementDTO.getTitle());
        requirement.setDescription(requirementDTO.getDescription());
        requirement.setPriority(requirementDTO.getPriority());
        requirement.setStatus(requirementDTO.getStatus());
        requirement.setCategory(requirementDTO.getCategory());

        DealRequirement updatedRequirement = requirementRepository.save(requirement);
        return mapToDTO(updatedRequirement);
    }

    @Override
    public void deleteRequirement(Long dealId, Long requirementId) {
        DealRequirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new ResourceNotFoundException("Requirement not found with id: " + requirementId));

        if (!requirement.getDeal().getId().equals(dealId)) {
            throw new ResourceNotFoundException("Requirement does not belong to deal with id: " + dealId);
        }

        requirementRepository.delete(requirement);
    }

    private DealRequirementDTO mapToDTO(DealRequirement requirement) {
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