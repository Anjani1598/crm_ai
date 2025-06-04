package com.crm.statemachine.dto;

import com.crm.statemachine.DealEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for state transition.
 * 
 * Example:
 * {
 *     "event": "START_NEGOTIATION"
 * }
 */
@Data
@Schema(description = "Request for transitioning deal state")
public class DealStateTransitionRequest {
    @NotNull(message = "Event cannot be null")
    @Schema(description = "The event that triggers the state transition", 
            example = "START_NEGOTIATION",
            required = true)
    private DealEvent event;
} 