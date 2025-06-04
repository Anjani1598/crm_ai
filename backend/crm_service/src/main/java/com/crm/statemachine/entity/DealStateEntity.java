package com.crm.statemachine.entity;

import com.crm.domain.model.Deal;
import com.crm.statemachine.DealState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "deal_states")
@Data
@NoArgsConstructor
public class DealStateEntity {
    @Id
    private Long dealId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealState state;

    @Column(nullable = false)
    private String lastEvent;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "dealId")
    @MapsId
    private Deal deal;

    public DealStateEntity(Long dealId, DealState state, String lastEvent) {
        this.dealId = dealId;
        this.state = state;
        this.lastEvent = lastEvent;
    }
} 