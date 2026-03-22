package com.pawemie.service;

import com.pawemie.exceptions.RateFetchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NbpServiceTest {

    private HttpClient httpClient;
    private ObjectMapper objectMapper;
    private NbpService nbpService;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        objectMapper = new ObjectMapper();
        nbpService = new NbpService(httpClient, objectMapper);
    }

    @Test
    void shouldReturnRateShenResponseIsValid() throws Exception {
        LocalDate date = LocalDate.of(2026, 3, 22);

        String jsonResponse = """
                {
                  "rates": [
                    { "mid": 4.1234 }
                  ]
                }
                """;

        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(jsonResponse);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        BigDecimal rate = nbpService.getRate(date);

        assertEquals(new BigDecimal("4.1234"), rate);
    }

    @Test
    void getRate_shouldThrowRateFetchException_whenStatusNot200() throws Exception {
        LocalDate date = LocalDate.of(2026, 3, 22);

        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.statusCode()).thenReturn(500);
        when(httpResponse.body()).thenReturn("Server error");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        RateFetchException exception = assertThrows(RateFetchException.class,
                () -> nbpService.getRate(date));

        assertTrue(exception.getMessage().contains("NBP API error"));
    }

    @Test
    void shouldThrowRateFetchExceptionWhenIOExceptionOccurs() throws Exception {
        LocalDate date = LocalDate.of(2026, 3, 22);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("IO error"));

        RateFetchException exception = assertThrows(RateFetchException.class,
                () -> nbpService.getRate(date));

        assertTrue(exception.getMessage().contains("Error downloading NBP data"));
    }


}