package com.example.kinoxp.dto;

public record PricingServiceRequest(
        double basePrice,
        int ticketCount
) {
}
