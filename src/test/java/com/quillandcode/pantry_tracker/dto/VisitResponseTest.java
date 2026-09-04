package com.quillandcode.pantry_tracker.dto;

import com.quillandcode.pantry_tracker.entity.Visit;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VisitResponseTest {

    @Test
    void whenFromEntity_thenAllFieldsMappedCorrectly() {
        Visit visit = new Visit("Jane Doe", "75009", 4);
        LocalDateTime now = LocalDateTime.now();
        
        // Use ReflectionTestUtils to set fields managed by JPA/database lifecycle
        ReflectionTestUtils.setField(visit, "id", 100L);
        ReflectionTestUtils.setField(visit, "createdAt", now);

        VisitResponse response = VisitResponse.fromEntity(visit);

        assertEquals(100L, response.id());
        assertEquals("Jane Doe", response.visitorName());
        assertEquals("75009", response.zipCode());
        assertEquals(4, response.householdSize());
        assertEquals(now, response.createdAt());
    }
}