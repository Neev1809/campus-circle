package com.campuscircular.service;

import com.campuscircular.dto.AuthDto;
import com.campuscircular.dto.UserDto;
import com.campuscircular.model.User;
import com.campuscircular.repository.UserRepository;
import com.campuscircular.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole().name());
        return buildResponse(token, user);
    }

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .id("u" + UUID.randomUUID().toString().replace("-", "").substring(0, 8))
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.STUDENT)
                .department(request.getDepartment())
                .year(request.getYear())
                .phone(request.getPhone())
                .trustScore(70)
                .verified(false)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getRole().name());
        return buildResponse(token, user);
    }

    private AuthDto.AuthResponse buildResponse(String token, User user) {
        AuthDto.AuthResponse response = new AuthDto.AuthResponse();
        response.setToken(token);
        response.setUser(mapToDto(user));
        return response;
    }

    public static UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .department(user.getDepartment())
                .year(user.getYear())
                .verified(user.isVerified())
                .trustScore(user.getTrustScore())
                .onTimeReturnRate(user.getOnTimeReturnRate())
                .conditionAccuracy(user.getConditionAccuracy())
                .responseRate(user.getResponseRate())
                .successfulExchanges(user.getSuccessfulExchanges())
                .unresolvedDisputes(user.getUnresolvedDisputes())
                .phone(user.getPhone())
                .badges(user.getBadges())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
