package com.campuscircular.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resources")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Resource {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(length = 50)
    private String condition;

    private boolean available = true;

    private double distance;

    private double rating;

    private int pricePerDay;

    private int deposit;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Owner (many resources can belong to one user)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ElementCollection
    @CollectionTable(name = "resource_accessories", joinColumns = @JoinColumn(name = "resource_id"))
    @Column(name = "accessory")
    private List<String> accessories = new ArrayList<>();

    // Location info
    @Column(length = 200)
    private String locationLabel;

    @Column(length = 200)
    private String locationArea;

    private Double locationLat;
    private Double locationLng;

    // History counts
    private int exchangeCount = 0;
    private int damageReportCount = 0;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
