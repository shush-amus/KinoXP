package com.example.pricingservice.dto;

public record PricingRequest(
        double basePrice,
        int ticketCount
) {
}
