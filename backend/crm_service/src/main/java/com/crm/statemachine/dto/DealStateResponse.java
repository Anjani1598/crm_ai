package com.crm.statemachine.dto;

import com.crm.statemachine.DealState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response DTO for deal state operations.
 * 
 * Example:
 * {
 *     "dealId": "123",
 *     "currentState": "NEGOTIATION",
 *     "transitionSuccessful": true
 * }
 */
@Data
@AllArgsConstructor
@Schema(description = "Response containing deal state information")
public class DealStateResponse {
    @Schema(description = "The ID of the deal", example = "123")
    private Long dealId;

    @Schema(description = "The current state of the deal", example = "NEGOTIATION")
    private DealState currentState;

    @Schema(description = "Whether the state transition was successful", example = "true")
    private boolean transitionSuccessful;
} 