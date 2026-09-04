package com.quillandcode.pantry_tracker.repository;

import com.quillandcode.pantry_tracker.entity.Visit;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class VisitSpecification {

    public static Specification<Visit> hasZipCode(String zipCode) {
        return (root, query, criteriaBuilder) -> {
            if (zipCode == null || zipCode.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("zipCode"), zipCode);
        };
    }

    public static Specification<Visit> createdAfter(LocalDateTime startDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
        };
    }

    public static Specification<Visit> createdBefore(LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
        };
    }
}