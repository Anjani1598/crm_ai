package com.crm.service;

import com.crm.dto.DealRequirementDTO;
import java.util.List;

public interface DealRequirementService {
    DealRequirementDTO addRequirement(Long dealId, DealRequirementDTO requirementDTO);
    List<DealRequirementDTO> getRequirements(Long dealId);
    DealRequirementDTO updateRequirement(Long dealId, Long requirementId, DealRequirementDTO requirementDTO);
    void deleteRequirement(Long dealId, Long requirementId);
} 