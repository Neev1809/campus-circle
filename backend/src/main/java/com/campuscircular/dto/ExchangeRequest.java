package com.campuscircular.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class ExchangeRequest {

    @Data
    public static class CreateExchangeRequest {
        @NotBlank
        private String resourceId;
        @Min(1)
        private int duration;
    }

    @Data
    public static class UpdateStatusRequest {
        private String reason;   // for reject
        private Integer rating;  // for rate
        private String conditionAfter; // for inspection
        private int damageDeduction;
        private int lateFee;
        private int refundAmount;
    }
}
