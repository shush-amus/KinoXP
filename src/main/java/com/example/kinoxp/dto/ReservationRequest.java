package com.example.kinoxp.dto;

public record ReservationRequest(
        Long showingId,
        int ticketCount
) {
}
