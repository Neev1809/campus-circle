package com.campuscircular.repository;

import com.campuscircular.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, String> {
    List<Exchange> findByBorrowerId(String borrowerId);
    List<Exchange> findByLenderId(String lenderId);
    List<Exchange> findByBorrowerIdOrLenderId(String borrowerId, String lenderId);
    List<Exchange> findByResourceId(String resourceId);
    
    Optional<Exchange> findByResourceIdAndStatusNotIn(
        String resourceId, List<Exchange.ExchangeStatus> statuses);
}
