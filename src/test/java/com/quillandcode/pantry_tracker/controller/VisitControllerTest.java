package com.quillandcode.pantry_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quillandcode.pantry_tracker.dto.CreateVisitRequest;
import com.quillandcode.pantry_tracker.dto.VisitResponse;
import com.quillandcode.pantry_tracker.service.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
@WithMockUser
class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockitoBean
    private VisitService visitService;

    @Test
    void createVisit_ValidInput_Returns201Created() throws Exception {
        CreateVisitRequest request = new CreateVisitRequest("Jane Doe", "75009", 4);
        VisitResponse mockResponse = new VisitResponse(1L, "Jane Doe", "75009", 4, LocalDateTime.now());

        when(visitService.createVisit(any(CreateVisitRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/visits")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.visitorName").value("Jane Doe"))
                .andExpect(jsonPath("$.zipCode").value("75009"))
                .andExpect(jsonPath("$.householdSize").value(4));
    }

    @Test
    void createVisit_InvalidInput_Returns400BadRequest() throws Exception {
        CreateVisitRequest invalidRequest = new CreateVisitRequest("", "ABCDE", 0);

        mockMvc.perform(post("/api/visits")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}