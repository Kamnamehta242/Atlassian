package com.test.practiceratelimiter.controller;

import com.test.practiceratelimiter.service.RateLimitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RateLimitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RateLimitService rateLimitService;

    @InjectMocks
    private RateLimitController rateLimiterController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testRequestAllowed() throws Exception {
        String userId = "user1";

        // Mocking the service to return true
        when(rateLimitService.isRequestAllowed(userId)).thenReturn(true);


        mockMvc.perform(post("/api/rate-limit/request")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Request allowed"));

        // Verify that the service method was called
        verify(rateLimitService).isRequestAllowed(userId);
    }

    @Test
    public void testRequestRateLimitExceeded() throws Exception {
        String userId = "user1";

        // Mocking the service to return false
        when(rateLimitService.isRequestAllowed(userId)).thenReturn(false);

        mockMvc.perform(post("/api/rate-limit/request")
                        .param("userId", userId))
                .andExpect(status().is(429))
                .andExpect(content().string("Rate limit exceeded"));

        // Verify that the service method was called
        verify(rateLimitService).isRequestAllowed(userId);
    }

}