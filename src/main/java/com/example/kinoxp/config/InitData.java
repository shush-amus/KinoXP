package com.example.kinoxp.config;

import com.example.kinoxp.model.Showing;
import com.example.kinoxp.repository.ShowingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class InitData implements CommandLineRunner {

    private final ShowingRepository showingRepository;

    public InitData(ShowingRepository showingRepository) {
        this.showingRepository = showingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (showingRepository.count() == 0) {
            Showing s1 = new Showing(
                    LocalDateTime.now().plusDays(1).withHour(18).withMinute(30),
                    95.0,
                    "Dune Part Two",
                    "Small Hall"
            );

            Showing s2 = new Showing(
                    LocalDateTime.now().plusDays(1).withHour(20).withMinute(0),
                    110.0,
                    "The Conjuring",
                    "Large Hall"
            );

            Showing s3 = new Showing(
                    LocalDateTime.now().plusDays(2).withHour(19).withMinute(15),
                    100.0,
                    "Titanic",
                    "Large Hall"
            );

            showingRepository.saveAll(List.of(s1, s2, s3));
        }
    }
}
