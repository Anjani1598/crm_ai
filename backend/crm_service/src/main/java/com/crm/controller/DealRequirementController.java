package com.crm.controller;

import com.crm.dto.DealRequirementDTO;
import com.crm.service.DealRequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deals/{dealId}/requirements")
@RequiredArgsConstructor
public class DealRequirementController {

    private final DealRequirementService requirementService;

    @PostMapping
    public ResponseEntity<DealRequirementDTO> addRequirement(
            @PathVariable Long dealId,
            @RequestBody DealRequirementDTO requirementDTO) {
        DealRequirementDTO createdRequirement = requirementService.addRequirement(dealId, requirementDTO);
        return ResponseEntity.ok(createdRequirement);
    }

    @GetMapping
    public ResponseEntity<List<DealRequirementDTO>> getRequirements(@PathVariable Long dealId) {
        List<DealRequirementDTO> requirements = requirementService.getRequirements(dealId);
        return ResponseEntity.ok(requirements);
    }

    @PutMapping("/{requirementId}")
    public ResponseEntity<DealRequirementDTO> updateRequirement(
            @PathVariable Long dealId,
            @PathVariable Long requirementId,
            @RequestBody DealRequirementDTO requirementDTO) {
        DealRequirementDTO updatedRequirement = requirementService.updateRequirement(dealId, requirementId, requirementDTO);
        return ResponseEntity.ok(updatedRequirement);
    }

    @DeleteMapping("/{requirementId}")
    public ResponseEntity<Void> deleteRequirement(
            @PathVariable Long dealId,
            @PathVariable Long requirementId) {
        requirementService.deleteRequirement(dealId, requirementId);
        return ResponseEntity.noContent().build();
    }
} 