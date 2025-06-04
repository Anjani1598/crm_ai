package com.crm.statemachine.dto;

import com.crm.statemachine.DealState;
import com.crm.statemachine.DealEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "Response containing deal state transition history information")
public class DealStateTransitionHistoryResponse {
    @Schema(description = "The ID of the deal", example = "123")
    private Long dealId;

    @Schema(description = "The previous state of the deal", example = "PROPOSAL_SENT")
    private DealState fromState;

    @Schema(description = "The new state of the deal", example = "NEGOTIATION")
    private DealState toState;

    @Schema(description = "The event that triggered the transition", example = "START_NEGOTIATION")
    private DealEvent event;

    @Schema(description = "The time when the transition occurred", example = "2024-03-20T10:30:00")
    private LocalDateTime transitionTime;
} 