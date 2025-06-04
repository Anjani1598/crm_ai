package com.crm.domain.model;

import com.crm.statemachine.entity.DealStateEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "deals")
@JsonIdentityInfo(
        generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@EntityListeners(AuditingEntityListener.class)
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String projectName;

    @Column
    private String projectType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private BigDecimal estimatedBudget;

    @Column
    private BigDecimal amount;

    @Column
    private LocalDate targetStartDate;

    @Column
    private LocalDate targetCompletionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "deal")
    private DealStateEntity dealState;

    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DealRequirement> requirements = new ArrayList<>();

    public void addRequirement(DealRequirement requirement) {
        requirements.add(requirement);
        requirement.setDeal(this);
    }

    public void removeRequirement(DealRequirement requirement) {
        requirements.remove(requirement);
        requirement.setDeal(null);
    }
} 