package com.quillandcode.pantry_tracker.service;

import com.quillandcode.pantry_tracker.dto.CreateVisitRequest;
import com.quillandcode.pantry_tracker.dto.VisitResponse;
import com.quillandcode.pantry_tracker.entity.Visit;
import com.quillandcode.pantry_tracker.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private VisitService visitService;

    private Visit sampleVisit;

    @BeforeEach
    void setUp() {
        sampleVisit = new Visit("Calamity", "Jane", "75009", 4);
    }

    @Test
    void createVisit_SavesAndReturnsResponse() {
        CreateVisitRequest request = new CreateVisitRequest("Calamity", "Jane", "75009", 4);
        when(visitRepository.save(any(Visit.class))).thenReturn(sampleVisit);

        VisitResponse response = visitService.createVisit(request);

        assertNotNull(response);
        assertEquals("Calamity", response.firstName());
        assertEquals("Jane", response.lastName());
        assertEquals("75009", response.zipCode());
        assertEquals(4, response.householdSize());
        verify(visitRepository, times(1)).save(any(Visit.class));
    }

    @Test
    void getAllVisits_AppliesSpecificationAndReturnsList() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        when(visitRepository.findAll(any(Specification.class))).thenReturn(List.of(sampleVisit));

        List<VisitResponse> results = visitService.getAllVisits("75009", start, end);

        assertEquals(1, results.size());
        assertEquals("Calamity", results.get(0).firstName());
        assertEquals("Jane", results.get(0).lastName());
        verify(visitRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void generateCsvExport_FormatsCsvAndEscapesQuotes() {
        Visit visitWithQuotes = new Visit("Calamity \"C\"", "Jane \"J\"", "75009", 4);
        when(visitRepository.findAll(any(Specification.class))).thenReturn(List.of(visitWithQuotes));

        String csv = visitService.generateCsvExport("75009", null, null);

        assertTrue(csv.startsWith("ID,First Name,Last Name,Zip Code,Household Size,Created At\n"));
        assertTrue(csv.contains("\"Calamity \"\"C\"\"\""));
        assertTrue(csv.contains("\"Jane \"\"J\"\"\""));
        assertTrue(csv.contains("75009"));
    }
}