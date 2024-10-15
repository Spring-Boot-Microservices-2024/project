package org.naukma.spring.modulith.security;
import org.junit.jupiter.api.Test;
import org.naukma.spring.modulith.authentication.AuthenticationService;
import org.naukma.spring.modulith.event.EventController;
import org.naukma.spring.modulith.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private AuthenticationService authenticationService;

    @Value("${auth.key:booking}")
    private String apiKey;

    @Test
    void testCreateEventUnauthorized_shouldReturn400() throws Exception {
        // WHEN THEN
        mockMvc.perform(post("/events"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteEventUnauthorized_shouldReturn400() throws Exception {
        // WHEN THEN
        mockMvc.perform(delete("/events/{id}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetEventByIdUnauthorized_shouldReturn401() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/events/{eventId}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAllByOrganiserIdUnauthorized_shouldReturn401() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/events/organiser/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAllByParticipantIdUnauthorized_shouldReturn401() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/events/participant/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAllUnauthorized_shouldReturn401() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/events"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetEventByIdAuthorized_shouldReturn200() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/events/{eventId}", 1L)
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetAllByOrganiserIdAuthorized_shouldReturn200() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/events/organiser/{id}", 1L)
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetAllByParticipantIdAuthorized_shouldReturn200() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/events/participant/{id}", 1L)
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetAllAuthorized_shouldReturn200() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/events")
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCreateEventWithoutApiKey_shouldReturn403() throws Exception {
        // WHEN THEN
        mockMvc.perform(post("/events"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteEventWithoutApiKey_shouldReturn403() throws Exception {
        // WHEN THEN
        mockMvc.perform(delete("/events/{id}", 1L))
                .andExpect(status().isForbidden());
    }

}
