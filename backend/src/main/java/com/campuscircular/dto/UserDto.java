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
public class UserDto {
    private String id;
    private String name;
    private String email;
    private String role;
    private String department;
    private Integer year;
    private boolean verified;
    private int trustScore;
    private int onTimeReturnRate;
    private int conditionAccuracy;
    private int responseRate;
    private int successfulExchanges;
    private int unresolvedDisputes;
    private String phone;
    private List<String> badges;
    private Instant createdAt;
}
