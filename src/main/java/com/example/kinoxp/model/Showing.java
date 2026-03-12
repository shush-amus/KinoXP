package com.example.kinoxp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Showing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime showTime;
    private double price;

    private String movieTitle;
    private String theaterName;

    public Showing() {
    }

    public Showing(LocalDateTime showTime, double price, String movieTitle, String theaterName) {
        this.showTime = showTime;
        this.price = price;
        this.movieTitle = movieTitle;
        this.theaterName = theaterName;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovie(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheater(String theaterName) {
        this.theaterName = theaterName;
    }

}
