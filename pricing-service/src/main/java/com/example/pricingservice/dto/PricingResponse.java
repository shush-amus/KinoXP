package com.example.pricingservice.dto;

public record PricingResponse(
        double subtotal,
        double bookingFee,
        double totalPrice
) {
}
