package com.example.kinoxp.service;

import com.example.kinoxp.dto.PricingServiceRequest;
import com.example.kinoxp.dto.PricingServiceResponse;
import com.example.kinoxp.security.ServiceJwtTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class PricingServiceClient {

    private final RestClient restClient;
    private final ServiceJwtTokenService serviceJwtTokenService;

    public PricingServiceClient(
            RestClient.Builder restClientBuilder,
            ServiceJwtTokenService serviceJwtTokenService,
            @Value("${pricing.service.base-url}") String pricingServiceBaseUrl
    ) {
        this.restClient = restClientBuilder.baseUrl(pricingServiceBaseUrl).build();
        this.serviceJwtTokenService = serviceJwtTokenService;
    }

    public PricingServiceResponse calculatePrice(double basePrice, int ticketCount) {
        try {
            return restClient.post()
                    .uri("/api/internal/pricing/calculate")
                    .header("Authorization", "Bearer " + serviceJwtTokenService.createServiceToken())
                    .body(new PricingServiceRequest(basePrice, ticketCount))
                    .retrieve()
                    .body(PricingServiceResponse.class);
        } catch (RestClientException exception) {
            throw new IllegalStateException("Pricing service is unavailable right now.", exception);
        }
    }
}
