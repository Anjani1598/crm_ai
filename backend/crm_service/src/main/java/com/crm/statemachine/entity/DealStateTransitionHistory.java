package com.crm.statemachine.entity;

import com.crm.statemachine.DealState;
import com.crm.statemachine.DealEvent;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "deal_state_transition_history")
@Data
@NoArgsConstructor
public class DealStateTransitionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deal_id", nullable = false)
    private Long dealId;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_state", nullable = false)
    private DealState fromState;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_state", nullable = false)
    private DealState toState;

    @Enumerated(EnumType.STRING)
    @Column(name = "event")
    private DealEvent event;

    @CreationTimestamp
    @Column(name = "transition_time", nullable = false, updatable = false)
    private LocalDateTime transitionTime;

    public DealStateTransitionHistory(Long dealId, DealState fromState, DealState toState, DealEvent event) {
        this.dealId = dealId;
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
    }
} 