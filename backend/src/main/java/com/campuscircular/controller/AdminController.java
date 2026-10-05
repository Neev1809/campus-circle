package com.campuscircular.controller;

import com.campuscircular.dto.ExchangeDto;
import com.campuscircular.dto.ResourceDto;
import com.campuscircular.dto.UserDto;
import com.campuscircular.repository.UserRepository;
import com.campuscircular.service.AuthService;
import com.campuscircular.service.ExchangeService;
import com.campuscircular.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final ResourceService resourceService;
    private final ExchangeService exchangeService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll().stream()
                .map(AuthService::mapToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/resources")
    public ResponseEntity<List<ResourceDto>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAll(null, null, "bestMatch"));
    }

    @GetMapping("/exchanges")
    public ResponseEntity<List<ExchangeDto>> getAllExchanges() {
        return ResponseEntity.ok(exchangeService.getAllExchanges());
    }

    @PatchMapping("/exchanges/{id}/approve")
    public ResponseEntity<ExchangeDto> approve(@PathVariable String id) {
        return ResponseEntity.ok(exchangeService.approve(id));
    }

    @PatchMapping("/exchanges/{id}/reject")
    public ResponseEntity<ExchangeDto> reject(@PathVariable String id,
            @RequestBody(required = false) java.util.Map<String, String> body) {
        String reason = body != null ? body.getOrDefault("reason", "") : "";
        return ResponseEntity.ok(exchangeService.reject(id, reason));
    }

    @PatchMapping("/exchanges/{id}/advance")
    public ResponseEntity<ExchangeDto> advance(@PathVariable String id) {
        return ResponseEntity.ok(exchangeService.advanceStatus(id));
    }
}
