package com.campuscircular.service;

import com.campuscircular.dto.ResourceDto;
import com.campuscircular.model.Resource;
import com.campuscircular.model.User;
import com.campuscircular.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public List<ResourceDto> getAll(String search, String category, String sort) {
        List<Resource> resources;

        if (search != null && !search.isBlank()) {
            resources = resourceRepository.searchResources(search.trim());
        } else if (category != null && !category.equalsIgnoreCase("all") && !category.isBlank()) {
            resources = resourceRepository.findByCategory(category.toLowerCase());
        } else {
            resources = resourceRepository.findAll();
        }

        // Apply category filter after search if both given
        if (search != null && !search.isBlank() && category != null && !category.equalsIgnoreCase("all")) {
            String cat = category.toLowerCase();
            resources = resources.stream()
                    .filter(r -> r.getCategory().equals(cat))
                    .collect(Collectors.toList());
        }

        // Sort
        if ("cheapest".equals(sort)) {
            resources.sort(Comparator.comparingInt(Resource::getPricePerDay));
        } else if ("closest".equals(sort)) {
            resources.sort(Comparator.comparingDouble(Resource::getDistance));
        } else if ("rating".equals(sort)) {
            resources.sort(Comparator.comparingDouble(Resource::getRating).reversed());
        } else {
            // bestMatch: available + trustScore + distance
            resources.sort((a, b) -> {
                double scoreA = (a.isAvailable() ? 20 : 0) + a.getOwner().getTrustScore() * 0.3 + (5 - a.getDistance()) * 10;
                double scoreB = (b.isAvailable() ? 20 : 0) + b.getOwner().getTrustScore() * 0.3 + (5 - b.getDistance()) * 10;
                return Double.compare(scoreB, scoreA);
            });
        }

        return resources.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public ResourceDto getById(String id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found: " + id));
        return mapToDto(resource);
    }

    public List<String> getCategories() {
        return resourceRepository.findAll().stream()
                .map(Resource::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public ResourceDto mapToDto(Resource r) {
        User owner = r.getOwner();
        return ResourceDto.builder()
                .id(r.getId())
                .name(r.getName())
                .category(r.getCategory())
                .condition(r.getCondition())
                .available(r.isAvailable())
                .distance(r.getDistance())
                .rating(r.getRating())
                .pricePerDay(r.getPricePerDay())
                .deposit(r.getDeposit())
                .description(r.getDescription())
                .accessories(r.getAccessories())
                .locationLabel(r.getLocationLabel())
                .locationArea(r.getLocationArea())
                .locationLat(r.getLocationLat())
                .locationLng(r.getLocationLng())
                .exchangeCount(r.getExchangeCount())
                .damageReportCount(r.getDamageReportCount())
                .owner(ResourceDto.OwnerDto.builder()
                        .id(owner.getId())
                        .name(owner.getName())
                        .department(owner.getDepartment())
                        .year(owner.getYear())
                        .verified(owner.isVerified())
                        .trustScore(owner.getTrustScore())
                        .phone(owner.getPhone())
                        .email(owner.getEmail())
                        .build())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
