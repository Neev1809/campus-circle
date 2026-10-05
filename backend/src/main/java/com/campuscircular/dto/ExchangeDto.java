package com.campuscircular.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDto {
    private String id;
    private String resourceId;
    private String resourceName;
    private String resourceCategory;
    private String borrowerId;
    private String borrowerName;
    private String lenderId;
    private String lenderName;
    private String status;
    private int duration;
    private int borrowingCharge;
    private int platformFee;
    private int deposit;
    private int totalAmount;
    private int refundAmount;
    private int lateFee;
    private int damageDeduction;
    private Integer rating;
    private String rejectReason;
    private String conditionBefore;
    private String conditionAfter;
    private Instant requestedAt;
    private Instant acceptedAt;
    private Instant handoverAt;
    private Instant returnDue;
    private Instant returnedAt;
    private Instant settledAt;
    private Instant ratedAt;
    private Instant rejectedAt;
    private Instant cancelledAt;
}
