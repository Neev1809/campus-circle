package com.campuscircular.service;

import com.campuscircular.dto.ExchangeDto;
import com.campuscircular.dto.ExchangeRequest;
import com.campuscircular.model.Exchange;
import com.campuscircular.model.Exchange.ExchangeStatus;
import com.campuscircular.model.Resource;
import com.campuscircular.model.User;
import com.campuscircular.repository.CartRepository;
import com.campuscircular.repository.ExchangeRepository;
import com.campuscircular.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private static final List<ExchangeStatus> TERMINAL_STATUSES =
            List.of(ExchangeStatus.rated, ExchangeStatus.cancelled, ExchangeStatus.rejected, ExchangeStatus.settlement);

    private final ExchangeRepository exchangeRepository;
    private final ResourceRepository resourceRepository;
    private final CartRepository cartRepository;

    /** Place orders for all items in a user's cart */
    @Transactional
    public List<ExchangeDto> placeOrderFromCart(User borrower) {
        List<com.campuscircular.model.CartItem> cartItems = cartRepository.findByUserId(borrower.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<Exchange> created = cartItems.stream().map(ci -> {
            Resource resource = ci.getResource();
            int borrowingCharge = resource.getPricePerDay();
            int platformFee = Math.round(borrowingCharge * 0.10f);
            int deposit = resource.getDeposit();

            Exchange exchange = Exchange.builder()
                    .id("ex" + UUID.randomUUID().toString().replace("-", "").substring(0, 10))
                    .resource(resource)
                    .borrower(borrower)
                    .lender(resource.getOwner())
                    .status(ExchangeStatus.requested)
                    .duration(1)
                    .borrowingCharge(borrowingCharge)
                    .platformFee(platformFee)
                    .deposit(deposit)
                    .totalAmount(borrowingCharge + platformFee + deposit)
                    .refundAmount(0)
                    .lateFee(0)
                    .damageDeduction(0)
                    .returnDue(Instant.now().plus(1, ChronoUnit.DAYS))
                    .build();
            return exchangeRepository.save(exchange);
        }).collect(Collectors.toList());

        cartRepository.deleteByUserId(borrower.getId());
        return created.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /** Place a single order for one resource */
    @Transactional
    public ExchangeDto createExchange(User borrower, ExchangeRequest.CreateExchangeRequest req) {
        Resource resource = resourceRepository.findById(req.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        if (!resource.isAvailable()) {
            throw new RuntimeException("Resource is not available for borrowing");
        }

        int borrowingCharge = resource.getPricePerDay() * req.getDuration();
        int platformFee = Math.round(borrowingCharge * 0.10f);
        int deposit = resource.getDeposit();

        Exchange exchange = Exchange.builder()
                .id("ex" + UUID.randomUUID().toString().replace("-", "").substring(0, 10))
                .resource(resource)
                .borrower(borrower)
                .lender(resource.getOwner())
                .status(ExchangeStatus.requested)
                .duration(req.getDuration())
                .borrowingCharge(borrowingCharge)
                .platformFee(platformFee)
                .deposit(deposit)
                .totalAmount(borrowingCharge + platformFee + deposit)
                .refundAmount(0)
                .lateFee(0)
                .damageDeduction(0)
                .returnDue(Instant.now().plus(req.getDuration(), ChronoUnit.DAYS))
                .build();

        return mapToDto(exchangeRepository.save(exchange));
    }

    public List<ExchangeDto> getMyOrders(String userId) {
        return exchangeRepository.findByBorrowerId(userId).stream()
                .map(this::mapToDto)
                .sorted((a, b) -> b.getRequestedAt().compareTo(a.getRequestedAt()))
                .collect(Collectors.toList());
    }

    public List<ExchangeDto> getMyRequests(String userId) {
        return exchangeRepository.findByLenderId(userId).stream()
                .map(this::mapToDto)
                .sorted((a, b) -> b.getRequestedAt().compareTo(a.getRequestedAt()))
                .collect(Collectors.toList());
    }

    public List<ExchangeDto> getAllExchanges() {
        return exchangeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ExchangeDto getById(String id) {
        return mapToDto(exchangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exchange not found")));
    }

    @Transactional
    public ExchangeDto approve(String exchangeId) {
        Exchange ex = getExchange(exchangeId);
        validateStatus(ex, ExchangeStatus.requested);
        ex.setStatus(ExchangeStatus.accepted);
        ex.setAcceptedAt(Instant.now());
        return mapToDto(exchangeRepository.save(ex));
    }

    @Transactional
    public ExchangeDto reject(String exchangeId, String reason) {
        Exchange ex = getExchange(exchangeId);
        validateStatus(ex, ExchangeStatus.requested);
        ex.setStatus(ExchangeStatus.rejected);
        ex.setRejectReason(reason);
        ex.setRejectedAt(Instant.now());
        return mapToDto(exchangeRepository.save(ex));
    }

    @Transactional
    public ExchangeDto cancel(String exchangeId, User user) {
        Exchange ex = getExchange(exchangeId);
        if (!ex.getBorrower().getId().equals(user.getId())) {
            throw new RuntimeException("Only the borrower can cancel");
        }
        validateStatus(ex, ExchangeStatus.requested);
        ex.setStatus(ExchangeStatus.cancelled);
        ex.setCancelledAt(Instant.now());
        return mapToDto(exchangeRepository.save(ex));
    }

    @Transactional
    public ExchangeDto advanceStatus(String exchangeId) {
        Exchange ex = getExchange(exchangeId);
        ExchangeStatus[] statuses = ExchangeStatus.values();
        int idx = ex.getStatus().ordinal();
        if (idx >= statuses.length - 1) {
            throw new RuntimeException("Already at terminal status");
        }
        ExchangeStatus next = statuses[idx + 1];
        ex.setStatus(next);
        setTimestamp(ex, next);
        return mapToDto(exchangeRepository.save(ex));
    }

    @Transactional
    public ExchangeDto updateExchange(String exchangeId, ExchangeRequest.UpdateStatusRequest req) {
        Exchange ex = getExchange(exchangeId);
        if (req.getRating() != null) {
            ex.setRating(req.getRating());
            ex.setStatus(ExchangeStatus.rated);
            ex.setRatedAt(Instant.now());
        }
        if (req.getConditionAfter() != null) {
            ex.setConditionAfter(req.getConditionAfter());
        }
        if (req.getDamageDeduction() > 0) ex.setDamageDeduction(req.getDamageDeduction());
        if (req.getLateFee() > 0) ex.setLateFee(req.getLateFee());
        if (req.getRefundAmount() > 0) ex.setRefundAmount(req.getRefundAmount());
        return mapToDto(exchangeRepository.save(ex));
    }

    // ---- helpers ----

    private Exchange getExchange(String id) {
        return exchangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exchange not found: " + id));
    }

    private void validateStatus(Exchange ex, ExchangeStatus expected) {
        if (ex.getStatus() != expected) {
            throw new RuntimeException("Invalid status transition from " + ex.getStatus());
        }
    }

    private void setTimestamp(Exchange ex, ExchangeStatus status) {
        Instant now = Instant.now();
        switch (status) {
            case accepted     -> ex.setAcceptedAt(now);
            case handover     -> ex.setHandoverAt(now);
            case borrowed     -> ex.setReturnedAt(now);
            case settlement   -> ex.setSettledAt(now);
            case rated        -> ex.setRatedAt(now);
            case rejected     -> ex.setRejectedAt(now);
            case cancelled    -> ex.setCancelledAt(now);
            default           -> {}
        }
    }

    public ExchangeDto mapToDto(Exchange ex) {
        return ExchangeDto.builder()
                .id(ex.getId())
                .resourceId(ex.getResource().getId())
                .resourceName(ex.getResource().getName())
                .resourceCategory(ex.getResource().getCategory())
                .borrowerId(ex.getBorrower().getId())
                .borrowerName(ex.getBorrower().getName())
                .lenderId(ex.getLender().getId())
                .lenderName(ex.getLender().getName())
                .status(ex.getStatus().name())
                .duration(ex.getDuration())
                .borrowingCharge(ex.getBorrowingCharge())
                .platformFee(ex.getPlatformFee())
                .deposit(ex.getDeposit())
                .totalAmount(ex.getTotalAmount())
                .refundAmount(ex.getRefundAmount())
                .lateFee(ex.getLateFee())
                .damageDeduction(ex.getDamageDeduction())
                .rating(ex.getRating())
                .rejectReason(ex.getRejectReason())
                .conditionBefore(ex.getConditionBefore())
                .conditionAfter(ex.getConditionAfter())
                .requestedAt(ex.getRequestedAt())
                .acceptedAt(ex.getAcceptedAt())
                .handoverAt(ex.getHandoverAt())
                .returnDue(ex.getReturnDue())
                .returnedAt(ex.getReturnedAt())
                .settledAt(ex.getSettledAt())
                .ratedAt(ex.getRatedAt())
                .rejectedAt(ex.getRejectedAt())
                .cancelledAt(ex.getCancelledAt())
                .build();
    }
}
