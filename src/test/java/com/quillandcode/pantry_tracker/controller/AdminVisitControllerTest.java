package com.quillandcode.pantry_tracker.controller;

import com.quillandcode.pantry_tracker.dto.VisitResponse;
import com.quillandcode.pantry_tracker.service.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminVisitController.class)
@AutoConfigureJson
@WithMockUser(roles = "ADMIN")
class AdminVisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisitService visitService;

    @Test
    void getAllVisits_WithoutFilters_Returns200AndList() throws Exception {
        VisitResponse visit = new VisitResponse(1L, "John", "Smith", "75009", 2, LocalDateTime.now());
        when(visitService.getAllVisits(null, null, null)).thenReturn(List.of(visit));

        mockMvc.perform(get("/api/admin/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"));
    }

    @Test
    void getAllVisits_WithDateFilters_ConvertsToFullDayBounds() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 9, 1);
        LocalDate endDate = LocalDate.of(2026, 9, 30);

        LocalDateTime expectedStart = startDate.atStartOfDay();
        LocalDateTime expectedEnd = endDate.atTime(LocalTime.MAX);

        mockMvc.perform(get("/api/admin/visits")
                .param("zipCode", "75009")
                .param("startDate", "2026-09-01")
                .param("endDate", "2026-09-30"))
                .andExpect(status().isOk());

        verify(visitService).getAllVisits(eq("75009"), eq(expectedStart), eq(expectedEnd));
    }

    @Test
    void exportCsv_ReturnsCsvFileHeaderAndContent() throws Exception {
        String mockCsv = "ID,First Name,Last Name,Zip Code,Household Size,Created At\n1,\"John\",\"Smith\",75009,2,2026-09-04T10:00:00\n";
        when(visitService.generateCsvExport(null, null, null)).thenReturn(mockCsv);

        mockMvc.perform(get("/api/admin/visits/export"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"pantry_visits.csv\""))
                .andExpect(content().contentType(MediaType.parseMediaType("text/csv")))
                .andExpect(content().string(mockCsv));
    }
}