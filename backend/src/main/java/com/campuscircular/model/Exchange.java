package com.campuscircular.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "exchanges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Exchange {

    @Id
    @Column(length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangeStatus status;

    private int duration;          // in days
    private int borrowingCharge;
    private int platformFee;
    private int deposit;
    private int totalAmount;
    private int refundAmount;
    private int lateFee;
    private int damageDeduction;
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String rejectReason;

    @Column(columnDefinition = "TEXT")
    private String conditionBefore;

    @Column(columnDefinition = "TEXT")
    private String conditionAfter;

    @CreationTimestamp
    private Instant requestedAt;

    private Instant acceptedAt;
    private Instant handoverAt;
    private Instant returnDue;
    private Instant returnedAt;
    private Instant settledAt;
    private Instant ratedAt;
    private Instant rejectedAt;
    private Instant cancelledAt;

    public enum ExchangeStatus {
        requested, accepted, handover, borrowed, return_due, inspection, settlement, rated, rejected, cancelled
    }
}
