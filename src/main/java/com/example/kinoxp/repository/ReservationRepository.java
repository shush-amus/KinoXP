package com.example.kinoxp.repository;

import com.example.kinoxp.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUsernameOrderByIdDesc(String username);

    Optional<Reservation> findByIdAndUsername(Long id, String username);
}
