package com.campuscircular.controller;

import com.campuscircular.dto.ExchangeDto;
import com.campuscircular.dto.ExchangeRequest;
import com.campuscircular.model.User;
import com.campuscircular.service.ExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exchanges")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    /** GET /api/exchanges/orders — borrower's orders */
    @GetMapping("/orders")
    public ResponseEntity<List<ExchangeDto>> getMyOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(exchangeService.getMyOrders(user.getId()));
    }

    /** GET /api/exchanges/requests — lender's incoming requests */
    @GetMapping("/requests")
    public ResponseEntity<List<ExchangeDto>> getMyRequests(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(exchangeService.getMyRequests(user.getId()));
    }

    /** GET /api/exchanges/:id — single exchange detail */
    @GetMapping("/{id}")
    public ResponseEntity<ExchangeDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(exchangeService.getById(id));
    }

    /** POST /api/exchanges — create a single exchange (direct borrow) */
    @PostMapping
    public ResponseEntity<ExchangeDto> create(
            @Valid @RequestBody ExchangeRequest.CreateExchangeRequest req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(exchangeService.createExchange(user, req));
    }

    /** POST /api/exchanges/checkout — place order for all cart items */
    @PostMapping("/checkout")
    public ResponseEntity<List<ExchangeDto>> checkout(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(exchangeService.placeOrderFromCart(user));
    }

    /** PATCH /api/exchanges/:id/approve — lender approves */
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ExchangeDto> approve(@PathVariable String id) {
        return ResponseEntity.ok(exchangeService.approve(id));
    }

    /** PATCH /api/exchanges/:id/reject — lender rejects */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ExchangeDto> reject(
            @PathVariable String id,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.getOrDefault("reason", "") : "";
        return ResponseEntity.ok(exchangeService.reject(id, reason));
    }

    /** PATCH /api/exchanges/:id/cancel — borrower cancels */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ExchangeDto> cancel(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(exchangeService.cancel(id, user));
    }

    /** PATCH /api/exchanges/:id/advance — advance lifecycle stage */
    @PatchMapping("/{id}/advance")
    public ResponseEntity<ExchangeDto> advance(@PathVariable String id) {
        return ResponseEntity.ok(exchangeService.advanceStatus(id));
    }

    /** PATCH /api/exchanges/:id — update fields (rating, damage, inspection) */
    @PatchMapping("/{id}")
    public ResponseEntity<ExchangeDto> update(
            @PathVariable String id,
            @RequestBody ExchangeRequest.UpdateStatusRequest req) {
        return ResponseEntity.ok(exchangeService.updateExchange(id, req));
    }
}
