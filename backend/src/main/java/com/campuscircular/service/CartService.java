package com.campuscircular.service;

import com.campuscircular.dto.ResourceDto;
import com.campuscircular.model.CartItem;
import com.campuscircular.model.Resource;
import com.campuscircular.model.User;
import com.campuscircular.repository.CartRepository;
import com.campuscircular.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceService resourceService;

    public List<ResourceDto> getCart(String userId) {
        return cartRepository.findByUserId(userId).stream()
                .map(ci -> resourceService.mapToDto(ci.getResource()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addItem(String userId, String resourceId, User user) {
        if (cartRepository.existsByUserIdAndResourceId(userId, resourceId)) {
            return; // already in cart
        }
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found: " + resourceId));

        CartItem item = CartItem.builder()
                .user(user)
                .resource(resource)
                .build();
        cartRepository.save(item);
    }

    @Transactional
    public void removeItem(String userId, String resourceId) {
        cartRepository.deleteByUserIdAndResourceId(userId, resourceId);
    }

    @Transactional
    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }

    public boolean isInCart(String userId, String resourceId) {
        return cartRepository.existsByUserIdAndResourceId(userId, resourceId);
    }
}
