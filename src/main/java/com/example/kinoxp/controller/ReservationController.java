package com.example.kinoxp.controller;

import com.example.kinoxp.model.Reservation;
import com.example.kinoxp.model.Showing;
import com.example.kinoxp.repository.ReservationRepository;
import com.example.kinoxp.repository.ShowingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final ShowingRepository showingRepository;

    public ReservationController(ReservationRepository reservationRepository, ShowingRepository showingRepository) {
        this.reservationRepository = reservationRepository;
        this.showingRepository = showingRepository;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {

        Optional<Showing> showingOptional = showingRepository.findById(reservation.getShowingId());

        if (showingOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Showing not found");
        }

        Showing showing = showingOptional.get();

        double totalPrice = showing.getPrice() * reservation.getTicketCount();

        reservation.setTotalPrice(totalPrice);

        Reservation savedReservation = reservationRepository.save(reservation);

        return ResponseEntity.ok(savedReservation);
    }
}
