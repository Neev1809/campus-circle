package com.campuscircular.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDto {
    private String id;
    private String name;
    private String category;
    private String condition;
    private boolean available;
    private double distance;
    private double rating;
    private int pricePerDay;
    private int deposit;
    private String description;
    private List<String> accessories;
    private String locationLabel;
    private String locationArea;
    private Double locationLat;
    private Double locationLng;
    private int exchangeCount;
    private int damageReportCount;
    private OwnerDto owner;
    private Instant createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OwnerDto {
        private String id;
        private String name;
        private String department;
        private Integer year;
        private boolean verified;
        private int trustScore;
        private String phone;
        private String email;
    }
}
