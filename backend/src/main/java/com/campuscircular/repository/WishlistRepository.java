package com.campuscircular.repository;

import com.campuscircular.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUserId(String userId);
    Optional<WishlistItem> findByUserIdAndResourceId(String userId, String resourceId);
    void deleteByUserIdAndResourceId(String userId, String resourceId);
    boolean existsByUserIdAndResourceId(String userId, String resourceId);
}
