package com.crm.controller;

import com.crm.dto.DealDTO;
import com.crm.dto.DealFilterDTO;
import com.crm.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping
    public ResponseEntity<DealDTO> createDeal(@RequestBody DealDTO dealDTO) {
        DealDTO createdDeal = dealService.createDeal(dealDTO);
        return ResponseEntity.ok(createdDeal);
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<DealDTO>> getDealsByContactId(@PathVariable Long contactId) {
        List<DealDTO> deals = dealService.getDealsByContactId(contactId);
        return ResponseEntity.ok(deals);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<DealDTO>> getFilteredDeals(DealFilterDTO filterDTO) {
        Page<DealDTO> deals = dealService.getFilteredDeals(filterDTO);
        return ResponseEntity.ok(deals);
    }

    @GetMapping("/{dealId}")
    public ResponseEntity<DealDTO> getDealById(@PathVariable Long dealId) {
        DealDTO deal = dealService.getDealById(dealId);
        return ResponseEntity.ok(deal);
    }
} 