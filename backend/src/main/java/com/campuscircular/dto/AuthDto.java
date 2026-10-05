package com.campuscircular.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class AuthDto {

    @Data
    public static class LoginRequest {
        @NotBlank @Email
        private String email;
        @NotBlank @Size(min = 4)
        private String password;
    }

    @Data
    public static class RegisterRequest {
        @NotBlank @Size(min = 2, max = 100)
        private String name;
        @NotBlank @Email
        private String email;
        @NotBlank @Size(min = 4, max = 60)
        private String password;
        private String department;
        private Integer year;
        private String phone;
        // role defaults to STUDENT; admin must be set manually
    }

    @Data
    public static class AuthResponse {
        private String token;
        private UserDto user;
    }
}
