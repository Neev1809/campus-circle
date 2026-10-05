package com.campuscircular.controller;

import com.campuscircular.dto.ResourceDto;
import com.campuscircular.model.User;
import com.campuscircular.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<ResourceDto>> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCart(user.getId()));
    }

    @PostMapping("/{resourceId}")
    public ResponseEntity<Map<String, String>> addItem(
            @PathVariable String resourceId,
            @AuthenticationPrincipal User user) {
        cartService.addItem(user.getId(), resourceId, user);
        return ResponseEntity.ok(Map.of("message", "Item added to cart"));
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Map<String, String>> removeItem(
            @PathVariable String resourceId,
            @AuthenticationPrincipal User user) {
        cartService.removeItem(user.getId(), resourceId);
        return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user.getId());
        return ResponseEntity.ok(Map.of("message", "Cart cleared"));
    }

    @GetMapping("/{resourceId}/status")
    public ResponseEntity<Map<String, Boolean>> isInCart(
            @PathVariable String resourceId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("inCart", cartService.isInCart(user.getId(), resourceId)));
    }
}
