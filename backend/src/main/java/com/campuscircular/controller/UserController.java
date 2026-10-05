package com.campuscircular.controller;

import com.campuscircular.dto.UserDto;
import com.campuscircular.model.User;
import com.campuscircular.repository.UserRepository;
import com.campuscircular.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(AuthService.mapToDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable String id) {
        return userRepository.findById(id)
                .map(u -> ResponseEntity.ok(AuthService.mapToDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(AuthService::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
