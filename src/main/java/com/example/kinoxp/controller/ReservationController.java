package com.example.kinoxp.controller;

import com.example.kinoxp.dto.PricingServiceResponse;
import com.example.kinoxp.dto.ReservationRequest;
import com.example.kinoxp.model.Reservation;
import com.example.kinoxp.model.Showing;
import com.example.kinoxp.repository.ReservationRepository;
import com.example.kinoxp.repository.ShowingRepository;
import com.example.kinoxp.service.PricingServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final ShowingRepository showingRepository;
    private final PricingServiceClient pricingServiceClient;

    public ReservationController(
            ReservationRepository reservationRepository,
            ShowingRepository showingRepository,
            PricingServiceClient pricingServiceClient
    ) {
        this.reservationRepository = reservationRepository;
        this.showingRepository = showingRepository;
        this.pricingServiceClient = pricingServiceClient;
    }

    @GetMapping
    public List<Reservation> getOwnReservations(Authentication authentication) {
        return reservationRepository.findByUsernameOrderByIdDesc(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request, Authentication authentication) {
        if (request.showingId() == null) {
            return ResponseEntity.badRequest().body("A showing must be selected.");
        }

        if (request.ticketCount() < 1) {
            return ResponseEntity.badRequest().body("Ticket count must be at least 1.");
        }

        Optional<Showing> showingOptional = showingRepository.findById(request.showingId());
        if (showingOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Showing does not exist.");
        }

        Showing showing = showingOptional.get();
        PricingServiceResponse price = pricingServiceClient.calculatePrice(showing.getPrice(), request.ticketCount());

        Reservation reservation = new Reservation(
                authentication.getName(),
                request.ticketCount(),
                price.totalPrice(),
                request.showingId(),
                price.bookingFee()
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok(savedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id, Authentication authentication) {
        return reservationRepository.findByIdAndUsername(id, authentication.getName())
                .map(reservation -> {
                    reservationRepository.delete(reservation);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.status(404).body("Reservation not found for current user."));
    }
}
