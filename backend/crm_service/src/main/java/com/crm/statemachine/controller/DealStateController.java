package com.crm.statemachine.controller;

import com.crm.statemachine.DealState;
import com.crm.statemachine.DealStateMachineService;
import com.crm.statemachine.dto.DealStateResponse;
import com.crm.statemachine.dto.DealStateTransitionHistoryResponse;
import com.crm.statemachine.dto.DealStateTransitionHistoryWrapper;
import com.crm.statemachine.dto.DealStateTransitionRequest;
import com.crm.statemachine.entity.DealStateTransitionHistory;
import com.crm.statemachine.exception.DealStateException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/deals")
public class DealStateController {

    private final DealStateMachineService stateMachineService;

    public DealStateController(DealStateMachineService stateMachineService) {
        this.stateMachineService = stateMachineService;
    }

    @GetMapping("/{dealId}/state")
    public ResponseEntity<DealStateResponse> getCurrentState(
            @PathVariable Long dealId) {
        try {
            DealState currentState = stateMachineService.getState(dealId);
            return ResponseEntity.ok(new DealStateResponse(dealId, currentState, true));
        } catch (DealStateException e) {
            throw e;
        }
    }

    @PostMapping("/{dealId}/state/transition")
    public ResponseEntity<DealStateResponse> transitionState(
            @PathVariable Long dealId,
            @Valid @RequestBody DealStateTransitionRequest request) {
        try {
            boolean success = stateMachineService.sendEvent(dealId, request.getEvent());
            if (!success) {
                throw new DealStateException("Invalid state transition for deal: " + dealId);
            }
            DealState currentState = stateMachineService.getState(dealId);
            return ResponseEntity.ok(new DealStateResponse(dealId, currentState, true));
        } catch (DealStateException e) {
            throw e;
        }
    }

    @GetMapping("/{dealId}/state/history")
    public ResponseEntity<DealStateTransitionHistoryWrapper> getStateTransitionHistory(
            @PathVariable Long dealId) {
        try {
            List<DealStateTransitionHistory> history = stateMachineService.getTransitionHistory(dealId);
            // Sort history in ascending order of transitionTime
            history.sort((h1, h2) -> h1.getTransitionTime().compareTo(h2.getTransitionTime()));
            List<DealStateTransitionHistoryResponse> transitions = history.stream()
                .map(h -> new DealStateTransitionHistoryResponse(
                    h.getDealId(),
                    h.getFromState(),
                    h.getToState(),
                    h.getEvent(),
                    h.getTransitionTime()))
                .collect(Collectors.toList());

            // Get current state
            DealState currentState = stateMachineService.getState(dealId);

            DealStateTransitionHistoryWrapper wrapper = new DealStateTransitionHistoryWrapper(
                dealId,
                transitions,
                transitions.size(),
                currentState.name()
            );

            return ResponseEntity.ok(wrapper);
        } catch (DealStateException e) {
            throw e;
        }
    }
} 