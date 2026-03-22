package com.pawemie.service;

import com.pawemie.exceptions.RateFetchException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;

public class NbpService {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public NbpService(HttpClient httpClient, ObjectMapper objectMapper) {
        this.client = httpClient;
        this.mapper = objectMapper;
    }

    public BigDecimal getRate(LocalDate date) throws RateFetchException {
        try {
            String url =
                    "https://api.nbp.pl/api/exchangerates/rates/A/USD/"
                            + date + "/?format=json";

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response =
                    client.send(request,
                            HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RateFetchException("NBP API error: " + response.body());
            }

            JsonNode root = mapper.readTree(response.body());

            JsonNode rates = root.get("rates");

            return rates
                    .get(0)
                    .get("mid")
                    .decimalValue();
        } catch (IOException | InterruptedException e) {
            throw new RateFetchException("Error downloading NBP data", e);
        }
    }
}
