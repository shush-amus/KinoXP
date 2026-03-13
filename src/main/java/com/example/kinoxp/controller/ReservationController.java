package com.example.kinoxp.controller;

import com.example.kinoxp.model.Reservation;
import com.example.kinoxp.repository.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin
public class ReservationController {

    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        if (reservation.getCustomerName() == null || reservation.getCustomerName().isBlank()) {
            return ResponseEntity.badRequest().body("Customer name is required");
        }

        if (reservation.getTicketCount() < 1) {
            return ResponseEntity.badRequest().body("Ticket count must be at least 1");
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok(savedReservation);
    }
}