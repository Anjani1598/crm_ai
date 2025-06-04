package com.crm.statemachine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Wrapper response containing deal state transition history")
public class DealStateTransitionHistoryWrapper {
    @Schema(description = "The ID of the deal", example = "123")
    private Long dealId;

    @Schema(description = "List of state transitions", example = "[]")
    private List<DealStateTransitionHistoryResponse> transitions;

    @Schema(description = "Total number of transitions", example = "5")
    private int totalTransitions;

    @Schema(description = "Current state of the deal", example = "NEGOTIATION")
    private String currentState;
} 