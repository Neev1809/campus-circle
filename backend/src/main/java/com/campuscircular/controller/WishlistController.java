package com.campuscircular.controller;

import com.campuscircular.dto.ResourceDto;
import com.campuscircular.model.User;
import com.campuscircular.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<ResourceDto>> getWishlist(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(wishlistService.getWishlist(user.getId()));
    }

    @PostMapping("/{resourceId}/toggle")
    public ResponseEntity<Map<String, Boolean>> toggle(
            @PathVariable String resourceId,
            @AuthenticationPrincipal User user) {
        boolean added = wishlistService.toggle(user.getId(), resourceId, user);
        return ResponseEntity.ok(Map.of("wishlisted", added));
    }

    @GetMapping("/{resourceId}/status")
    public ResponseEntity<Map<String, Boolean>> isWishlisted(
            @PathVariable String resourceId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("wishlisted", wishlistService.isWishlisted(user.getId(), resourceId)));
    }
}
