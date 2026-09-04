package com.quillandcode.pantry_tracker.controller;

import com.quillandcode.pantry_tracker.dto.CreateVisitRequest;
import com.quillandcode.pantry_tracker.dto.VisitResponse;
import com.quillandcode.pantry_tracker.service.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public ResponseEntity<VisitResponse> createVisit(@Valid @RequestBody CreateVisitRequest request) {
        VisitResponse response = visitService.createVisit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}