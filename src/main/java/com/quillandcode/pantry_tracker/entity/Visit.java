package com.quillandcode.pantry_tracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visitor_name", nullable = false)
    private String visitorName;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Column(name = "household_size", nullable = false)
    private Integer householdSize;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Visit() {
        // Required by JPA specification
    }

    public Visit(String visitorName, String zipCode, Integer householdSize) {
        this.visitorName = visitorName;
        this.zipCode = zipCode;
        this.householdSize = householdSize;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getHouseholdSize() {
        return householdSize;
    }

    public void setHouseholdSize(Integer householdSize) {
        this.householdSize = householdSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}