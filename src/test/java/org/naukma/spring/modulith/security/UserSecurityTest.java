package org.naukma.spring.modulith.security;

import org.junit.jupiter.api.Test;
import org.naukma.spring.modulith.authentication.AuthenticationService;
import org.naukma.spring.modulith.user.UserController;
import org.naukma.spring.modulith.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @Value("${auth.key:booking}")
    private String apiKey;

    @Test
    void testCreateUserUnauthorized_shouldReturn400() throws Exception {
        mockMvc.perform(post("/users"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteUserUnauthorized_shouldReturn400() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetUserByIdUnauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetUserByEmailUnauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/users/email/{email}", "test@example.com"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAllUsersUnauthorized_shouldReturn400() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetUserByIdAuthorized_shouldReturn200() throws Exception {
        mockMvc.perform(get("/users/{userId}", 1L)
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetUserByEmailAuthorized_shouldReturn200() throws Exception {
        mockMvc.perform(get("/users/email/{email}", "test@example.com")
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetAllUsersAuthorized_shouldReturn200() throws Exception {
        mockMvc.perform(get("/users")
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUserWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(post("/users"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteUserWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetUserByIdWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetUserByEmailWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(get("/users/email/{email}", "test@example.com"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAllUsersWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is4xxClientError());
    }
}

