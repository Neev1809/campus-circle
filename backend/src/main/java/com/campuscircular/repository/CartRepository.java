package com.campuscircular.repository;

import com.campuscircular.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(String userId);
    Optional<CartItem> findByUserIdAndResourceId(String userId, String resourceId);
    void deleteByUserId(String userId);
    void deleteByUserIdAndResourceId(String userId, String resourceId);
    boolean existsByUserIdAndResourceId(String userId, String resourceId);
}
