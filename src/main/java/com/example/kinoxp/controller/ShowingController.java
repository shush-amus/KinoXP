package com.example.kinoxp.controller;

import com.example.kinoxp.model.Showing;
import com.example.kinoxp.repository.ShowingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showings")
public class ShowingController {

    private final ShowingRepository showingRepository;

    public ShowingController(ShowingRepository showingRepository) {
        this.showingRepository = showingRepository;
    }

    @GetMapping
    public List<Showing> getAllShowings() {
        return showingRepository.findAll();
    }

    @PostMapping
    public Showing createShowing(@RequestBody Showing showing) {
        return showingRepository.save(showing);
    }

    @DeleteMapping("/{id}")
    public void deleteShowing(@PathVariable Long id){
        showingRepository.deleteById(id);
    }
}