package org.naukma.spring.modulith.security;

import org.junit.jupiter.api.Test;
import org.naukma.spring.modulith.authentication.AuthenticationService;
import org.naukma.spring.modulith.review.ReviewController;
import org.naukma.spring.modulith.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private AuthenticationService authenticationService;

    @Value("${auth.key:booking}")
    private String apiKey;

    @Test
    void testCreateReviewUnauthorized_shouldReturn400() throws Exception {
        mockMvc.perform(post("/reviews"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteReviewUnauthorized_shouldReturn400() throws Exception {
        mockMvc.perform(delete("/reviews/{reviewId}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetReviewByIdUnauthorized_shouldReturn400() throws Exception {
        mockMvc.perform(get("/reviews/{reviewId}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAllReviewsUnauthorized_shouldReturn400() throws Exception {
        mockMvc.perform(get("/reviews"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAllByAuthorIdUnauthorized_shouldReturn400() throws Exception {
        mockMvc.perform(get("/reviews/author/{authorId}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetReviewByIdAuthorized_shouldReturn200() throws Exception {
        mockMvc.perform(get("/reviews/{reviewId}", 1L)
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetAllReviewsAuthorized_shouldReturn200() throws Exception {
        mockMvc.perform(get("/reviews")
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetAllByAuthorIdAuthorized_shouldReturn200() throws Exception {
        mockMvc.perform(get("/reviews/author/{authorId}", 1L)
                        .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCreateReviewWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(post("/reviews"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteReviewWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(delete("/reviews/{reviewId}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetReviewByIdWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(get("/reviews/{reviewId}", 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAllReviewsWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(get("/reviews"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAllByAuthorIdWithoutApiKey_shouldReturn403() throws Exception {
        mockMvc.perform(get("/reviews/author/{authorId}", 1L))
                .andExpect(status().is4xxClientError());
    }
}

