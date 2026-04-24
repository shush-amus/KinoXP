package com.example.kinoxp.dto;

public record PricingServiceResponse(
        double subtotal,
        double bookingFee,
        double totalPrice
) {
}
