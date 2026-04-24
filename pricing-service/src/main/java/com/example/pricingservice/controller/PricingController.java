package com.example.pricingservice.controller;

import com.example.pricingservice.dto.PricingRequest;
import com.example.pricingservice.dto.PricingResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/pricing")
public class PricingController {

    @PostMapping("/calculate")
    @PreAuthorize("hasAuthority('SCOPE_pricing:calculate')")
    public PricingResponse calculate(@RequestBody PricingRequest request) {
        double subtotal = request.basePrice() * request.ticketCount();
        double bookingFee = request.ticketCount() >= 4 ? 0.0 : 12.0;
        double totalPrice = subtotal + bookingFee;

        return new PricingResponse(subtotal, bookingFee, totalPrice);
    }
}
