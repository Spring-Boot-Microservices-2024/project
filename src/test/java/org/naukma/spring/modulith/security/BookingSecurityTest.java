package org.naukma.spring.modulith.security;
import org.junit.jupiter.api.Test;
import org.naukma.spring.modulith.authentication.AuthenticationService;
import org.naukma.spring.modulith.booking.BookingController;
import org.naukma.spring.modulith.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private AuthenticationService authenticationService;

    @Value("${auth.key:booking}")
    private String apiKey;

    @Test
    void testRegisterUserForEventUnauthorized_shouldReturn400() throws Exception {
        // WHEN THEN
        mockMvc.perform(put("/book/{eventId}/register/{userId}", 1L, 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testUnregisterUserFromEventUnauthorized_shouldReturn400() throws Exception {
        // WHEN THEN
        mockMvc.perform(put("/book/{eventId}/unregister/{userId}", 1L, 1L))
                .andExpect(status().is4xxClientError());
    }
}

