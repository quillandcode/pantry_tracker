package com.quillandcode.pantry_tracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quillandcode.pantry_tracker.controller.AdminVisitController;
import com.quillandcode.pantry_tracker.controller.VisitController;
import com.quillandcode.pantry_tracker.dto.CreateVisitRequest;
import com.quillandcode.pantry_tracker.service.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {VisitController.class, AdminVisitController.class})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockitoBean
    private VisitService visitService;

    @Test
    void publicKioskEndpoint_AllowsUnauthenticatedPost() throws Exception {
        CreateVisitRequest request = new CreateVisitRequest("Jane Doe", "75009", 3);

        mockMvc.perform(post("/api/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void adminEndpoint_RejectsUnauthenticatedGet() throws Exception {
        mockMvc.perform(get("/api/admin/visits"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminEndpoint_AllowsValidBasicAuth() throws Exception {
        mockMvc.perform(get("/api/admin/visits")
                .with(httpBasic("admin", "pantryadmin123")))
                .andExpect(status().isOk());
    }
}