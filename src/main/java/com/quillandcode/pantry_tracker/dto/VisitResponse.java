package com.quillandcode.pantry_tracker.dto;

import com.quillandcode.pantry_tracker.entity.Visit;
import java.time.LocalDateTime;

public record VisitResponse(
    Long id,
    String firstName,
    String lastName,
    String zipCode,
    Integer householdSize,
    LocalDateTime createdAt
) {
    public static VisitResponse fromEntity(Visit visit) {
        return new VisitResponse(
            visit.getId(),
            visit.getFirstName(),
            visit.getLastName(),
            visit.getZipCode(),
            visit.getHouseholdSize(),
            visit.getCreatedAt()
        );
    }
}