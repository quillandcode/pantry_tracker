package com.quillandcode.pantry_tracker.service;

import com.quillandcode.pantry_tracker.dto.CreateVisitRequest;
import com.quillandcode.pantry_tracker.dto.VisitResponse;
import com.quillandcode.pantry_tracker.entity.Visit;
import com.quillandcode.pantry_tracker.repository.VisitRepository;
import com.quillandcode.pantry_tracker.repository.VisitSpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public VisitResponse createVisit(CreateVisitRequest request) {
        Visit visit = new Visit(request.firstName(), request.lastName(), request.zipCode(), request.householdSize());
        Visit savedVisit = visitRepository.save(visit);
        return VisitResponse.fromEntity(savedVisit);
    }

    public List<VisitResponse> getAllVisits(String zipCode, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<Visit> spec = Specification
                .where(VisitSpecification.hasZipCode(zipCode))
                .and(VisitSpecification.createdAfter(startDate))
                .and(VisitSpecification.createdBefore(endDate));
    
        return visitRepository.findAll(spec)
                .stream()
                .map(VisitResponse::fromEntity)
                .toList();
    }
    
    public String generateCsvExport(String zipCode, LocalDateTime startDate, LocalDateTime endDate) {
        List<VisitResponse> visits = getAllVisits(zipCode, startDate, endDate);
        StringBuilder sb = new StringBuilder();
        sb.append("ID,First Name,Last Name,Zip Code,Household Size,Created At\n");

        for (VisitResponse v : visits) {
            String escapedFirstName = v.firstName() != null ? v.firstName().replace("\"", "\"\"") : "";
            String escapedLastName = v.lastName() != null ? v.lastName().replace("\"", "\"\"") : "";

            sb.append(v.id()).append(",")
            .append("\"").append(escapedFirstName).append("\",")
            .append("\"").append(escapedLastName).append("\",")
            .append(v.zipCode()).append(",")
            .append(v.householdSize()).append(",")
            .append(v.createdAt()).append("\n");
        }

        return sb.toString();
    }
}