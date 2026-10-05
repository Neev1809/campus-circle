package com.campuscircular.service;

import com.campuscircular.dto.ResourceDto;
import com.campuscircular.model.Resource;
import com.campuscircular.model.User;
import com.campuscircular.model.WishlistItem;
import com.campuscircular.repository.ResourceRepository;
import com.campuscircular.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceService resourceService;

    public List<ResourceDto> getWishlist(String userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(wi -> resourceService.mapToDto(wi.getResource()))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean toggle(String userId, String resourceId, User user) {
        if (wishlistRepository.existsByUserIdAndResourceId(userId, resourceId)) {
            wishlistRepository.deleteByUserIdAndResourceId(userId, resourceId);
            return false; // removed
        } else {
            Resource resource = resourceRepository.findById(resourceId)
                    .orElseThrow(() -> new RuntimeException("Resource not found"));
            WishlistItem item = WishlistItem.builder()
                    .user(user)
                    .resource(resource)
                    .build();
            wishlistRepository.save(item);
            return true; // added
        }
    }

    public boolean isWishlisted(String userId, String resourceId) {
        return wishlistRepository.existsByUserIdAndResourceId(userId, resourceId);
    }
}
