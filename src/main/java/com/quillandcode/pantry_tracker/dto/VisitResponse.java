package com.quillandcode.pantry_tracker.dto;

import com.quillandcode.pantry_tracker.entity.Visit;
import java.time.LocalDateTime;

public record VisitResponse(
    Long id,
    String visitorName,
    String zipCode,
    Integer householdSize,
    LocalDateTime createdAt
) {
    public static VisitResponse fromEntity(Visit visit) {
        return new VisitResponse(
            visit.getId(),
            visit.getVisitorName(),
            visit.getZipCode(),
            visit.getHouseholdSize(),
            visit.getCreatedAt()
        );
    }
}